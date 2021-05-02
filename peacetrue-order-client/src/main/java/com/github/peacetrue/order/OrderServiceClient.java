package com.github.peacetrue.order;

import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Nullable;

/**
 * 订单客户端
 *
 * @author xiayx
 */
@ReactiveFeignClient(name = "peacetrue-order", url = "${peacetrue.Order.url:${peacetrue.server.url:}}")
public interface OrderServiceClient {

    @PostMapping(value = "/orders")
    Mono<OrderVO> add(OrderAdd params);

    @GetMapping(value = "/orders", params = "page")
    Mono<Page<OrderVO>> query(@Nullable @SpringQueryMap OrderQuery params, @Nullable Pageable pageable, @SpringQueryMap String... projection);

    @GetMapping(value = "/orders", params = "sort")
    Flux<OrderVO> query(@SpringQueryMap OrderQuery params, Sort sort, @SpringQueryMap String... projection);

    @GetMapping(value = "/orders")
    Flux<OrderVO> query(@SpringQueryMap OrderQuery params, @SpringQueryMap String... projection);

    @GetMapping(value = "/orders/get")
    Mono<OrderVO> get(@SpringQueryMap OrderGet params, @SpringQueryMap String... projection);

    @PutMapping(value = "/orders")
    Mono<Integer> modify(OrderModify params);

    @DeleteMapping(value = "/orders/delete")
    Mono<Integer> delete(@SpringQueryMap OrderDelete params);

}
