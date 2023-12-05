package com.krakedev.inventarios.bdd;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.krakedev.inventarios.entidades.Categoria;
import com.krakedev.inventarios.entidades.Producto;
import com.krakedev.inventarios.entidades.UnidadDeMedida;
import com.krakedev.inventarios.excepciones.KrakeDevException;
import com.krakedev.inventarios.utils.ConexionBDD;

public class ProductosBDD {
	public ArrayList<Producto> buscar(String subcadena) throws KrakeDevException {
		ArrayList<Producto> productos = new ArrayList<Producto>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Producto producto = null;
		try {
			con = ConexionBDD.obtenerConexion();
			ps = con.prepareStatement("select prod.codigo_producto, prod.nombre as nombre_producto, "
					+ "um.nombre as nombre_udm, um.descripcion as descripcion_udm, "
					+ "cast (prod.precio as decimal(6,2)), prod.iva, "
					+ "cast (prod.coste as decimal (5,4)), "
					+ "prod.categoria, cat.nombre as nombre_categoria, "
					+ "stock "
					+ "from producto prod,udm um, categorias cat "
					+ "where prod.unidades_medidas= um.codigo_udm "
					+ "and prod.categoria = cat.codigo_cat "
					+ "and upper(prod.nombre) like ?");
			ps.setString(1, "%"+subcadena.toUpperCase()+"%");
			rs = ps.executeQuery();

			while (rs.next()) {
				int codigoProducto = rs.getInt("codigo_producto");
				String nombreProducto = rs.getString("nombre_producto");
				String nombreUnidadMedida = rs.getString("nombre_udm");
				String descripcionUnidadMedida = rs.getString("descripcion_udm");
				BigDecimal precioVenta = rs.getBigDecimal("precio");
				boolean tieneIva = rs.getBoolean("iva");
				BigDecimal coste = rs.getBigDecimal("coste");
				int codigoCategoria = rs.getInt("categoria");
				String nombreCategoria = rs.getString("nombre_categoria");
				int stock = rs.getInt("stock");
				
				UnidadDeMedida udm = new UnidadDeMedida();
				udm.setNombre(nombreUnidadMedida);
				udm.setDescripcion(descripcionUnidadMedida);
				
				Categoria cat = new Categoria();
				cat.setNombre(nombreCategoria);
				cat.setCodigo(codigoCategoria);
				
				producto=new Producto();
				producto.setCodigo(codigoProducto);
				producto.setNombre(nombreProducto);
				producto.setUnidadMedida(udm);
				producto.setPrecioVenta(precioVenta);
				producto.setTieneIva(tieneIva);
				producto.setCoste(coste);
				producto.setCategoria(cat);
				producto.setStock(stock);
				
				productos.add(producto);
			
			}
		} catch (KrakeDevException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al consultar. Detalle" + e.getMessage());
		}
		return productos;
	}
}