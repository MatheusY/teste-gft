package com.example.testegft.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.testegft.dto.ListaProdutosDTO;
import com.example.testegft.dto.ProdutoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@Slf4j
public class InitConfiguration {

	private static final String EXTENSION_JSON = ".json";
	private static final String DATA_PATH = "src/main/resources/massa/";

	@Autowired
	ObjectMapper mapper;

	@Bean
	public void loadData() {
		List<String> nomesArquivos = buscaArquivosMassa();
		List<ProdutoDTO> lista = getDadosArquivos(nomesArquivos);
	}

	private List<String> buscaArquivosMassa() {
		File pasta = new File(DATA_PATH);
		return Arrays.stream(pasta.listFiles()) //
				.map(File::getName) //
				.filter(arquivo -> Objects.nonNull(arquivo) && arquivo.endsWith(EXTENSION_JSON)) //
				.collect(Collectors.toList());
	}

	private List<ProdutoDTO> getDadosArquivos(List<String> nomesArquivos) {
		List<ProdutoDTO> produtos = new ArrayList<>();

		nomesArquivos.forEach(nomeArquivo -> {
			try {
				ListaProdutosDTO produtosDTO = mapper.readValue(new File(DATA_PATH + nomeArquivo), ListaProdutosDTO.class);
				produtosDTO.getData().forEach(produtos::add);
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		});
		return produtos;
	}
}
