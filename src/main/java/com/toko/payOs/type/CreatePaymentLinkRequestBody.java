package com.toko.payOs.type;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CreatePaymentLinkRequestBody {
	private int orderCode;
	private int amount;
	private String description;
	private String buyerName;
	private String buyerEmail;
	private String buyerPhone;
	private String buyerAddress;
	private List<Item> items;
	private String cancelUrl;
	private String returnUrl;
	private int expiredAt;
	private String signature = null;
}

