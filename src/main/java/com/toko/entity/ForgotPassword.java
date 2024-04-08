package com.toko.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Forgotpassword")
public class ForgotPassword {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "forgotpassword_id")
	private int forgotpassword_id;
	
	@ManyToOne
	@JoinColumn(name = "account_id")
	private Accounts account;
	
	@Column(name = "verifycode")
	private String verifycode;

	public int getForgotpassword_id() {
		return forgotpassword_id;
	}

	public void setForgotpassword_id(int forgotpassword_id) {
		this.forgotpassword_id = forgotpassword_id;
	}

	public Accounts getAccount() {
		return account;
	}

	public void setAccount(Accounts account) {
		this.account = account;
	}

	public String getVerifycode() {
		return verifycode;
	}

	public void setVerifycode(String verifycode) {
		this.verifycode = verifycode;
	}

	
	
}
