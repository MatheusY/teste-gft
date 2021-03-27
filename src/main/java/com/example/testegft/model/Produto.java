package com.example.testegft.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "TB_PRODUTO")
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
	private String tipo;

	@Column(name = "INDUSTRIA")
	private String industria;

	@Column(name = "ORIGEM")
	private String origem;

}
