package com.example.testegft.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
public class ProdutoDTO {

	@JsonProperty("product")
	private String nome;

	@JsonProperty("quantity")
	private Integer quantidade;

	@JsonProperty("price")
	private String preco;

	@JsonProperty("type")
	private String tipo;

	@JsonProperty("industry")
	private String industria;

	@JsonProperty("origin")
	private String origem;

	public BigDecimal getPreco() {
		return new BigDecimal(preco.replaceAll("\\$", ""));
	}

}
