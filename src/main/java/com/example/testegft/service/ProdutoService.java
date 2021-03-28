package com.example.testegft.service;

import java.util.List;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.testegft.model.entity.Produto;
import com.example.testegft.repository.ProdutoRepository;

@Service
@Slf4j
public class ProdutoService implements IProdutoService {

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

}
