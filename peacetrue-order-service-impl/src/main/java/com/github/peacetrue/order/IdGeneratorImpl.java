package com.github.peacetrue.order;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.github.peacetrue.util.DateTimeFormatterUtils.SHORT_YEAR_DATE;

/**
 * @author : xiayx
 * @since : 2021-04-27 22:59
 **/
@Component
@Slf4j
@ToString
public class IdGeneratorImpl implements IdGenerator {

    /** yyMMdd */
    private DateTimeFormatter todayFormatter;
    private String todayValue;
    private int serialNumberInitial;
    private int serialNumberLength;
    private long serialNumberMaximum;

    public IdGeneratorImpl() {
        this.setSerialNumberInitial(1);
        this.setSerialNumberLength(4);
        this.setTodayFormatter(SHORT_YEAR_DATE);
    }

    void setTodayFormatter(DateTimeFormatter todayFormatter) {
        this.todayFormatter = todayFormatter;
        this.todayValue = LocalDate.now().format(todayFormatter);
    }

    void setSerialNumberInitial(int serialNumberInitial) {
        this.serialNumberInitial = serialNumberInitial;
    }

    void setSerialNumberLength(int serialNumberLength) {
        this.serialNumberLength = serialNumberLength;
        this.serialNumberMaximum = (long) Math.pow(10, serialNumberLength) - 1;
    }

    @Override
    public synchronized String generateId() {
        String current = LocalDate.now().format(todayFormatter);
        if (!current.equals(todayValue)) {
            todayValue = current;
            serialNumberInitial = 1;
        }
        long serialNumber = serialNumberInitial++;
        if (serialNumber > serialNumberMaximum) {
            String message = String.format("序号[%s]超过最大长度[%s]的上限值[%s]", serialNumber, serialNumberLength, serialNumberMaximum);
            throw new IllegalStateException(message);
        }
        return todayValue + StringUtils.leftPad(String.valueOf(serialNumber), serialNumberLength, '0');
    }

}
