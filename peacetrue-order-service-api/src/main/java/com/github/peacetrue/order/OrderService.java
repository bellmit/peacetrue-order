package com.github.peacetrue.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * 订单服务接口
 *
 * @author xiayx
 */
public interface OrderService {

    /** 新增 */
    Mono<OrderVO> add(OrderAdd params);

    /** 分页查询 */
    Mono<Page<OrderVO>> query(OrderQuery params, Pageable pageable, String... projection);

    /** 全量查询 */
    Flux<OrderVO> query(OrderQuery params, Sort sort, String... projection);

    /** 全量查询 */
    Flux<OrderVO> query(OrderQuery params, String... projection);

    /** 获取 */
    Mono<OrderVO> get(OrderGet params, String... projection);

    /** 修改 */
    Mono<Integer> modify(OrderModify params);

    /** 删除 */
    Mono<Integer> delete(OrderDelete params);

    /*--- 会员接口 ---*/

    /** 分页查询当前会员创建的订单 */
    default Mono<Page<OrderVO>> queryCreated(OrderQuery params, Pageable pageable, String... projection) {
        params.setCreatorId(Objects.requireNonNull(params.getOperatorId()));
        return this.query(params, pageable, projection);
    }

}
