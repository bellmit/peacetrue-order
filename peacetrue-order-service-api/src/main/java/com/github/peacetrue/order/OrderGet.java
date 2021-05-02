package com.github.peacetrue.order;

import com.github.peacetrue.core.OperatorImpl;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


/**
 * @author xiayx
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderGet extends OperatorImpl<Long> {

    private static final long serialVersionUID = 0L;

    @NotNull
    @Min(1)
    private Long id;

}
