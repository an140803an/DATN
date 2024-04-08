package com.toko.mail;

import javax.mail.MessagingException;

import com.toko.entity.Mailinfo;

public interface MailerService {
	
	void send(Mailinfo mail) throws MessagingException;
	void send(String to, String subject, String body) throws MessagingException;
	void queue(Mailinfo mail);
	void queue(String to, String subject, String body);
}
