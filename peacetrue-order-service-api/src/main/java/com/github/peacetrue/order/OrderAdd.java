package com.github.peacetrue.order;

import com.github.peacetrue.core.OperatorImpl;
import com.github.peacetrue.flow.FinalState;
import com.github.peacetrue.flow.Tense;
import lombok.*;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * @author xiayx
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderAdd extends OperatorImpl<Long> {

    private static final long serialVersionUID = 0L;

    /** 商品. 主键 */
    @NotNull
    @Min(1)
    private Long goodsId;
    /** 商品件数 */
    @NotNull
    @Min(1)
    private Integer goodsCount;
    /** 收货地址. 主键 */
    @NotNull
    @Min(1)
    private Long shippingAddressId;
    //TODO template support BigDecimal
    /** 金额(元). 保留 2 位小数 */
    @NotNull
    @DecimalMin("0")
    @Digits(integer = 9, fraction = 2)
    private BigDecimal amount;
    /** 付款金额(元). 保留 2 位小数 */
    @NotNull
    @DecimalMin("0")
    @Digits(integer = 9, fraction = 2)
    private BigDecimal paymentAmount;
    /** 付款时间 */
    private LocalDateTime paymentTime;
    /** 环节. 下单、付款、发货、收货、取消、退款 */
    @NotNull
    private OrderNode node;
    /** 时态. 进行中、成功、失败 */
    @NotNull
    private Tense tenseState;
    /** 终态. 进行中、交易成功、交易失败 */
    @NotNull
    @Size(min = 1, max = 7)
    private FinalState finalState;
    /** 备注 */
    @NotNull
    @Size(min = 1, max = 255)
    private String remark;

}
