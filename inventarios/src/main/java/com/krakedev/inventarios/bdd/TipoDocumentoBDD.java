package com.krakedev.inventarios.bdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.krakedev.inventarios.entidades.TipoDocumento;
import com.krakedev.inventarios.excepciones.KrakeDevException;
import com.krakedev.inventarios.utils.ConexionBDD;

public class TipoDocumentoBDD {
	public ArrayList<TipoDocumento> recuperar() throws KrakeDevException {
		ArrayList<TipoDocumento> TD = new ArrayList<TipoDocumento>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		TipoDocumento documentos = null;
		try {
			con = ConexionBDD.obtenerConexion();
			ps = con.prepareStatement("select codigo,descripcion "
					+ "from tipo_documento");
			rs = ps.executeQuery();

			while (rs.next()) {
				String codigo = rs.getString("codigo");
				String descripcion = rs.getString("descripcion");
				
				documentos = new TipoDocumento(codigo,descripcion);
				TD.add(documentos);
			}
		} catch (KrakeDevException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al consultar. Detalle" + e.getMessage());
		}
		return TD;
	}
}
