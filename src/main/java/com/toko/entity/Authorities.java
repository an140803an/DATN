package com.toko.entity;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import lombok.Data;

@SuppressWarnings("serial")
@Data
@Entity
@Table(name = "authorities", uniqueConstraints = { @UniqueConstraint(columnNames = { "account_id", "role_id" }) })
public class Authorities implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JoinColumn(name = "authorities_id")
	private Integer authorities_id;
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "account_id")
	private Accounts account;
	@ManyToOne
	@JoinColumn(name = "role_id")
	private Role role;

	public Integer getAuthorities_id() {
		return authorities_id;
	}

	public void setAuthorities_id(Integer authorities_id) {
		this.authorities_id = authorities_id;
	}

	public Accounts getAccount() {
		return account;
	}

	public void setAccount(Accounts account) {
		this.account = account;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	// Getter and Setter methods

}
