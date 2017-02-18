package com.payments.dao.api;

import com.payments.model.Entity;

import java.sql.Connection;
import java.util.List;

/**
 * @param <E>
 * @author Dubovik Kostiantyn
 */
public interface DAO<E extends Entity> {


    /**
     * Insert new entity to database
     *
     * @param entity which will be inserted to database
     */
    void insert(E entity, Connection con);

    /**
     * Update entity in database
     *
     * @param entity which will be updated in database
     */
    void update(E entity, Connection con);

    /**
     * Delete entity from database
     *
     * @param id     of entity which will be deleted from database
     * @param aClass for chooseStatus from which table in database entity will be deleted from
     */
    void delete(int id, Class<E> aClass, Connection con);

    /**
     * Read entity from database, chooseStatus by id of entity
     *
     * @param id of needed entity
     * @return entity from database
     */
    E get(int id, Connection con);


    /**
     * Method reads all entities of one type from database
     *
     * @param startPosition for pagination
     * @param orderBy       order in result set
     * @return - list of entities
     */
    List<E> getAllById(int startPosition, String orderBy, Connection con);

    /**
     * Looking for a specific user orders
     *
     * @param id            identifier of entity
     * @param startPosition for pagination
     * @param orderBy       order in result set
     * @return list of orders
     */
    List<E> getAllById(int id, int startPosition, String orderBy, Connection con);

    int getCountOfRecords(int criteria, Connection connection);

}
