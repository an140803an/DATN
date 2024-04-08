package com.toko.payOs.type;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Item {
	private String name;
	private int quantity;
	private int price;
}
