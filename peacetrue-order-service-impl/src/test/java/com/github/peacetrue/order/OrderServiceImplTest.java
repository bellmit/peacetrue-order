package com.github.peacetrue.order;

import com.github.peacetrue.spring.util.BeanUtils;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author : xiayx
 * @since : 2020-05-22 16:43
 **/
@SpringBootTest(classes = TestServiceOrderAutoConfiguration.class)
@ActiveProfiles("order-service-test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderServiceImplTest {

    public static final EasyRandomParameters parameters = new EasyRandomParameters().randomize(Serializable.class, () -> 1L);
    public static final EasyRandom EASY_RANDOM = new EasyRandom(parameters);
    public static final OrderAdd ADD = EASY_RANDOM.nextObject(OrderAdd.class);
    public static final OrderModify MODIFY = EASY_RANDOM.nextObject(OrderModify.class);
    public static OrderVO vo;

    static {
        ADD.setOperatorId(1L);
        ADD.setAmount(BigDecimal.ONE);
        ADD.setPaymentAmount(BigDecimal.ONE);
        MODIFY.setOperatorId(1L);
        MODIFY.setAmount(BigDecimal.ONE);
        MODIFY.setPaymentAmount(BigDecimal.ONE);
    }

    @Autowired
    private OrderServiceImpl service;
    @Autowired
    private Environment environment;

    @Test
    @org.junit.jupiter.api.Order(10)
    void add() {
        service.add(ADD)
                .as(StepVerifier::create)
                .assertNext(data -> {
                    Assertions.assertEquals(data.getCreatorId(), ADD.getOperatorId());
                    vo = data;
                })
                .verifyComplete();
    }

    @Test
    @org.junit.jupiter.api.Order(20)
    void queryForPage() {
        OrderQuery params = BeanUtils.map(vo, OrderQuery.class);
        service.query(params, PageRequest.of(0, 10))
                .as(StepVerifier::create)
                .assertNext(page -> Assertions.assertEquals(1, page.getTotalElements()))
                .verifyComplete();
    }

    @Test
    @org.junit.jupiter.api.Order(30)
    void queryForList() {
        OrderQuery params = BeanUtils.map(vo, OrderQuery.class);
        service.query(params)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    @org.junit.jupiter.api.Order(40)
    void get() {
        OrderGet params = BeanUtils.map(vo, OrderGet.class);
        service.get(params)
                .as(StepVerifier::create)
                .assertNext(item -> Assertions.assertEquals(vo.getId(), item.getId()))
                .verifyComplete();
    }

    @Test
    @org.junit.jupiter.api.Order(50)
    void modify() {
        OrderModify params = MODIFY;
        params.setId(vo.getId());
        service.modify(params)
                .as(StepVerifier::create)
                .expectNext(1)
                .verifyComplete();
    }

    @Test
    @org.junit.jupiter.api.Order(60)
    void delete() {
        OrderDelete params = new OrderDelete(vo.getId());
        service.delete(params)
                .as(StepVerifier::create)
                .expectNext(1)
                .verifyComplete();
    }
}
