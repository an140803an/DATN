package com.toko.rest.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

//import com.lib.payos.PayOS;
//import com.lib.payos.type.ItemData;
//import com.lib.payos.type.PaymentData;
//import com.toko.payOs.type.CreatePaymentLinkRequestBody;
import com.toko.entity.Order;
import com.toko.service.OrderDetailService;
import com.toko.service.OrderService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/order")
public class OderRestController {

	@Autowired
	OrderService service;
	
	@Autowired
	OrderDetailService odtService;

	@GetMapping("{id}")
	public Order getOne(@PathVariable("id") Integer id) {
		return service.findById(id);
	}

	@PostMapping("/setOneByJson")
	public Order setOne(@RequestBody JsonNode order) {
		return service.saveByJson(order);
	}

	@RequestMapping("/allOrderInCurrentYear")
	private int allOrder() {
		LocalDate firstDayOfYear = LocalDate.ofYearDay(LocalDate.now().getYear(), 1);

		// Lấy ngày cuối cùng của năm
		LocalDate lastDayOfYear = LocalDate.ofYearDay(LocalDate.now().getYear(), LocalDate.MAX.getDayOfYear());

		// Chuyển đổi LocalDate thành java.sql.Date nếu cần
		Date firstDayOfYearSql = Date.valueOf(firstDayOfYear);
		Date lastDayOfYearSql = Date.valueOf(lastDayOfYear);

		return service.countByDateRange(firstDayOfYearSql, lastDayOfYearSql);

	}

