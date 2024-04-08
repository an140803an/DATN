package com.toko.entity;

import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Role")
public class Role {
	@Id
	@Column(name = "role_id")
	private String role_id;
	@Column(name = "name")
	private String name;
	@JsonIgnore
	@OneToMany(mappedBy = "role")
	List<Authorities> authorities;

	public String getId() {
		return role_id;
	}

	public void setId(String id) {
		this.role_id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Authorities> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<Authorities> authorities) {
		this.authorities = authorities;
	}

	// Getter and Setter methods
}