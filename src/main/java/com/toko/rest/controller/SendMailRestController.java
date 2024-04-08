package com.toko.rest.controller;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.toko.entity.Mailinfo;
import com.toko.mail.MailServiceImpl;



@CrossOrigin("*")
@RestController
@RequestMapping("/rest/sendmail")
public class SendMailRestController {
	
	@Autowired
	MailServiceImpl service ;
	
	@PostMapping("")
	public boolean sendMailSuccess(@RequestBody JsonNode resData){
		try {

			Mailinfo mailinfo = new Mailinfo(resData.get("toEmail").asText(),resData.get("subjectTitle").asText(),resData.get("body").asText());
			service.send(mailinfo);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
}
