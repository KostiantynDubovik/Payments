package com.payments.service.api;

import com.payments.model.Entity;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Kostiantyn Dubovik
 */
public interface Service<E extends Entity> {

    int getCountOfRecords(int criteria);

    List<E> getAll(int startPosition, String orderBy);
    List<E> getAll(int criteria,int startPosition, String orderBy);

    void insert(E entity);

    void update(E entity);

    E getById (int id);

    default void close(Connection connection, Logger logger) {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    default void rollback(Connection connection, Logger logger) {
        try {
            connection.rollback();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    default void commit(Connection connection, Logger logger) {
        try {
            connection.commit();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
