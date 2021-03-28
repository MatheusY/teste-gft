package com.example.testegft.model.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;

import com.example.testegft.model.enumerate.TipoProdutoEnum;

@Entity
@Table(name = "TB_PRODUTO", //
		uniqueConstraints = { //
				@UniqueConstraint(columnNames = { "NOME", "QUANTIDADE", "TIPO", "PRECO", "INDUSTRIA", "ORIGEM", "DADO_ORIGEM" }) })
@Getter
@Setter
public class Produto {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "produto_sequence")
	@SequenceGenerator(name = "produto_sequence", sequenceName = "prod_seq")
	private Long id;

	@Column(name = "NOME")
	private String nome;

	@Column(name = "QUANTIDADE")
	private Integer quantidade;

	@Column(name = "PRECO")
	private BigDecimal preco;

	@Column(name = "TIPO")
	private TipoProdutoEnum tipo;

	@Column(name = "INDUSTRIA")
	private String industria;

	@Column(name = "ORIGEM")
	private String origem;

	@Column(name = "DADO_ORIGEM")
	private String dadoOrigem;
}
