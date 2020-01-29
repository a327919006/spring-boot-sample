package com.cn.boot.sample.api.model.po;

import javax.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Table(name = "t_order")
public class Order {
    @Id
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "order_id")
    private Long orderId;
}