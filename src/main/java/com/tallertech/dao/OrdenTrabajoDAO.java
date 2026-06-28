package com.tallertech.dao;

import com.tallertech.db.DBConnection;
import com.tallertech.model.OrdenTrabajo;
import com.tallertech.model.OrdenTrabajo.Estado;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class OrdenTrabajoDAO implements IDao<OrdenTrabajo> {

    private static final String SELECT_BASE =
        "SELECT o.id, o.id_vehiculo, o.descripcion, o.estado, " +
        "o.fecha_ingreso, o.fecha_estimada, o.fecha_entrega, " +
        "v.patente, CONCAT(c.apellido, ', ', c.nombre) AS nombre_cliente " +
        "FROM orden_trabajo o " +
        "JOIN vehiculo v ON o.id_vehiculo = v.id " +
        "JOIN cliente  c ON v.id_cliente  = c.id ";

    @Override
    public ArrayList<OrdenTrabajo> listarTodos() throws SQLException {
        ArrayList<OrdenTrabajo> lista = new ArrayList<>();
        try (Statement st = DBConnection.get().createStatement();
             ResultSet rs = st.executeQuery(SELECT_BASE +
                     "ORDER BY o.fecha_ingreso DESC")) {
            while (rs.next()) lista.add(mapRow(rs));
        }
        return lista;
    }

    public ArrayList<OrdenTrabajo> listarActivas() throws SQLException {
        ArrayList<OrdenTrabajo> lista = new ArrayList<>();
        try (Statement st = DBConnection.get().createStatement();
             ResultSet rs = st.executeQuery(SELECT_BASE +
                     "WHERE o.estado <> 'ENTREGADO' ORDER BY o.fecha_ingreso DESC")) {
            while (rs.next()) lista.add(mapRow(rs));
        }
        return lista;
    }

    @Override
    public OrdenTrabajo buscarPorId(int id) throws SQLException {
        String sql = SELECT_BASE + "WHERE o.id = ?";
        try (PreparedStatement ps = DBConnection.get().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    @Override
    public void insertar(OrdenTrabajo o) throws SQLException {
        String sql = "INSERT INTO orden_trabajo " +
                     "(id_vehiculo, descripcion, estado, fecha_ingreso, fecha_estimada) " +
                     "VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = DBConnection.get()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, o.getIdVehiculo());
            ps.setString(2, o.getDescripcion());
            ps.setString(3, o.getEstado().name());
            ps.setDate(4, Date.valueOf(o.getFechaIngreso()));
            ps.setDate(5, o.getFechaEstimada() != null ?
                    Date.valueOf(o.getFechaEstimada()) : null);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) o.setId(keys.getInt(1));
            }
        }
    }

    public void actualizarEstado(int id, Estado nuevoEstado) throws SQLException {
        String sql = nuevoEstado == Estado.ENTREGADO
            ? "UPDATE orden_trabajo SET estado=?, fecha_entrega=? WHERE id=?"
            : "UPDATE orden_trabajo SET estado=? WHERE id=?";
        try (PreparedStatement ps = DBConnection.get().prepareStatement(sql)) {
            ps.setString(1, nuevoEstado.name());
            if (nuevoEstado == Estado.ENTREGADO) {
                ps.setDate(2, Date.valueOf(LocalDate.now()));
                ps.setInt(3, id);
            } else {
                ps.setInt(2, id);
            }
            ps.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM orden_trabajo WHERE id = ?";
        try (PreparedStatement ps = DBConnection.get().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private OrdenTrabajo mapRow(ResultSet rs) throws SQLException {
        OrdenTrabajo o = new OrdenTrabajo();
        o.setId(rs.getInt("id"));
        o.setIdVehiculo(rs.getInt("id_vehiculo"));
        o.setDescripcion(rs.getString("descripcion"));
        o.setEstado(Estado.valueOf(rs.getString("estado")));
        o.setFechaIngreso(rs.getDate("fecha_ingreso").toLocalDate());
        Date fe = rs.getDate("fecha_estimada");
        if (fe != null) o.setFechaEstimada(fe.toLocalDate());
        Date fd = rs.getDate("fecha_entrega");
        if (fd != null) o.setFechaEntrega(fd.toLocalDate());
        o.setPatenteVehiculo(rs.getString("patente"));
        o.setNombreCliente(rs.getString("nombre_cliente"));
        return o;
    }
}
