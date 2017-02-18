package com.payments.datasource;

import com.payments.utilites.Constants;
import com.payments.utilites.PropertyHolder;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Dubovik Kostiantyn
 */
public class DataSource {


    private static final Logger LOG = Logger.getLogger(DataSource.class);


    private static HikariDataSource connectionPool;

    /**
     *
     */
    private static volatile DataSource dataSource;


    /**
     *
     */
    private DataSource() {
        initConnectionPool();
    }

    /**
     * @return instance of DataSource
     */
    public static synchronized DataSource getInstance() {
        if (dataSource == null) {
            synchronized (DataSource.class) {
                if (dataSource == null) {
                    dataSource = new DataSource();
                }
            }
        }
        return dataSource;
    }

    private void initConnectionPool() {
        LOG.debug("Start initialise connection pool.");
        PropertyHolder propertyHolder = PropertyHolder.getInstance();

        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setDriverClassName(propertyHolder.getDriverClassName());
        hikariConfig.setJdbcUrl(propertyHolder.getJdbcUrl());
        hikariConfig.setUsername(propertyHolder.getDbUserLogin());
        hikariConfig.setPassword(propertyHolder.getDbUserPassword());
        hikariConfig.setMaximumPoolSize(propertyHolder.getMaxPoolSize());
        hikariConfig.setAutoCommit(propertyHolder.isAutoCommit());
        hikariConfig.setIdleTimeout(60000);

        connectionPool = new HikariDataSource(hikariConfig);
        LOG.debug("Connection pool initialisation finished.");
    }

    /**
     * @return connection to database
     */
    public Connection getConnection() {
        Connection connection = null;
        LOG.debug(Constants.TRY_TO_GET_CONNECTION);
        try {
            connection = connectionPool.getConnection();
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
        }
        LOG.trace(String.format(Constants.CONNECTION_RECEIVED, connection));
        return connection;
    }

}
