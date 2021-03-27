package com.example.testegft.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProdutoDTO {

	private String product;

	private Integer quantity;

	private String price;

	private String type;

	private String industry;

	private String origin;

}
