package com.example.testegft;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.testegft.dto.LojaDTO;
import com.example.testegft.service.IProdutoService;

import javassist.NotFoundException;

@RestController
@RequestMapping("/produto")
public class ProdutoController {

	@Autowired
	private IProdutoService produtoService;

	@GetMapping
	public String teste() {
		return "Ola";
	}

	@GetMapping("/{nomeProduto}")
	@ResponseStatus(HttpStatus.OK)
	public List<LojaDTO> getProduto(@PathVariable("nomeProduto") String nomeProduto, Integer quantidadeLojas) throws NotFoundException {
		return produtoService.getProduto(nomeProduto, quantidadeLojas);
	}
}
