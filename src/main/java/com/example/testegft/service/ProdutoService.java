package com.example.testegft.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.testegft.dto.LojaDTO;
import com.example.testegft.model.entity.Produto;
import com.example.testegft.repository.ProdutoRepository;

import javassist.NotFoundException;

@Service
@Slf4j
public class ProdutoService implements IProdutoService {

	private static final String MSG_PRODUTO_NAO_ENCONTRADO = "Produto não encontrado!";
	private static final String MSG_QTD_LOJA_INVALIDO = "Valor de quantidade de lojas inválido";

	@Autowired
	private ProdutoRepository produtoRepository;

	@Override
	@Async
	public void gravaProdutos(List<Produto> produtos) {
		if (Objects.nonNull(produtos)) {
			produtos.forEach(produto -> {
				try {
					produtoRepository.saveAndFlush(produto);
				} catch (DataIntegrityViolationException e) {
					log.error("Dado duplicado");
				}
			});
		}
	}

	@Override
	public List<LojaDTO> getProduto(String nomeProduto, Integer quantidadeLojas) throws NotFoundException {
		if (Objects.isNull(quantidadeLojas) || quantidadeLojas <= 0) {
			throw new IllegalArgumentException(MSG_QTD_LOJA_INVALIDO);
		}

		List<Produto> produtos = produtoRepository.findByNome(nomeProduto);
		if (produtos.isEmpty()) {
			throw new NotFoundException(MSG_PRODUTO_NAO_ENCONTRADO);
		}

		return calculaParaCadaEstabelecimento(quantidadeLojas, produtos);

	}

	private List<LojaDTO> calculaParaCadaEstabelecimento(Integer quantidadeLojas, List<Produto> produtos) {
		List<LojaDTO> lojas = Stream.generate(LojaDTO::new).limit(quantidadeLojas).collect(Collectors.toList());
		produtos.forEach(produto -> {
			int resto = produto.getQuantidade() % quantidadeLojas;
			int quantidade = produto.getQuantidade() / quantidadeLojas;
			if (resto == 0) {
				lojas.forEach(loja -> loja.adicionaProduto(produto, quantidade));
			} else {
				lojas.sort(LojaDTO::comparaPorQuantidadeEFinanceiro);
				lojas.subList(0, resto).forEach(loja -> loja.adicionaProduto(produto, quantidade + 1));
				lojas.subList(resto, lojas.size()).forEach(loja -> loja.adicionaProduto(produto, quantidade));
			}
		});
		return lojas;
	}

}
