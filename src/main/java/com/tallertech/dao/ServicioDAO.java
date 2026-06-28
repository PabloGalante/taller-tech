package com.tallertech.dao;

import com.tallertech.db.DBConnection;
import com.tallertech.model.Servicio;
import com.tallertech.model.ServicioMantenimiento;
import com.tallertech.model.ServicioReparacion;

import java.sql.*;
import java.util.ArrayList;

/**
 * DAO para la jerarquía Servicio.
 * Demuestra cómo persistir una jerarquía de clases (herencia)
 * en una tabla única de MySQL usando el campo 'tipo' como discriminador.
 */
public class ServicioDAO implements IDao<Servicio> {

    @Override
    public ArrayList<Servicio> listarTodos() throws SQLException {
        ArrayList<Servicio> lista = new ArrayList<>();
        String sql = "SELECT * FROM servicio ORDER BY id";
        try (Statement st = DBConnection.get().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapRow(rs));
        }
        return lista;
    }

    @Override
    public Servicio buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM servicio WHERE id = ?";
        try (PreparedStatement ps = DBConnection.get().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    @Override
    public void insertar(Servicio s) throws SQLException {
        String sql = "INSERT INTO servicio (tipo, descripcion, precio_base, " +
                     "tipo_mantenimiento, costo_insumos, pieza, nivel_complejidad) " +
                     "VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement ps = DBConnection.get()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, s instanceof ServicioMantenimiento ?
                    "MANTENIMIENTO" : "REPARACION");
            ps.setString(2, s.getDescripcion());
            ps.setDouble(3, s.getPrecioBase());

            if (s instanceof ServicioMantenimiento sm) {
                ps.setString(4, sm.getTipoMantenimiento());
                ps.setDouble(5, sm.getCostoInsumos());
                ps.setNull(6, Types.VARCHAR);
                ps.setNull(7, Types.INTEGER);
            } else if (s instanceof ServicioReparacion sr) {
                ps.setNull(4, Types.VARCHAR);
                ps.setNull(5, Types.DECIMAL);
                ps.setString(6, sr.getPieza());
                ps.setInt(7, sr.getNivelComplejidad());
            }

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) s.setId(keys.getInt(1));
            }
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM servicio WHERE id = ?";
        try (PreparedStatement ps = DBConnection.get().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // Reconstruye el objeto concreto según el discriminador 'tipo'
    private Servicio mapRow(ResultSet rs) throws SQLException {
        int    id          = rs.getInt("id");
        String descripcion = rs.getString("descripcion");
        double precioBase  = rs.getDouble("precio_base");
        String tipo        = rs.getString("tipo");

        if ("MANTENIMIENTO".equals(tipo)) {
            return new ServicioMantenimiento(id, descripcion, precioBase,
                rs.getString("tipo_mantenimiento"),
                rs.getDouble("costo_insumos"));
        } else {
            return new ServicioReparacion(id, descripcion, precioBase,
                rs.getString("pieza"),
                rs.getInt("nivel_complejidad"));
        }
    }
}
