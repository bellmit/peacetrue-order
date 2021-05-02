package com.github.peacetrue.order;

import com.github.peacetrue.core.CodeCapable;
import com.github.peacetrue.core.NameCapable;

public enum OrderNode implements CodeCapable, NameCapable {

    SUBMIT("下单"),
    PAY("付款"),
    DELIVER("发货"),
    RECEIVE("收货"),
    CANCEL("取消"),
    REFUND("退款"),
    ;

    private final String name;

    OrderNode(String name) {
        this.name = name;
    }

    @Override
    public String getCode() {
        return name();
    }

    @Override
    public String getName() {
        return name;
    }
}
