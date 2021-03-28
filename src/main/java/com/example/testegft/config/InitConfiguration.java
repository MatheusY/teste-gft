package com.example.testegft.config;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.testegft.dto.ProdutoDTO;
import com.example.testegft.model.entity.Produto;
import com.example.testegft.service.IProdutoService;
import com.example.testegft.util.ArquivoUtil;

@Configuration
public class InitConfiguration {

	@Autowired
	private ModelMapper modelMapper;

	@Bean
	public Object loadData(IProdutoService produtoService, ArquivoUtil arquivoUtil) {
		List<String> nomesArquivos = arquivoUtil.buscaArquivosMassa();
		List<ProdutoDTO> lista = arquivoUtil.getDadosArquivos(nomesArquivos);
		produtoService.gravaProdutos(convert(lista));
		return null;
	}

	public List<Produto> convert(List<ProdutoDTO> produtosDTO) {
		List<Produto> produto = new ArrayList<>();
		produtosDTO.forEach(e -> produto.add(modelMapper.map(e, Produto.class)));
		return produto;
	}

}
