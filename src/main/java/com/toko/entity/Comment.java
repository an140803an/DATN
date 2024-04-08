package com.toko.entity;

import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Comments")
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_id")
	private int commentId;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@NotNull(message = "Phải có tài khoản liên quan")
	@ManyToOne
	@JoinColumn(name = "account_id")
	private Accounts account;

	@NotBlank(message = "Nội dung bình luận không được trống")
	@Column(name = "content")
	private String content;


	@Column(name = "comment_date")
	private Date commentDate;

	// Constructors, getters, and setters
	// You can generate constructors and getters/setters using your IDE or manually
}