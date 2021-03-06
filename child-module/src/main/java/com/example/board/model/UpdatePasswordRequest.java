package com.example.board.model;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Data
public class UpdatePasswordRequest {
	@Valid
	@NotBlank
	private String password;

}