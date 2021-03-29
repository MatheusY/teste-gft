package com.example.testegft.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

import lombok.Getter;

import com.example.testegft.model.entity.Produto;

@Getter
public class LojaDTO {

	private Integer quantidade = 0;

	private BigDecimal financeiro = BigDecimal.ZERO;

	private BigDecimal precoMedio = BigDecimal.ZERO;

	public void adicionaProduto(Produto produto, Integer quantidade) {
		this.quantidade += quantidade;
		BigDecimal total = produto.getPreco().multiply(new BigDecimal(quantidade));
		financeiro = financeiro.add(total);
		precoMedio = financeiro.divide(new BigDecimal(this.quantidade), 4, RoundingMode.HALF_UP);
	}

	public static int comparaPorQuantidadeEFinanceiro(LojaDTO loja, LojaDTO outraLoja) {
		if (loja.getQuantidade().equals(outraLoja.getQuantidade())) {
			return loja.getFinanceiro().compareTo(outraLoja.getFinanceiro());
		}
		return loja.getQuantidade().compareTo(outraLoja.getQuantidade());
	}

}
