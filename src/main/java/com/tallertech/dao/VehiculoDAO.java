package com.tallertech.dao;

import com.tallertech.db.DBConnection;
import com.tallertech.model.Vehiculo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehiculoDAO {

    private static final String SELECT_BASE =
        "SELECT v.id, v.id_cliente, v.marca, v.modelo, v.anio, v.patente, " +
        "       CONCAT(c.apellido, ', ', c.nombre) AS nombre_cliente " +
        "FROM vehiculo v JOIN cliente c ON v.id_cliente = c.id ";

    public List<Vehiculo> listarTodos() throws SQLException {
        List<Vehiculo> lista = new ArrayList<>();
        try (Statement st = DBConnection.get().createStatement();
             ResultSet rs = st.executeQuery(SELECT_BASE + "ORDER BY v.patente")) {
            while (rs.next()) lista.add(mapRow(rs));
        }
        return lista;
    }

    public List<Vehiculo> listarPorCliente(int idCliente) throws SQLException {
        List<Vehiculo> lista = new ArrayList<>();
        String sql = SELECT_BASE + "WHERE v.id_cliente = ? ORDER BY v.patente";
        try (PreparedStatement ps = DBConnection.get().prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        }
        return lista;
    }

    public Vehiculo buscarPorPatente(String patente) throws SQLException {
        String sql = SELECT_BASE + "WHERE v.patente = ?";
        try (PreparedStatement ps = DBConnection.get().prepareStatement(sql)) {
            ps.setString(1, patente.toUpperCase());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public void insertar(Vehiculo v) throws SQLException {
        String sql = "INSERT INTO vehiculo (id_cliente, marca, modelo, anio, patente) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = DBConnection.get().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, v.getIdCliente());
            ps.setString(2, v.getMarca());
            ps.setString(3, v.getModelo());
            ps.setInt(4, v.getAnio());
            ps.setString(5, v.getPatente().toUpperCase());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) v.setId(keys.getInt(1));
            }
        }
    }

    public void actualizar(Vehiculo v) throws SQLException {
        String sql = "UPDATE vehiculo SET id_cliente=?, marca=?, modelo=?, anio=?, patente=? WHERE id=?";
        try (PreparedStatement ps = DBConnection.get().prepareStatement(sql)) {
            ps.setInt(1, v.getIdCliente());
            ps.setString(2, v.getMarca());
            ps.setString(3, v.getModelo());
            ps.setInt(4, v.getAnio());
            ps.setString(5, v.getPatente().toUpperCase());
            ps.setInt(6, v.getId());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM vehiculo WHERE id = ?";
        try (PreparedStatement ps = DBConnection.get().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Vehiculo mapRow(ResultSet rs) throws SQLException {
        Vehiculo v = new Vehiculo(
            rs.getInt("id"),
            rs.getInt("id_cliente"),
            rs.getString("marca"),
            rs.getString("modelo"),
            rs.getInt("anio"),
            rs.getString("patente")
        );
        v.setNombreCliente(rs.getString("nombre_cliente"));
        return v;
    }
}
