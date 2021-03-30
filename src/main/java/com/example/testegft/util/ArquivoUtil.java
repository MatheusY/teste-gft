package com.example.testegft.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.testegft.dto.ListaProdutosDTO;
import com.example.testegft.dto.ProdutoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Slf4j
public class ArquivoUtil {

	@Autowired
	private ObjectMapper mapper;

	private static final String EXTENSION_JSON = ".json";

	@Value("${data.path}")
	private String dataPath;

	public List<String> buscaArquivosMassa() {
		File pasta = new File(dataPath);
		return Arrays.stream(pasta.listFiles()) //
				.map(File::getName) //
				.filter(arquivo -> Objects.nonNull(arquivo) && arquivo.endsWith(EXTENSION_JSON)) //
				.collect(Collectors.toList());
	}

	public List<ProdutoDTO> getDadosArquivos(List<String> nomesArquivos) {
		List<ProdutoDTO> produtos = new ArrayList<>();

		nomesArquivos.forEach(nomeArquivo -> {
			String nomeArquivoSemExtensao = nomeArquivo.replaceFirst(EXTENSION_JSON, "");
			try {
				ListaProdutosDTO produtosDTO = mapper.readValue(new File(dataPath + nomeArquivo), ListaProdutosDTO.class);

				produtosDTO.getData().forEach(produto -> {
					produto.setDadoOrigem(nomeArquivoSemExtensao);
					produtos.add(produto);
				});
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		});
		return produtos;
	}
}
