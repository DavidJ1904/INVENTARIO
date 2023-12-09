package com.krakedev.inventarios.entidades;

public class EstadoPedido {
	private String codigo;
	private String descripcioin;

	public EstadoPedido() {
		super();
	}

	public EstadoPedido(String codigo, String descripcioin) {
		super();
		this.codigo = codigo;
		this.descripcioin = descripcioin;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcioin() {
		return descripcioin;
	}

	public void setDescripcioin(String descripcioin) {
		this.descripcioin = descripcioin;
	}

	@Override
	public String toString() {
		return "EstadoPedido [codigo=" + codigo + ", descripcioin=" + descripcioin + "]";
	}

}