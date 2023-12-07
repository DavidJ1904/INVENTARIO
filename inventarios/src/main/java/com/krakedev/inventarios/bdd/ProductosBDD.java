package com.krakedev.inventarios.bdd;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.krakedev.inventarios.entidades.Categoria;
import com.krakedev.inventarios.entidades.Producto;
import com.krakedev.inventarios.entidades.UnidadMedida;
import com.krakedev.inventarios.excepciones.KrakeDevException;
import com.krakedev.inventarios.utils.ConexionBDD;

public class ProductosBDD {
	public ArrayList<Producto> buscar(String subcadena) throws KrakeDevException {
		ArrayList<Producto> producto = new ArrayList<Producto>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Producto prod = null;
		try {
			con = ConexionBDD.obtenerConexion();
			ps = con.prepareStatement("select pro.codigo_producto, pro.nombre as nombre_producto, udm.nombre as nombre_categoria,udm.descripcion as descripcion_udm, "
					+ "cast(pro.precio as decimal (5,2) ),pro.iva,cast(pro.coste as decimal (5,2)), pro.categoria,ct.nombre as nombre_categoria,pro.stock "
					+ "from producto pro , udm udm, categorias ct "
					+ "where (pro.unidades_medidas = udm.nombre) "
					+ "and (pro.categoria = ct.codigo_cat)"
					+ "and upper (pro.nombre) like ?");
			ps.setString(1, "%" + subcadena.toUpperCase() + "%");
			rs = ps.executeQuery();
			while (rs.next()) {
				int codigoProducto = rs.getInt("codigo_producto");
				String nombreProducto = rs.getString("nombre_producto");
				String udmNombreCategoria= rs.getString("nombre_categoria");
				String udDescripcion = rs.getString("descripcion_udm");
				BigDecimal precio = rs.getBigDecimal("precio");
				Boolean iva = rs.getBoolean("iva");
				BigDecimal coste = rs.getBigDecimal("coste");
				int categoria = rs.getInt("categoria");
				String nombreCategoria = rs.getString("nombre_categoria");
				int stock = rs.getInt("stock");
				
				//TABLA UNIDADES DE MEDIDAD 
				UnidadMedida UD = new UnidadMedida();
				UD.setNombre(udmNombreCategoria);
				UD.setDescripcion(udDescripcion);
				//TABLA CATEGORIAS
				Categoria cat = new Categoria();
				cat.setCodigo(categoria);
				cat.setNombre(nombreCategoria);
				//TABLA PRODUCTOS
				prod = new Producto();
				prod.setCodigo(codigoProducto);
				prod.setNombre(nombreProducto);
				prod.setUnidadMedida(UD);
				prod.setPrecioVenta(precio);
				prod.setTieneIva(iva);
				prod.setCoste(coste);
				prod.setCategoria(cat);
				prod.setStock(stock);
				producto.add(prod);
			}
		} catch (KrakeDevException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("ERROR AL CONSULTAR. DETALLE:" + e.getMessage());

		}
		return producto;
	}
	
	public void insertar(Producto producto) throws KrakeDevException {
		Connection con = null;
		try {
			con = ConexionBDD.obtenerConexion();
			PreparedStatement ps = con.prepareStatement(
					"insert into producto(codigo_producto,nombre,unidades_medidas,precio,iva,coste,categoria,stock)\r\n"
					+ "values(?,?,?,?,?,?,?,?)");
			ps.setInt(1, producto.getCodigo());
			ps.setString(2, producto.getNombre());
			ps.setString(3, producto.getUnidadMedida().getNombre());
			ps.setBigDecimal(4, producto.getPrecioVenta());
			ps.setBoolean(5, producto.isTieneIva());
			ps.setBigDecimal(6, producto.getCoste());
			ps.setInt(7, producto.getCategoria().getCodigo());
			ps.setInt(8, producto.getStock());
			
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("ERROR AL INSERTAR AL CLIENTE. DETALLE:" + e.getMessage());
		} catch (KrakeDevException e) {
			throw e;
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}