	@RequestMapping("/totalAmountOrdersInCurrentMonth")
	private int totalAmountOrdersInCurrentMonth() {
		LocalDate firstDayOfMonth = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1);
		// lấy ngày cuối cùng trong tháng
		LocalDate lastDayOfMonth = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1).plusMonths(1)
				.minusDays(1);
		// convert về kiểu sql.Date
		Date firstDayOfMonthSql = Date.valueOf(firstDayOfMonth);
		Date lastDayOfMonthSql = Date.valueOf(lastDayOfMonth);

		List<Order> orders = service.findByDateRange("SUCCESS", firstDayOfMonthSql, lastDayOfMonthSql);
		int total = 0;
		for (Order order : orders) {
			total += (Integer) (odtService.totalAmount(order.getOrderId()) + 30000);
		}
		return total;

	}

	@RequestMapping("/totalAmountOrdersInCurrentYear")
	private int totalAmountOrdersInCurrentYear() {
		LocalDate firstDayOfYear = LocalDate.ofYearDay(LocalDate.now().getYear(), 1);

		// Lấy ngày cuối cùng của năm
		LocalDate lastDayOfYear = LocalDate.ofYearDay(LocalDate.now().getYear(), LocalDate.MAX.getDayOfYear());

		// Chuyển đổi LocalDate thành java.sql.Date nếu cần
		Date firstDayOfYearSql = Date.valueOf(firstDayOfYear);
		Date lastDayOfYearSql = Date.valueOf(lastDayOfYear);

		List<Order> orders = service.findByDateRange("SUCCESS", firstDayOfYearSql, lastDayOfYearSql);
		int total = 0;
		for (Order order : orders) {
			total += (Integer) (odtService.totalAmount(order.getOrderId()) + 30000);
		}
		return total;

	}

	@RequestMapping("/orderPieChartInCurrentYear")
	private String orderPieChart() {

		LocalDate firstDayOfYear = LocalDate.ofYearDay(LocalDate.now().getYear(), 1);

		// Lấy ngày cuối cùng của năm
		LocalDate lastDayOfYear = LocalDate.ofYearDay(LocalDate.now().getYear(), LocalDate.MAX.getDayOfYear());

		// Chuyển đổi LocalDate thành java.sql.Date nếu cần
		Date firstDayOfYearSql = Date.valueOf(firstDayOfYear);
		Date lastDayOfYearSql = Date.valueOf(lastDayOfYear);

		int countCOD = service.countOSAndPMByDateRange("COD", "SUCCESS", firstDayOfYearSql, lastDayOfYearSql);
		int countOnl = service.countOSAndPMByDateRange("Online Banking", "SUCCESS", firstDayOfYearSql,
				lastDayOfYearSql);

		JSONObject data = new JSONObject();
		data.put("COD", countCOD);
		data.put("OnlineBanking", countOnl);
		return data.toString();
	}

	@RequestMapping("/CODOrderBarChart")
	private List<JsonNode> CODOrderBarChart() {
		//// THIEU 14:33 13/12/2023
		List<JsonNode> dataResp = new ArrayList<>();
		for (int i = 1; i <= LocalDate.now().getMonthValue(); i++) {
			// lấy ngày đầu tiên trong tháng
			LocalDate firstDayOfMonth = LocalDate.of(LocalDate.now().getYear(), i, 1);
			// lấy ngày cuối cùng trong tháng
			LocalDate lastDayOfMonth = LocalDate.of(LocalDate.now().getYear(), i, 1).plusMonths(1).minusDays(1);
			// convert về kiểu sql.Date
			Date firstDayOfMonthSql = Date.valueOf(firstDayOfMonth);
			Date lastDayOfMonthSql = Date.valueOf(lastDayOfMonth);

			// đếm tất cả đơn hàng COD có orderStatus là CANCEL
			int countCancel = service.countOSAndPMByDateRange("COD", "CANCEL", firstDayOfMonthSql, lastDayOfMonthSql);
			// đếm tất cả đơn hàng COD có orderStatus là SUCCESS
			int countSuccess = service.countOSAndPMByDateRange("COD", "SUCCESS", firstDayOfMonthSql, lastDayOfMonthSql);
			// đếm tất cả đơn hàng COD có orderStatus là NULL
			int countNull = service.countOSNullAndPMByDateRange("COD", firstDayOfMonthSql, lastDayOfMonthSql);

			JSONObject data = new JSONObject();
			data.put("month", i);
			data.put("cancel", countCancel);
			data.put("success", countSuccess);
			data.put("null", countNull);
			ObjectMapper objectMapper = new ObjectMapper();

			JsonNode jsonNode = null;
			try {
				jsonNode = objectMapper.readTree(data.toString());
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dataResp.add(jsonNode);
		}
		return dataResp;

	}

	@RequestMapping("/OnlineBankingOrderBarChart")
	private List<JsonNode> OnlineBankingOrderBarChart() {
		List<JsonNode> dataResp = new ArrayList<>();
		for (int i = 1; i <= LocalDate.now().getMonthValue(); i++) {
			// lấy ngày đầu tiên trong tháng
			LocalDate firstDayOfMonth = LocalDate.of(LocalDate.now().getYear(), i, 1);
			// lấy ngày cuối cùng trong tháng
			LocalDate lastDayOfMonth = LocalDate.of(LocalDate.now().getYear(), i, 1).plusMonths(1).minusDays(1);
			// convert về kiểu sql.Date
			Date firstDayOfMonthSql = Date.valueOf(firstDayOfMonth);
			Date lastDayOfMonthSql = Date.valueOf(lastDayOfMonth);

			// đếm tất cả đơn hàng COD có orderStatus là CANCEL
			int countCancel = service.countOSAndPMByDateRange("Online Banking", "CANCEL", firstDayOfMonthSql,
					lastDayOfMonthSql);
			// đếm tất cả đơn hàng COD có orderStatus là SUCCESS
			int countSuccess = service.countOSAndPMByDateRange("Online Banking", "SUCCESS", firstDayOfMonthSql,
					lastDayOfMonthSql);
			// đếm tất cả đơn hàng COD có orderStatus là NULL
			int countNull = service.countOSNullAndPMByDateRange("Online Banking", firstDayOfMonthSql,
					lastDayOfMonthSql);

			JSONObject data = new JSONObject();
			data.put("month", i);
			data.put("cancel", countCancel);
			data.put("success", countSuccess);
			data.put("null", countNull);
			ObjectMapper objectMapper = new ObjectMapper();

			JsonNode jsonNode = null;
			try {
				jsonNode = objectMapper.readTree(data.toString());
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dataResp.add(jsonNode);
		}
		return dataResp;

	}

}
