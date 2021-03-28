package com.example.testegft.model.enumerate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import lombok.Getter;

@Getter
public enum TipoProdutoEnum {

	TIPO_S("S"), //
	TIPO_M("M"), //
	TIPO_L("L"), //
	TIPO_X("X"), //
	TIPO_XS("XS"), //
	TIPO_XL("XL"), //
	TIPO_2XL("2XL"), //
	TIPO_3XL("3XL");

	private static final Map<String, TipoProdutoEnum> LOOKUP = new HashMap<>();

	static {
		for (TipoProdutoEnum e : TipoProdutoEnum.values()) {
			LOOKUP.put(e.getId(), e);
		}
	}

	private final String id;

	private TipoProdutoEnum(String id) {
		this.id = id;
	}

	public static TipoProdutoEnum valueOfId(String id) {
		return Objects.nonNull(id) ? LOOKUP.get(id) : null;
	}

}
