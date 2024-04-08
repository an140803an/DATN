package com.toko.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.toko.entity.OrderDetail;


@Repository
public interface OrdersDetailDAO extends JpaRepository<OrderDetail, Integer>{

	@Query("SELECT odd FROM OrderDetail odd WHERE odd.order.orderId = ?1")
	  List<OrderDetail> findByOrderId(Integer id);
	
	
	
	@Query("SELECT odd FROM OrderDetail odd WHERE odd.order.orderId = ?1")
	  List<OrderDetail> findByOrderDetailId(Integer orderId);
	
	
//	@Query("SELECT o FROM Order o LEFT JOIN (SELECT od.order, SUM(od.price * od.quantity) AS totalAmount FROM OrderDetail od GROUP BY od.order) subquery ON o.orderId = subquery.order WHERE  o.orderId = ?1")
//	 List<OrderDetail> findOrderWithTotalAmountByOrderId(Integer orderId);
//	


	@Query("SELECT SUM( odt.price * odt.quantity) as total_amount  FROM  Order od JOIN OrderDetail odt ON od.orderId = odt.order  WHERE od.account.account_id = ?1 AND od.orderId = ?2 ")
    int findOrderDetailsWithTotalAmount(Integer accountId ,Integer orderId);
	  
	  
	  @Query("SELECT SUM( odt.price * odt.quantity) as total_amount  FROM  Order od JOIN OrderDetail odt ON od.orderId = odt.order.orderId  WHERE  od.orderId = ?1 ")
		 int totalAmountOrders( Integer orderID );
	  
	  
	  
	}

