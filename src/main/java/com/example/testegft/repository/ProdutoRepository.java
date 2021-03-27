package com.example.testegft.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.testegft.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

}
