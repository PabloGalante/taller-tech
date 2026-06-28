package com.tallertech.dao;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * INTERFACE genérica que define el contrato CRUD para todos los DAOs.
 * Aplica el principio de abstracción: los menús operan contra IDao<T>
 * sin conocer la implementación concreta de cada DAO.
 *
 * @param <T> tipo de entidad gestionada por el DAO
 */
public interface IDao<T> {
    void           insertar(T entidad)  throws SQLException;
    T              buscarPorId(int id)  throws SQLException;
    ArrayList<T>   listarTodos()        throws SQLException;
    void           eliminar(int id)     throws SQLException;
}
