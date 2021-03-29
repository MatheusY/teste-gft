package com.example.testegft.exception;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Erro {

	private LocalDateTime timestamp = LocalDateTime.now();

	private String message;

}
