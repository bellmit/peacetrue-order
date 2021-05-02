package com.github.peacetrue.order;

import com.github.peacetrue.init.InitAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * @author : xiayx
 * @since : 2021-04-28 00:04
 **/
@Slf4j
@Component
@AutoConfigureAfter(InitAutoConfiguration.class)
public class IdGeneratorInitializer implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("应用启动完成，设置订单编号的初始序号");
        ApplicationContext applicationContext = event.getApplicationContext();
        DatabaseClient databaseClient = applicationContext.getBean(DatabaseClient.class);
        IdGeneratorImpl idGenerator = applicationContext.getBean(IdGeneratorImpl.class);
        maxCode(databaseClient)
                .map(code -> Integer.parseInt(code.substring(6)))
                .defaultIfEmpty(1)
                .doOnNext(serial -> log.debug("设置订单生成器的初始序号为[{}]", serial))
                .doOnNext(idGenerator::setSerialNumberInitial)
                .subscribeOn(Schedulers.immediate())
                .subscribe()
        ;
    }

    private static Mono<String> maxCode(DatabaseClient databaseClient) {
        return databaseClient
                .sql("select IFNULL(MAX(code),'0') from `order`")
                .map(row -> row.get(0, String.class))
                .first()
                .doOnNext(code -> log.debug("取得最大的订单编号为[{}]", code))
                ;
    }
}
