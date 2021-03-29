package com.example.testegft.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import com.example.testegft.dto.LojaDTO;
import com.example.testegft.model.entity.Produto;
import com.example.testegft.model.enumerate.TipoProdutoEnum;
import com.example.testegft.repository.ProdutoRepository;

import javassist.NotFoundException;

public class ProdutoServiceTest {

	private static final String MSG_QTD_LOJA_INVALIDO = "Valor de quantidade de lojas inválido";
	private static final String MSG_PRODUTO_NAO_ENCONTRADO = "Produto não encontrado!";

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

	@Test
	public void testBuscaProdutoComNullParaQuantidadeLoja() throws NotFoundException {
		try {
			produtoService.getProduto("bmm", null);
		} catch (IllegalArgumentException e) {
			assertEquals(MSG_QTD_LOJA_INVALIDO, e.getMessage());
		}
		verifyNoInteractions(produtoRepository);
	}

	@Test
	public void testBuscaProdutoComValorZeroParaQuantidadeLoja() throws NotFoundException {
		try {
			produtoService.getProduto("bmm", 0);
		} catch (IllegalArgumentException e) {
			assertEquals(MSG_QTD_LOJA_INVALIDO, e.getMessage());
		}
		verifyNoInteractions(produtoRepository);
	}

	@Test
	public void testBuscaProdutoQuandoNaoEncontraProduto() {
		when(produtoRepository.findByNome(any())).thenReturn(new ArrayList<>());
		try {
			produtoService.getProduto("bmm", 1);
		} catch (NotFoundException e) {
			assertEquals(MSG_PRODUTO_NAO_ENCONTRADO, e.getMessage());
		}
		verify(produtoRepository).findByNome(any());
	}

	@Test
	public void testBuscaComUmProduto() throws NotFoundException {
		List<Produto> produtos = createProdutos();
		when(produtoRepository.findByNome(any())).thenReturn(produtos);
		List<LojaDTO> lojas = produtoService.getProduto("bmm", 1);
		verify(produtoRepository).findByNome(any());
		LojaDTO loja = lojas.get(0);
		assertEquals(1, lojas.size());
		assertEquals(4, loja.getQuantidade());
		assertTrue(new BigDecimal(80).compareTo(loja.getFinanceiro()) == 0);
		assertTrue(new BigDecimal(20).compareTo(loja.getPrecoMedio()) == 0);
	}

	@Test
	public void testBuscaComMaisDeUmProduto() throws NotFoundException {
		List<Produto> produtos = createProdutos();
		Produto produto = createProduto();
		produto.setQuantidade(8);
		produto.setPreco(new BigDecimal(10));
		produtos.add(produto);
		when(produtoRepository.findByNome(any())).thenReturn(produtos);
		List<LojaDTO> lojas = produtoService.getProduto("bmm", 1);
		verify(produtoRepository).findByNome(any());
		LojaDTO loja = lojas.get(0);
		assertEquals(1, lojas.size());
		assertEquals(12, loja.getQuantidade());
		BigDecimal total = new BigDecimal(80).add(new BigDecimal(80));
		assertTrue(total.compareTo(loja.getFinanceiro()) == 0);
		assertTrue(total.divide(new BigDecimal(12), 4, RoundingMode.HALF_UP).compareTo(loja.getPrecoMedio()) == 0);
	}

	@Test
	public void testBuscaComDoisEstabelecimentos() throws NotFoundException {
		List<Produto> produtos = createProdutos();
		when(produtoRepository.findByNome(any())).thenReturn(produtos);
		List<LojaDTO> lojas = produtoService.getProduto("bmm", 2);
		verify(produtoRepository).findByNome(any());
		assertEquals(2, lojas.size());
		lojas.forEach(loja -> {
			assertEquals(2, loja.getQuantidade());
			assertTrue(new BigDecimal(40).compareTo(loja.getFinanceiro()) == 0);
			assertTrue(new BigDecimal(20).compareTo(loja.getPrecoMedio()) == 0);
		});
	}

	@Test
	public void testBuscaComMaisDeUmProdutoEDoisEstabelecimentos() throws NotFoundException {
		List<Produto> produtos = createProdutos();
		Produto produto = createProduto();
		produto.setQuantidade(8);
		produto.setPreco(new BigDecimal(10));
		produtos.add(produto);
		when(produtoRepository.findByNome(any())).thenReturn(produtos);
		List<LojaDTO> lojas = produtoService.getProduto("bmm", 2);
		verify(produtoRepository).findByNome(any());
		assertEquals(2, lojas.size());
		lojas.forEach(loja -> {
			assertEquals(6, loja.getQuantidade());
			BigDecimal total = new BigDecimal(40).add(new BigDecimal(40));
			assertTrue(total.compareTo(loja.getFinanceiro()) == 0);
			assertTrue(total.divide(new BigDecimal(6), 4, RoundingMode.HALF_UP).compareTo(loja.getPrecoMedio()) == 0);
		});
	}

	@Test
	public void testBuscaComUmProdutoDeQuantidadeImparEDoisEstabelecimentos() throws NotFoundException {
		List<Produto> produtos = createProdutos();
		produtos.get(0).setQuantidade(5);
		when(produtoRepository.findByNome(any())).thenReturn(produtos);
		List<LojaDTO> lojas = produtoService.getProduto("bmm", 2);
		verify(produtoRepository).findByNome(any());
		assertEquals(2, lojas.size());

		LojaDTO loja = lojas.get(0);
		int quantidade = 3;
		assertEquals(quantidade, loja.getQuantidade());
		BigDecimal total = new BigDecimal(60);
		assertTrue(total.compareTo(loja.getFinanceiro()) == 0);
		assertTrue(total.divide(new BigDecimal(quantidade), 4, RoundingMode.HALF_UP).compareTo(loja.getPrecoMedio()) == 0);

		loja = lojas.get(1);
		quantidade = 2;
		assertEquals(quantidade, loja.getQuantidade());
		total = new BigDecimal(40);
		assertTrue(total.compareTo(loja.getFinanceiro()) == 0);
		assertTrue(total.divide(new BigDecimal(quantidade), 4, RoundingMode.HALF_UP).compareTo(loja.getPrecoMedio()) == 0);
	}

