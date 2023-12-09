package com.krakedev.inventarios.bdd;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import com.krakedev.inventarios.entidades.Pedido;
import com.krakedev.inventarios.entidades.DetallePedido;
import com.krakedev.inventarios.excepciones.KrakeDevException;
import com.krakedev.inventarios.utils.ConexionBDD;

public class PedidosBDD {
	public void insertar(Pedido pedido) throws KrakeDevException {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement psDet = null;
		Date fechaActual = new Date(); 
		java.sql.Date fechaSql = new java.sql.Date(fechaActual.getTime()); 
		ResultSet rsClave = null;
		int codigoCabecera = 0;
		try {
			con = ConexionBDD.obtenerConexion();
			ps = con.prepareStatement("insert into cabecera_pedidos(proveedor,fecha,estado)" + "values(?,?,?)",
					Statement.RETURN_GENERATED_KEYS); 
			ps.setString(1, pedido.getProveedor().getIdentificador());
			ps.setDate(2, fechaSql);
			ps.setString(3, "S");
			ps.executeUpdate();
			// RECUPERAR LA CLAVE GENERADA
			rsClave = ps.getGeneratedKeys();
			if (rsClave.next()) {
				codigoCabecera = rsClave.getInt(1);
			}
			ArrayList<DetallePedido> detallesPedido = pedido.getDetalles(); 
			DetallePedido det;
			for (int i = 0; i < detallesPedido.size(); i++) {
				det = detallesPedido.get(i);
				psDet = con.prepareStatement(
						"insert into detalle_pedido(cabecera_pedido,producto,cantidad_solicitada,subtotal,cantidad_recibida) values(?,?,?,?,?)");
				psDet.setInt(1, codigoCabecera);
				psDet.setInt(2, det.getProducto().getCodigo());
				psDet.setInt(3, det.getCantidadSolicitada());
				BigDecimal pv = det.getProducto().getPrecioVenta();
				BigDecimal cantidad = new BigDecimal(det.getCantidadSolicitada());
				BigDecimal subtotal = pv.multiply(cantidad);
				psDet.setBigDecimal(4, subtotal);
				psDet.setInt(5, 0);
				psDet.executeUpdate();
			}

		} catch (SQLException e) { 
			e.printStackTrace();
			throw new KrakeDevException("ERROR AL INSERTAR AL PEDIDO. DETALLE:" + e.getMessage());
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