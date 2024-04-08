package com.toko.service;

import java.util.*;
import org.apache.commons.codec.digest.HmacUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

@Service
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class PayosService {

	@Value("${PAYOS_CLIENT_ID}")
	private String clientId;

	@Value("${PAYOS_API_KEY}")
	private String apiKey;

	@Value("${PAYOS_CHECKSUM_KEY}")
	private String checksumKey;
	

	public String createSignature(String transaction) {
		JSONObject jsonObject = new JSONObject(transaction);
		Iterator<String> sortedIt = sortedIterator(jsonObject.keys(), (a, b) -> a.compareTo(b));

		StringBuilder transactionStr = new StringBuilder();
		while (sortedIt.hasNext()) {
			String key = sortedIt.next();
			String value = jsonObject.get(key).toString();
			transactionStr.append(key);
			transactionStr.append('=');
			transactionStr.append(value);
			if (sortedIt.hasNext()) {
				transactionStr.append('&');
			}
		}

		String signature = new HmacUtils("HmacSHA256", checksumKey).hmacHex(transactionStr.toString());
		System.out.println(signature);
		return signature;
	}

	public static Iterator<String> sortedIterator(Iterator<String> it, Comparator<String> comparator) {
		List<String> list = new ArrayList<String>();
		while (it.hasNext()) {
			list.add(it.next());
		}

		Collections.sort(list, comparator);
		return list.iterator();
	}


}
