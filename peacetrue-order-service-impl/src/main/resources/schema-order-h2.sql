drop table if exists `order`;
create table `order`
(
    id                  bigint unsigned primary key auto_increment,
    code                VARCHAR(255) UNIQUE                                         NOT NULL COMMENT '编号',
    goods_id            bigint unsigned                                             NOT NULL COMMENT '商品. 主键',
    goods_count         int unsigned                                                NOT NULL COMMENT '商品件数',
    shipping_address_id bigint unsigned                                             NOT NULL COMMENT '收货地址. 主键',
    amount              decimal(11, 2) unsigned                                     NOT NULL COMMENT '金额(元). 保留 2 位小数',
    payment_amount      decimal(11, 2) unsigned                                     NOT NULL COMMENT '付款金额(元). 保留 2 位小数',
    payment_time        datetime COMMENT '付款时间',
    node                enum ('SUBMIT','PAY','DELIVER','RECEIVE','CANCEL','REFUND') NOT NULL COMMENT '环节. 下单、付款、发货、收货、取消、退款',
    tense_state         enum ('DOING','SUCCESS','FAILURE')                          NOT NULL COMMENT '时态. 进行中、成功、失败',
    final_state         enum ('DOING','SUCCESS','FAILURE')                          NOT NULL COMMENT '终态. 进行中、交易成功、交易失败',
    remark              VARCHAR(255)                                                NOT NULL default '' COMMENT '备注',
    creator_id          bigint unsigned                                             not null comment '创建者. 主键',
    created_time        datetime                                                    not null comment '创建时间',
    modifier_id         bigint unsigned                                             not null comment '最近修改者. 主键',
    modified_time       timestamp                                                   not null comment '最近修改时间'
);
