package com.example.testegft.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import com.example.testegft.model.entity.Produto;
import com.example.testegft.model.enumerate.TipoProdutoEnum;
import com.example.testegft.repository.ProdutoRepository;

public class ProdutoServiceTeste {

	@InjectMocks
	private ProdutoService produtoService;

	@Mock
	private ProdutoRepository produtoRepository;

	@BeforeEach
	void beforeEach() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testGravaComUmProduto() {
		produtoService.gravaProdutos(createProdutos());
		verify(produtoRepository).saveAndFlush(any());
	}

	@Test
	public void testGravaMaisDeUmProduto() {
		List<Produto> produtos = createProdutos();
		produtos.add(createProduto());
		produtos.add(createProduto());
		produtoService.gravaProdutos(produtos);
		verify(produtoRepository, times(produtos.size())).saveAndFlush(any());
	}

	@Test
	public void testGravaSemProduto() {
		produtoService.gravaProdutos(new ArrayList<>());
		verifyNoInteractions(produtoRepository);
	}

	@Test
	public void testGravarEnviandoNull() {
		produtoService.gravaProdutos(null);
		verifyNoInteractions(produtoRepository);
	}

	@Test
	public void testGravarDadoDuplicado() {
		when(produtoRepository.saveAndFlush(createProduto())).thenThrow(DataIntegrityViolationException.class);
		produtoService.gravaProdutos(createProdutos());
		verify(produtoRepository).saveAndFlush(any());
	}

	@Test
	public void testGravarComMaisDeUmDadoDuplicado() {
		List<Produto> produtos = createProdutos();
		produtos.add(createProduto());
		produtos.add(createProduto());
		when(produtoRepository.saveAndFlush(createProduto())).thenThrow(DataIntegrityViolationException.class);
		produtoService.gravaProdutos(produtos);
		verify(produtoRepository, times(produtos.size())).saveAndFlush(any());
	}

	private List<Produto> createProdutos() {
		List<Produto> produtos = new ArrayList<>();
		Produto produto = createProduto();
		produtos.add(produto);
		return produtos;
	}

	private Produto createProduto() {
		Produto produto = new Produto();
		produto.setDadoOrigem("dados");
		produto.setIndustria("industria");
		produto.setNome("bmm");
		produto.setOrigem("AA");
		produto.setPreco(new BigDecimal(20));
		produto.setQuantidade(1);
		produto.setTipo(TipoProdutoEnum.TIPO_S);
		return produto;
	}
}
