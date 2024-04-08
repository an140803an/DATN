package com.toko.entity;

import java.sql.Date;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Orders")

public class Order {
	@Id
	@Column(name = "order_id")
	private int orderId;

	@ManyToOne
	@JoinColumn(name = "account_id")
	private Accounts account;

	@Column(name = "order_date")
	private Date orderDate;

	@Column(name = "customer_note")
	private String customerNote;

	
	@Column(name = "order_status")
	private String orderStatus;

	
	@Column(name = "payment_method")
	private String paymentMethod;

	@Column(name = "customer_name")
	private String customerName;
	
	@Column (name = "customer_phone")
	private String customerPhone;
	
	@Column (name = "customer_email")
	private String customerEmail;
	
	@Column (name = "customer_address")
	private String customerAddress;
	
	@Column (name = "shipping_status")
	private String shippingStatus;
	
	@Column (name = "shipping_money")
	private int shippingMoney;
	
	@Column (name = "payment_status")
	private String paymentStatus;

    @OneToMany(mappedBy = "order")
    List<OrderDetail> orderDetail;
	// Constructors, getters, and setters
	// You can generate constructors and getters/setters using your IDE or manually
}
