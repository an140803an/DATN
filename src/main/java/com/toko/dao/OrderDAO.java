package com.toko.dao;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.toko.entity.Order;
import com.toko.entity.Accounts;

@Repository
public interface OrderDAO extends JpaRepository<Order, Integer> {
	@Query("SELECT od FROM Order od WHERE od.account.account_id = ?1")
	List<Order> findOrderByAccId(Integer id);

//	@Query("SELECT o FROM Order o WHERE o.orderId = ?1")
//	  List<Order> findByOrderId(Integer OrderId);
	@Query("SELECT o FROM Order o WHERE o.orderId = :orderId")
	List<Order> findByOrderId(@Param("orderId") Integer orderId);

	@Query("SELECT o FROM Order o WHERE o.orderId = :orderId")
	Order findByOrderIdUpDate(@Param("orderId") Integer orderId);

	@Query("SELECT SUM( odt.price * odt.quantity) as total_amount  FROM  Order od JOIN OrderDetail odt ON od.orderId = odt.order  WHERE od.account.account_id = ?1 AND od.orderId = ?2 ")
	int findTotalAmount(Integer accountId, Integer orderID);

	@Query("SELECT od FROM Order od WHERE od.account.account_id = ?1 AND od.orderStatus = 'SUCCESS'")
	List<Order> findOrderByStatusSuccess(Integer id);

	@Query("SELECT od FROM Order od WHERE od.account.account_id = ?1 AND  od.shippingStatus = 'WAITING'  AND od.orderStatus is null")
	List<Order> findOrderByStatusWaiting(Integer id);

	@Query("SELECT od FROM Order od WHERE od.account.account_id = ?1 AND  od.orderStatus = 'CANCEL'")
	List<Order> findOrderByStatusCancel(Integer id);

	@Query("SELECT o FROM Order o WHERE o.orderStatus = ?1 AND o.orderDate >= ?2 AND o.orderDate <= ?3")
	List<Order> findByDateRange(String orderStatus, Date fromDate, Date toDate);

	@Query("SELECT COUNT(o) FROM Order o WHERE o.orderDate >= ?1 AND o.orderDate <= ?2")
	int countByDateRange(Date fromDate, Date toDate);

	@Query("SELECT COUNT(o) FROM Order o WHERE o.orderStatus = ?1 AND o.orderDate >= ?2 AND o.orderDate <= ?3")
	int countOrderStatusByDateRange(String orderStatus, Date fromDate, Date toDate);

	@Query("SELECT COUNT(o) FROM Order o WHERE o.orderStatus IS NULL AND o.orderDate >= ?1 AND o.orderDate <= ?2")
	int countOSNullByDateRange(Date fromDate, Date toDate);

	@Query("SELECT COUNT(o) FROM Order o WHERE o.paymentMethod = ?1 AND o.orderDate >= ?2 AND o.orderDate <= ?3")
	int countPaymentMethodByDateRange(String paymentMethod, Date fromDate, Date toDate);

	@Query("SELECT COUNT(o) FROM Order o WHERE o.paymentMethod = ?1 AND o.orderStatus = ?2 AND o.orderDate >= ?3 AND o.orderDate <= ?4")
	int countOSAndPMByDateRange(String paymentMethod, String orderStatus, Date fromDate, Date toDate);

	@Query("SELECT COUNT(o) FROM Order o WHERE o.paymentMethod = ?1 AND o.orderStatus IS NULL AND o.orderDate >= ?2 AND o.orderDate <= ?3")
	int countOSNullAndPMByDateRange(String paymentMethod, Date fromDate, Date toDate);

}
