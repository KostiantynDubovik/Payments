package com.payments.service.impl;

import com.payments.dao.api.BillDAO;
import com.payments.dao.impl.BillDAOImpl;
import com.payments.datasource.DataSource;
import com.payments.exception.DaoException;
import com.payments.model.Bill;
import com.payments.service.api.BillService;
import com.payments.utilites.Constants;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

public class BillServiceImpl implements BillService {

    private static final Logger LOG = Logger.getLogger(BillServiceImpl.class);

    private static volatile BillServiceImpl instance;

    private static final BillDAO BILL_DAO = BillDAOImpl.getInstance();

    private static final DataSource DATA_SOURCE = DataSource.getInstance();

    public static BillServiceImpl getInstance() {
        if (instance == null) {
            synchronized (BillServiceImpl.class) {
                if (instance == null) {
                    instance = new BillServiceImpl();
                }
            }
        }
        return instance;
    }

    private BillServiceImpl() {
        LOG.debug(String.format(Constants.ENTITY_WAS_CREATED, this.getClass().getSimpleName()));
    }


    @Override
    public int getCountOfRecords(int userId) {
        Connection connection = DATA_SOURCE.getConnection();
        int result = 0;
        try {
            result = BILL_DAO.getCountOfRecords(userId, connection);
        } catch (DaoException ex) {
            rollback(connection, LOG);
        } finally {
            close(connection, LOG);
        }
        return result;
    }


    @Override
    public List<Bill> getAll(int startPosition, String orderBy) {
        Connection connection = DATA_SOURCE.getConnection();
        List<Bill> bills = new LinkedList<>();
        try {
            bills = BILL_DAO.getAllById(startPosition, orderBy, connection);
        } catch (DaoException ex) {
            rollback(connection, LOG);
        } finally {
            close(connection, LOG);
        }
        return bills;
    }

    @Override
    public List<Bill> getAll(int userId, int startPosition, String orderBy) {
        Connection connection = DATA_SOURCE.getConnection();
        List<Bill> bills = new LinkedList<>();
        try {
            bills = BILL_DAO.getAllById(userId, startPosition, orderBy, connection);
        } catch (DaoException ex) {
            rollback(connection, LOG);
        } finally {
            close(connection, LOG);
        }
        return bills;

    }

    @Override
    public void update(Bill bill) {
        Connection connection = DATA_SOURCE.getConnection();
        try {
            BILL_DAO.update(bill, connection);
            commit(connection,LOG);
        } catch (DaoException ex) {
            rollback(connection, LOG);
        } finally {
            close(connection, LOG);
        }
    }

    @Override
    public Bill getById(int id) {
        Connection connection = DATA_SOURCE.getConnection();
        Bill bill;
        try {
            bill = BILL_DAO.get(id, connection);
        } finally {
            close(connection, LOG);
        }
        return bill;
    }

    @Override
    public Bill getBillByNumber(String billNumber) {
        Connection connection = DATA_SOURCE.getConnection();
        Bill bill = null;
        try {
            bill = BILL_DAO.getBillByNumber(billNumber, connection);
        } catch (DaoException ex) {
            rollback(connection, LOG);
        } finally {
            close(connection, LOG);
        }
        return bill;
    }

    @Override
    public void insert(Bill bill) {
        Connection connection = DATA_SOURCE.getConnection();
        try {
            BILL_DAO.insert(bill, connection);
            commit(connection,LOG);
        } catch (DaoException ex) {
            rollback(connection, LOG);
        } finally {
            close(connection, LOG);
        }
    }

}
