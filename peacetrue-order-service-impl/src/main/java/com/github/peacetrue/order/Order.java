package com.github.peacetrue.order;

import com.github.peacetrue.core.CreateModify;
import com.github.peacetrue.flow.FinalState;
import com.github.peacetrue.flow.Tense;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类
 *
 * @author xiayx
 */
@Table("`order`")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable, com.github.peacetrue.core.Id<Long>, CreateModify<Long> {

    private static final long serialVersionUID = 0L;

    /** 主键 */
    @Id
    private Long id;
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
    private LocalDateTime paymentTime;
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
    private LocalDateTime createdTime;
    /** 最近修改者. 主键 */
    private Long modifierId;
    /** 最近修改时间 */
    private LocalDateTime modifiedTime;

}
