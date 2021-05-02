package com.github.peacetrue.order;

import com.github.peacetrue.core.IdCapable;
import com.github.peacetrue.core.OperatorCapable;
import com.github.peacetrue.core.Operators;
import com.github.peacetrue.core.Range;
import com.github.peacetrue.flow.FinalState;
import com.github.peacetrue.flow.Tense;
import com.github.peacetrue.spring.data.domain.SortUtils;
import com.github.peacetrue.spring.data.relational.core.query.CriteriaUtils;
import com.github.peacetrue.spring.data.relational.core.query.QueryUtils;
import com.github.peacetrue.spring.data.relational.core.query.UpdateUtils;
import com.github.peacetrue.spring.util.BeanUtils;
import com.github.peacetrue.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.LocalDateTime;
import java.util.Collections;

/**
 * 订单服务实现
 *
 * @author xiayx
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private R2dbcEntityOperations entityOperations;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private IdGenerator idGenerator;

    public static Criteria buildCriteria(OrderQuery params) {
        if (params.getPaymentTime() == null) params.setPaymentTime(Range.LocalDateTime.DEFAULT);
        if (params.getCreatedTime() == null) params.setCreatedTime(Range.LocalDateTime.DEFAULT);
        if (params.getModifiedTime() == null) params.setModifiedTime(Range.LocalDateTime.DEFAULT);
        return CriteriaUtils.and(
                CriteriaUtils.nullableCriteria(CriteriaUtils.smartIn("id"), params::getId),
                CriteriaUtils.nullableCriteria(Criteria.where("code")::like, value -> "%" + value + "%", params::getCode),
                CriteriaUtils.nullableCriteria(Criteria.where("goodsId")::is, params::getGoodsId),
                CriteriaUtils.nullableCriteria(Criteria.where("goodsCount")::is, params::getGoodsCount),
                CriteriaUtils.nullableCriteria(Criteria.where("shippingAddressId")::is, params::getShippingAddressId),
                CriteriaUtils.nullableCriteria(Criteria.where("paymentTime")::greaterThanOrEquals, params.getPaymentTime()::getLowerBound),
                CriteriaUtils.nullableCriteria(Criteria.where("paymentTime")::lessThan, DateUtils.DATE_CELL_EXCLUDE, params.getPaymentTime()::getUpperBound),
                CriteriaUtils.nullableCriteria(Criteria.where("node")::is, params::getNode),
                CriteriaUtils.nullableCriteria(Criteria.where("tenseState")::is, params::getTenseState),
                CriteriaUtils.nullableCriteria(Criteria.where("finalState")::is, params::getFinalState),
                CriteriaUtils.nullableCriteria(Criteria.where("creatorId")::is, params::getCreatorId),
                CriteriaUtils.nullableCriteria(Criteria.where("createdTime")::greaterThanOrEquals, params.getCreatedTime()::getLowerBound),
                CriteriaUtils.nullableCriteria(Criteria.where("createdTime")::lessThan, DateUtils.DATE_CELL_EXCLUDE, params.getCreatedTime()::getUpperBound),
                CriteriaUtils.nullableCriteria(Criteria.where("modifierId")::is, params::getModifierId),
                CriteriaUtils.nullableCriteria(Criteria.where("modifiedTime")::greaterThanOrEquals, params.getModifiedTime()::getLowerBound),
                CriteriaUtils.nullableCriteria(Criteria.where("modifiedTime")::lessThan, DateUtils.DATE_CELL_EXCLUDE, params.getModifiedTime()::getUpperBound)
        );
    }

    @Override
    @Transactional
    public Mono<OrderVO> add(OrderAdd params) {
        log.info("新增订单信息[{}]", params);
        Order entity = BeanUtils.map(params, Order.class);
        entity.setCode(idGenerator.generateId());
        entity.setNode(OrderNode.SUBMIT);
        entity.setTenseState(Tense.SUCCESS);
        entity.setFinalState(FinalState.DOING);
        Operators.setCreateModify(params, entity);
        BeanUtils.setDefaultValue(entity);
        return entityOperations.insert(entity)
                .map(item -> BeanUtils.map(item, OrderVO.class))
                .doOnNext(item -> eventPublisher.publishEvent(new PayloadApplicationEvent<>(item, params)));
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Page<OrderVO>> query(OrderQuery params, Pageable pageable, String... projection) {
        log.info("分页查询订单信息[{}]", params);
        Criteria where = buildCriteria(params);
        return entityOperations.count(Query.query(where), Order.class)
                .filter(total -> total > 0)
                .<Page<OrderVO>>flatMap(total -> {
                    Query query = Query.query(where).with(pageable).sort(pageable.getSortOr(SortUtils.SORT_CREATED_TIME_DESC));
                    return entityOperations.select(query, Order.class)
                            .map(item -> BeanUtils.map(item, OrderVO.class))
                            .collectList()
                            .doOnNext(item -> eventPublisher.publishEvent(new PayloadApplicationEvent<>(item, params)))
                            .map(item -> new PageImpl<>(item, pageable, total));
                })
                .switchIfEmpty(Mono.just(new PageImpl<>(Collections.emptyList(), pageable, 0L)))
                ;
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<OrderVO> query(OrderQuery params, Sort sort, String... projection) {
        log.info("全量查询订单信息[{}]", params);
        Criteria where = buildCriteria(params);
        Query query = Query.query(where).sort(sort).limit(100);
        return entityOperations.select(query, Order.class)
                .map(item -> BeanUtils.map(item, OrderVO.class))
                .doOnNext(item -> eventPublisher.publishEvent(new PayloadApplicationEvent<>(item, params)))
                ;
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<OrderVO> query(OrderQuery params, String... projection) {
        return query(params, SortUtils.SORT_CREATED_TIME_DESC, projection);
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<OrderVO> get(OrderGet params, String... projection) {
        log.info("获取订单信息[{}]", params);
//        Criteria where = CriteriaUtils.and(
//                CriteriaUtils.nullableCriteria(Criteria.where("id")::is, params::getId),
//        );
        Criteria where = Criteria.where("id").is(params.getId());
        return entityOperations.selectOne(Query.query(where), Order.class)
                .map(item -> BeanUtils.map(item, OrderVO.class))
                .doOnNext(item -> eventPublisher.publishEvent(new PayloadApplicationEvent<>(item, params)))
                ;
    }

    @Override
    @Transactional
    public Mono<Integer> modify(OrderModify params) {
        log.info("修改订单信息[{}]", params);
        return this.modifyGeneric(params);
    }

    private <T extends IdCapable<Long> & OperatorCapable<Long>> Mono<Integer> modifyGeneric(T params) {
        Query idQuery = QueryUtils.id(params::getId);
        return entityOperations.selectOne(idQuery, Order.class)
                .zipWhen(entity -> {
                    Order modify = BeanUtils.map(params, Order.class);
                    modify.setModifierId(params.getOperatorId());
                    modify.setModifiedTime(LocalDateTime.now());
                    Update update = UpdateUtils.selectiveUpdateFromExample(modify);
                    return entityOperations.update(idQuery, update, Order.class);
                })
                .map(tuple2 -> {
                    OrderVO vo = BeanUtils.map(tuple2.getT1(), OrderVO.class);
                    BeanUtils.copyProperties(params, vo, BeanUtils.EMPTY_PROPERTY_VALUE);
                    eventPublisher.publishEvent(new PayloadApplicationEvent<>(vo, params));
                    return tuple2.getT2();
                })
                .switchIfEmpty(Mono.just(0));
    }

    @Override
    @Transactional
    public Mono<Integer> delete(OrderDelete params) {
        log.info("删除订单信息[{}]", params);
        Query idQuery = QueryUtils.id(params::getId);
        return entityOperations.selectOne(idQuery, Order.class)
                .map(item -> BeanUtils.map(item, OrderVO.class))
                .zipWhen(region -> entityOperations.delete(idQuery, Order.class))
                .doOnNext(tuple2 -> eventPublisher.publishEvent(new PayloadApplicationEvent<>(tuple2.getT1(), params)))
                .map(Tuple2::getT2)
                .switchIfEmpty(Mono.just(0));
    }

}
