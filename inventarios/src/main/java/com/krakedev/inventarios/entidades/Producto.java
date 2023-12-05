package com.krakedev.inventarios.entidades;

import java.math.BigDecimal;

public class Producto {
	private int codigo;
	private String nombre;
	private UnidadDeMedida unidadMedida;
	private BigDecimal precioVenta;
	private boolean tieneIva;
	private BigDecimal coste;
	private Categoria categoria;
	private int stock;
	
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public UnidadDeMedida getUnidadMedida() {
		return unidadMedida;
	}
	public void setUnidadMedida(UnidadDeMedida unidadMedida) {
		this.unidadMedida = unidadMedida;
	}
	public BigDecimal getPrecioVenta() {
		return precioVenta;
	}
	public void setPrecioVenta(BigDecimal precioVenta) {
		this.precioVenta = precioVenta;
	}
	public boolean isTieneIva() {
		return tieneIva;
	}
	public void setTieneIva(boolean tieneIva) {
		this.tieneIva = tieneIva;
	}
	public BigDecimal getCoste() {
		return coste;
	}
	public void setCoste(BigDecimal coste) {
		this.coste = coste;
	}
	public Categoria getCategoria() {
		return categoria;
	}
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	
	
}