	@Test
	public void testBuscaComMaisDeUmProdutoDeQuantidadeImparEDoisEstabelecimentos() throws NotFoundException {
		List<Produto> produtos = createProdutos();
		produtos.get(0).setQuantidade(5);
		Produto produto = createProduto();
		produto.setQuantidade(9);
		produto.setPreco(new BigDecimal(10));
		produtos.add(produto);
		when(produtoRepository.findByNome(any())).thenReturn(produtos);
		List<LojaDTO> lojas = produtoService.getProduto("bmm", 2);
		verify(produtoRepository).findByNome(any());
		assertEquals(2, lojas.size());

		LojaDTO loja = lojas.get(0);
		int quantidade = 7;
		assertEquals(quantidade, loja.getQuantidade());
		BigDecimal total = new BigDecimal(40).add(new BigDecimal(50));
		assertTrue(total.compareTo(loja.getFinanceiro()) == 0);
		assertTrue(total.divide(new BigDecimal(quantidade), 4, RoundingMode.HALF_UP).compareTo(loja.getPrecoMedio()) == 0);

		loja = lojas.get(1);
		quantidade = 7;
		assertEquals(quantidade, loja.getQuantidade());
		total = new BigDecimal(60).add(new BigDecimal(40));
		assertTrue(total.compareTo(loja.getFinanceiro()) == 0);
		assertTrue(total.divide(new BigDecimal(quantidade), 4, RoundingMode.HALF_UP).compareTo(loja.getPrecoMedio()) == 0);
	}

	@Test
	public void testBuscaComUmProdutoETresEstabelecimentos() throws NotFoundException {
		List<Produto> produtos = createProdutos();
		when(produtoRepository.findByNome(any())).thenReturn(produtos);
		List<LojaDTO> lojas = produtoService.getProduto("bmm", 3);
		verify(produtoRepository).findByNome(any());
		assertEquals(3, lojas.size());

		LojaDTO loja = lojas.get(0);
		int quantidade = 2;
		assertEquals(quantidade, loja.getQuantidade());
		BigDecimal total = new BigDecimal(40);
		assertTrue(total.compareTo(loja.getFinanceiro()) == 0);
		assertTrue(total.divide(new BigDecimal(quantidade), 4, RoundingMode.HALF_UP).compareTo(loja.getPrecoMedio()) == 0);

		loja = lojas.get(1);
		quantidade = 1;
		assertEquals(quantidade, loja.getQuantidade());
		total = new BigDecimal(20);
		assertTrue(total.compareTo(loja.getFinanceiro()) == 0);
		assertTrue(total.divide(new BigDecimal(quantidade), 4, RoundingMode.HALF_UP).compareTo(loja.getPrecoMedio()) == 0);

		loja = lojas.get(2);
		assertEquals(quantidade, loja.getQuantidade());
		total = new BigDecimal(20);
		assertTrue(total.compareTo(loja.getFinanceiro()) == 0);
		assertTrue(total.divide(new BigDecimal(quantidade), 4, RoundingMode.HALF_UP).compareTo(loja.getPrecoMedio()) == 0);
	}

	@Test
	public void testBuscaComDoisProdutosETresEstabelecimentos() throws NotFoundException {
		List<Produto> produtos = createProdutos();
		Produto produto = createProduto();
		produto.setQuantidade(5);
		produto.setPreco(new BigDecimal(10));
		produtos.add(produto);
		when(produtoRepository.findByNome(any())).thenReturn(produtos);
		List<LojaDTO> lojas = produtoService.getProduto("bmm", 3);
		verify(produtoRepository).findByNome(any());
		assertEquals(3, lojas.size());

		LojaDTO loja = lojas.get(0);
		int quantidade = 3;
		assertEquals(quantidade, loja.getQuantidade());
		BigDecimal total = new BigDecimal(20).add(new BigDecimal(20));
		assertTrue(total.compareTo(loja.getFinanceiro()) == 0);
		assertTrue(total.divide(new BigDecimal(quantidade), 4, RoundingMode.HALF_UP).compareTo(loja.getPrecoMedio()) == 0);

		loja = lojas.get(1);
		assertEquals(quantidade, loja.getQuantidade());
		total = new BigDecimal(20).add(new BigDecimal(20));
		assertTrue(total.compareTo(loja.getFinanceiro()) == 0);
		assertTrue(total.divide(new BigDecimal(quantidade), 4, RoundingMode.HALF_UP).compareTo(loja.getPrecoMedio()) == 0);

		loja = lojas.get(2);
		assertEquals(quantidade, loja.getQuantidade());
		total = new BigDecimal(40).add(new BigDecimal(10));
		assertTrue(total.compareTo(loja.getFinanceiro()) == 0);
		assertTrue(total.divide(new BigDecimal(quantidade), 4, RoundingMode.HALF_UP).compareTo(loja.getPrecoMedio()) == 0);
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
		produto.setQuantidade(4);
		produto.setTipo(TipoProdutoEnum.TIPO_S);
		return produto;
	}
}
