package com.github.peacetrue.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 订单控制器
 *
 * @author xiayx
 */
@Slf4j
@RestController
@RequestMapping(value = "/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Mono<OrderVO> addByForm(OrderAdd params) {
        log.info("新增订单信息(请求方法+表单参数)[{}]", params);
        return orderService.add(params);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<OrderVO> addByJson(@RequestBody OrderAdd params) {
        log.info("新增订单信息(请求方法+JSON参数)[{}]", params);
        return orderService.add(params);
    }

    @GetMapping(params = "page")
    public Mono<Page<OrderVO>> query(OrderQuery params, Pageable pageable, String... projection) {
        log.info("分页查询订单信息(请求方法+参数变量)[{}]", params);
        return orderService.query(params, pageable, projection);
    }

    @GetMapping
    public Flux<OrderVO> query(OrderQuery params, Sort sort, String... projection) {
        log.info("全量查询订单信息(请求方法+参数变量)[{}]", params);
        return orderService.query(params, sort, projection);
    }

    @GetMapping("/{id}")
    public Mono<OrderVO> getByUrlPathVariable(@PathVariable Long id, String... projection) {
        log.info("获取订单信息(请求方法+路径变量)详情[{}]", id);
        return orderService.get(new OrderGet(id), projection);
    }

    @RequestMapping("/get")
    public Mono<OrderVO> getByPath(OrderGet params, String... projection) {
        log.info("获取订单信息(请求路径+参数变量)详情[{}]", params);
        return orderService.get(params, projection);
    }

    @PutMapping(value = {"", "/*"}, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Mono<Integer> modifyByForm(OrderModify params) {
        log.info("修改订单信息(请求方法+表单参数)[{}]", params);
        return orderService.modify(params);
    }

    @PutMapping(value = {"", "/*"}, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Integer> modifyByJson(@RequestBody OrderModify params) {
        log.info("修改订单信息(请求方法+JSON参数)[{}]", params);
        return orderService.modify(params);
    }

    @DeleteMapping("/{id}")
    public Mono<Integer> deleteByUrlPathVariable(@PathVariable Long id) {
        log.info("删除订单信息(请求方法+URL路径变量)[{}]", id);
        return orderService.delete(new OrderDelete(id));
    }

    @DeleteMapping(params = "id")
    public Mono<Integer> deleteByUrlParamVariable(OrderDelete params) {
        log.info("删除订单信息(请求方法+URL参数变量)[{}]", params);
        return orderService.delete(params);
    }

    @RequestMapping(path = "/delete")
    public Mono<Integer> deleteByPath(OrderDelete params) {
        log.info("删除订单信息(请求路径+URL参数变量)[{}]", params);
        return orderService.delete(params);
    }

    /*------ 会员接口 -----*/
    @GetMapping(value = "/created", params = "page")
    public Mono<Page<OrderVO>> queryCreated(OrderQuery params, Pageable pageable, String... projection) {
        log.info("分页查询当前会员创建的订单信息(请求方法+参数变量)[{}]", params);
        return orderService.queryCreated(params, pageable, projection);
    }

}
