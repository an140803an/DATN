package com.toko.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Accounts", uniqueConstraints = { @UniqueConstraint(columnNames = { "email" }, name = "UK_email"),
		@UniqueConstraint(columnNames = { "phone" }, name = "UK_phone") })
public class Accounts {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "account_id")
	private int account_id;

	
	@Column(name = "name")
	private String name;

	
	@Column(name = "email")
	private String email;

	
	@Column(name = "phone")
	private String phone;

	@Column(name = "img")
	private String img;

	@NotBlank(message = "Mật khẩu không được trống")
	@Size(min = 6, message = "Mật khẩu phải chứa ít nhất 6 ký tự")
	@Column(name = "password")
	private String password;

	@Column(name = "gender")
	private String gender;

	@Column(name = "address")
	private String address;

	@Column(name = "create_date")
	private Date createDate;

	@OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
	List<Authorities> authorities;

	public int getAccount_id() {
		return account_id;
	}

	public void setAccount_id(int account_id) {
		this.account_id = account_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public List<Authorities> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<Authorities> authorities) {
		this.authorities = authorities;
	}

	// Constructors, getters, and setters

	// You can generate constructors and getters/setters using your IDE or manually
}