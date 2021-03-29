package com.example.testegft.service;

import java.util.List;

import com.example.testegft.dto.LojaDTO;
import com.example.testegft.model.entity.Produto;

import javassist.NotFoundException;

public interface IProdutoService {

	void gravaProdutos(List<Produto> produtos);

	List<LojaDTO> getProduto(String nomeProduto, Integer quantidadeLojas) throws NotFoundException;
}
