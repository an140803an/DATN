package com.toko.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toko.dao.ProductDetailDAO;
import com.toko.entity.OrderDetail;
import com.toko.entity.Product;
import com.toko.entity.ProductDetail;

@Service
public class ProductDetailService {
	@Autowired
	ProductDetailDAO dao;

	public List<ProductDetail> findAll() {
		List<ProductDetail> list = dao.findAll();
		return list;
	}

	public ProductDetail findById(Integer productId) {
		ProductDetail productDetail = dao.findById(productId).get();
		return productDetail;
	}

	public List<ProductDetail> findByProductId(Integer productId) {
		List<ProductDetail> list = dao.findByProductId(productId);
		return list;
	}

	public ProductDetail create(ProductDetail productDetail) {
		return dao.save(productDetail);
	}

	public boolean existedById(Integer productId) {
		return dao.existsById(productId);
	}

	public ProductDetail update(ProductDetail productDetail) {
		return dao.save(productDetail);
	}

	public void deleteById(Integer productId) {
		dao.deleteById(productId);
	}
	
//	public void updateByOrderDetailList(JsonNode dataList) {
//		try {
//			ObjectMapper mapper = new ObjectMapper();
//			TypeReference<List<OrderDetail>> type = new TypeReference<List<OrderDetail>>() {};
//			List<OrderDetail> orderDetailList = mapper.convertValue(dataList, type);
////			System.out.println("convertValue thành công: "+ orderDetailList);
//			
//			for(OrderDetail orderDetail : orderDetailList) {
//				ProductDetail pdd = dao.findById(orderDetail.getProductDetailId()).get();
//				if(orderDetail.getQuantity() >= pdd.getQuantity()) {
//					pdd.setQuantity(0);
//					dao.save(pdd);
////					System.out.println("Update thành công productDetail by id: "+pdd.getProductDetailId() +", qty: "+pdd.getQuantity());
//				}else {
//					pdd.setQuantity((pdd.getQuantity()-orderDetail.getQuantity()));
//					dao.save(pdd);		
////					System.out.println("Update thành công productDetail by id: "+pdd.getProductDetailId() +", qty: "+pdd.getQuantity());
//				}
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//	
//	}
	
	public void updateByOrderDetailList(List<OrderDetail> dataList) {
		try {	
			for(OrderDetail orderDetail : dataList) {
				ProductDetail pdd = dao.findById(orderDetail.getProductDetailId()).get();
				if(orderDetail.getQuantity() >= pdd.getQuantity()) {
					pdd.setQuantity(0);
					dao.save(pdd);
//					System.out.println("Update thành công productDetail by id: "+pdd.getProductDetailId() +", qty: "+pdd.getQuantity());
				}else {
					pdd.setQuantity((pdd.getQuantity()-orderDetail.getQuantity()));
					dao.save(pdd);		
//					System.out.println("Update thành công productDetail by id: "+pdd.getProductDetailId() +", qty: "+pdd.getQuantity());
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	
	}
	
	public void updateWhenOrderisCanceled(List<OrderDetail> orderDetailList) {
		try {
			for(OrderDetail orderDetail : orderDetailList) {
				ProductDetail pdd = dao.findById(orderDetail.getProductDetailId()).get();
					int originalQuantity = pdd.getQuantity();
					pdd.setQuantity(originalQuantity + orderDetail.getQuantity());
					dao.save(pdd);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	
	}
	
	public boolean checkInStock(Integer id , Integer qty) {
		ProductDetail pdd = findById(id);
			if(pdd != null && pdd.getQuantity() >= qty ) {
				return true;
			}
			return false;
		}
	
	
}
