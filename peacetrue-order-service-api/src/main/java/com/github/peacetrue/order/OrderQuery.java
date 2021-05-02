package com.github.peacetrue.order;

import com.github.peacetrue.core.OperatorImpl;
import com.github.peacetrue.core.Range;
import com.github.peacetrue.flow.FinalState;
import com.github.peacetrue.flow.Tense;
import lombok.*;

import java.math.BigDecimal;


/**
 * @author xiayx
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderQuery extends OperatorImpl<Long> {

    public static final OrderQuery DEFAULT = new OrderQuery();

    private static final long serialVersionUID = 0L;

    /** 主键 */
    private Long[] id;
    /** 编号 */
    private String code;
    /** 商品. 主键 */
    private Long goodsId;
    /** 商品件数 */
    private Integer goodsCount;
    /** 收货地址. 主键 */
    private Long shippingAddressId;
    /** 金额(元). 保留 2 位小数 */
    private BigDecimal amount;
    /** 付款金额(元). 保留 2 位小数 */
    private BigDecimal paymentAmount;
    /** 付款时间 */
    private Range.LocalDateTime paymentTime;
    /** 环节. 下单、付款、发货、收货、取消、退款 */
    private OrderNode node;
    /** 时态. 进行中、成功、失败 */
    private Tense tenseState;
    /** 终态. 进行中、交易成功、交易失败 */
    private FinalState finalState;
    /** 备注 */
    private String remark;
    /** 创建者. 主键 */
    private Long creatorId;
    /** 创建时间 */
    private Range.LocalDateTime createdTime;
    /** 最近修改者. 主键 */
    private Long modifierId;
    /** 最近修改时间 */
    private Range.LocalDateTime modifiedTime;

    public OrderQuery(Long[] id) {
        this.id = id;
    }

}
