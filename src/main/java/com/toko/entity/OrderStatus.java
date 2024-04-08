package com.toko.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "Orderstatus")
public class OrderStatus {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_status_id")
	private int orderStatusId;

	@Column(name = "shipping_status")
	private String shippingStatus;

	@Column(name = "shipping_start_date")
	private Date shippingStartDate;
}
