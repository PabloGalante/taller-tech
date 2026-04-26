package com.tallertech.dao;

import com.tallertech.db.DBConnection;
import com.tallertech.model.Mecanico;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MecanicoDAO {

    public List<Mecanico> listarTodos() throws SQLException {
        List<Mecanico> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, apellido, especialidad FROM mecanico ORDER BY apellido, nombre";
        try (Statement st = DBConnection.get().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapRow(rs));
        }
        return lista;
    }

    public Mecanico buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nombre, apellido, especialidad FROM mecanico WHERE id = ?";
        try (PreparedStatement ps = DBConnection.get().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    private Mecanico mapRow(ResultSet rs) throws SQLException {
        return new Mecanico(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getString("apellido"),
            rs.getString("especialidad")
        );
    }
}
