package com.tallertech.dao;

import com.tallertech.db.DBConnection;
import com.tallertech.model.OrdenTrabajo;
import com.tallertech.model.OrdenTrabajo.Estado;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrdenTrabajoDAO {

    private static final String SELECT_BASE =
        "SELECT o.id, o.id_vehiculo, o.descripcion, o.estado, " +
        "       o.fecha_ingreso, o.fecha_estimada, o.fecha_entrega, " +
        "       v.patente, CONCAT(c.apellido, ', ', c.nombre) AS nombre_cliente " +
        "FROM orden_trabajo o " +
        "JOIN vehiculo v ON o.id_vehiculo = v.id " +
        "JOIN cliente  c ON v.id_cliente  = c.id ";

    public List<OrdenTrabajo> listarTodas() throws SQLException {
        List<OrdenTrabajo> lista = new ArrayList<>();
        String sql = SELECT_BASE + "ORDER BY o.fecha_ingreso DESC";
        try (Statement st = DBConnection.get().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapRow(rs));
        }
        return lista;
    }

    public List<OrdenTrabajo> listarActivas() throws SQLException {
        List<OrdenTrabajo> lista = new ArrayList<>();
        String sql = SELECT_BASE + "WHERE o.estado <> 'ENTREGADO' ORDER BY o.fecha_ingreso DESC";
        try (Statement st = DBConnection.get().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapRow(rs));
        }
        return lista;
    }

    public List<OrdenTrabajo> listarPorVehiculo(int idVehiculo) throws SQLException {
        List<OrdenTrabajo> lista = new ArrayList<>();
        String sql = SELECT_BASE + "WHERE o.id_vehiculo = ? ORDER BY o.fecha_ingreso DESC";
        try (PreparedStatement ps = DBConnection.get().prepareStatement(sql)) {
            ps.setInt(1, idVehiculo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        }
        return lista;
    }

    public void insertar(OrdenTrabajo o) throws SQLException {
        String sql = "INSERT INTO orden_trabajo (id_vehiculo, descripcion, estado, fecha_ingreso, fecha_estimada) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = DBConnection.get().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, o.getIdVehiculo());
            ps.setString(2, o.getDescripcion());
            ps.setString(3, o.getEstado().name());
            ps.setDate(4, Date.valueOf(o.getFechaIngreso()));
            ps.setDate(5, o.getFechaEstimada() != null ? Date.valueOf(o.getFechaEstimada()) : null);
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

    public void insertarTarea(int idOrden, int idMecanico, String descripcion) throws SQLException {
        String sql = "INSERT INTO tarea (id_orden, id_mecanico, descripcion, fecha) VALUES (?,?,?,?)";
        try (PreparedStatement ps = DBConnection.get().prepareStatement(sql)) {
            ps.setInt(1, idOrden);
            ps.setInt(2, idMecanico);
            ps.setString(3, descripcion);
            ps.setDate(4, Date.valueOf(LocalDate.now()));
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
