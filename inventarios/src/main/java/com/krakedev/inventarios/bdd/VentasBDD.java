package com.krakedev.inventarios.bdd;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import com.krakedev.inventarios.entidades.DetalleVentas;
import com.krakedev.inventarios.entidades.Ventas;
import com.krakedev.inventarios.excepciones.KrakeDevException;
import com.krakedev.inventarios.utils.ConexionBDD;

public class VentasBDD {
	public void insertarActualizarVenta(Ventas venta) throws KrakeDevException {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement psDet = null;
		PreparedStatement psStock = null;
		ResultSet rsClave = null;
		int codigoCabecera = 0;

		Date fechaActual = new Date(); 
		Timestamp fechaHoraActual = new Timestamp(fechaActual.getTime());
		try {
			con = ConexionBDD.obtenerConexion();
			ps = con.prepareStatement("insert into cabecera_ventas(fecha,total_sin_iva,iva,total) values(?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS); 
			BigDecimal cantidad = new BigDecimal(0);
			ps.setTimestamp(1, fechaHoraActual);
			ps.setBigDecimal(2, cantidad);
			ps.setBigDecimal(3, cantidad);
			ps.setBigDecimal(4, cantidad);
			ps.executeUpdate();
			rsClave = ps.getGeneratedKeys();
			if (rsClave.next()) {
				codigoCabecera = rsClave.getInt(1);
			}
			ArrayList<DetalleVentas> detallesVentas = venta.getDetalles(); 
			DetalleVentas det;
			for (int i = 0; i < detallesVentas.size(); i++) {
				det = detallesVentas.get(i);
				psDet = con.prepareStatement(
						"insert into detalle_ventas(cabecera_ventas,producto,precio_venta,cantidad,subtotal,subtotal_iva) values(?,?,?,?,?,?)");
				psDet.setInt(1, codigoCabecera);
				psDet.setInt(2, det.getProducto().getCodigo());
				psDet.setInt(3, det.getCantidad());
				BigDecimal pv = det.getProducto().getPrecioVenta();
				psDet.setBigDecimal(4, pv);
				Boolean IVABoolean = det.getProducto().isTieneIva();
				BigDecimal cantidad2 = new BigDecimal(det.getCantidad());
				BigDecimal porcentaje = new BigDecimal(1.12);
				BigDecimal subtotal = pv.multiply(cantidad2);
				BigDecimal iva = subtotal.multiply(porcentaje);
				psDet.setBigDecimal(5, subtotal);
				if (IVABoolean) {
					psDet.setBigDecimal(6, iva);

				} else {
					psDet.setBigDecimal(6, subtotal);
				}
				psDet.executeUpdate();
			
				ps = con.prepareStatement(
						"update cabecera_ventas set total_sin_iva = ?, iva=?, total=? where codigo_cv=?");
				BigDecimal sumarIva = iva.add(iva);
				BigDecimal sumarNoIva = subtotal.add(subtotal);
				BigDecimal total = sumarIva.add(sumarNoIva);
				ps.setBigDecimal(1, sumarNoIva);
				ps.setBigDecimal(2, sumarIva);
				ps.setBigDecimal(3, total);
				ps.setInt(4, codigoCabecera);
				ps.executeUpdate();
			
				psStock = con.prepareStatement( "insert into historial_stock(fecha,referencia,producto,cantidad) values(?,?,?,?)");
				psStock.setTimestamp(1, fechaHoraActual);
				psStock.setString(2, "VENTA " + codigoCabecera);
				psStock.setInt(3, det.getProducto().getCodigo());
				psStock.setInt(4, (-1)*det.getCantidad());
				psStock.executeUpdate();
			}
		} catch (SQLException e) { 
			e.printStackTrace();
			throw new KrakeDevException("ERROR AL INSERTAR LA VENTA. DETALLE:" + e.getMessage());
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

