package com.example.testegft.model.enumerate;

import java.util.Objects;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class TipoProdutoConverter implements AttributeConverter<TipoProdutoEnum, String> {

	@Override
	public String convertToDatabaseColumn(TipoProdutoEnum tipoProduto) {
		return Objects.nonNull(tipoProduto) ? tipoProduto.getId() : null;
	}

	@Override
	public TipoProdutoEnum convertToEntityAttribute(String id) {
		return TipoProdutoEnum.valueOfId(id);
	}

}
