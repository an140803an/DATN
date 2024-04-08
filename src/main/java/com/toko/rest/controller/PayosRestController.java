package com.toko.rest.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toko.payOs.type.CreatePaymentLinkRequestBody;
import com.toko.service.PayosService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/payos")
public class PayosRestController {
	@Value("${PAYOS_CLIENT_ID}")
	private String clientId;

	@Value("${PAYOS_API_KEY}")
	private String apiKey;

	@Value("${PAYOS_CHECKSUM_KEY}")
	private String checksumKey;
	
	@Autowired
	PayosService payosService;

	@PostMapping(path = "/createPaymentOnlineLink")
	public JsonNode createPaymentLink(@RequestBody CreatePaymentLinkRequestBody RequestBody)
			throws JsonMappingException, JsonProcessingException {
		System.out.println("RequestBody: " + RequestBody.getOrderCode());
		try {

			HttpHeaders headers = new HttpHeaders();
			headers.set("x-api-key", apiKey);
			headers.set("x-client-id", clientId);
			headers.setContentType(MediaType.APPLICATION_JSON);

			String transaction = "{'amount':" + RequestBody.getAmount() + ",'cancelUrl': '" + RequestBody.getCancelUrl()
					+ "','description':'" + RequestBody.getDescription() + "','orderCode':" + RequestBody.getOrderCode()
					+ ",'returnUrl':'" + RequestBody.getReturnUrl() + "'}";
			System.out.println("transaction: " + transaction);
			try {
				String transactionSignature = payosService.createSignature(transaction);
				RequestBody.setSignature(transactionSignature);
			} catch (Exception e) {
				ObjectMapper objectMapper = new ObjectMapper();

				String json = "{\"error\": 500, \"message\":\"Lỗi tạo signature\"}";

				JsonNode result = objectMapper.readTree(json);

				return result;
			}

			System.out.println("RequestBody has Signature: " + RequestBody);
			HttpEntity<CreatePaymentLinkRequestBody> httpEntity = new HttpEntity<>(RequestBody, headers);

			// Gửi yêu cầu POST với request body
			ResponseEntity<JsonNode> responseEntity = new RestTemplate().exchange(
					"https://api-merchant.payos.vn/v2/payment-requests", HttpMethod.POST, httpEntity, JsonNode.class);
			JsonNode result = responseEntity.getBody();
			return result;

		} catch (Exception e) {
			ObjectMapper objectMapper = new ObjectMapper();

			String json = "{\"error\": 500, \"message\":\"Không tạo được link thanh toán\"}";

			JsonNode result = objectMapper.readTree(json);

			return result;

		}
	}

	@GetMapping(path = "/getOnlineBankingOrder/{id}")
	public JsonNode getOneOrder(@PathVariable("id") Integer id) {

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("x-api-key", apiKey);
		headers.set("x-client-id", clientId);
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(
				"https://api-merchant.payos.vn/v2/payment-requests/" + id, HttpMethod.GET, entity, JsonNode.class

		);

		JsonNode result = responseEntity.getBody();

		return result;
	}

	

}
