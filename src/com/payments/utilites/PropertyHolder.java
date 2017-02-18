package com.payments.utilites;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Kostiantyn Dubovik
 */
public class PropertyHolder {

    private static final Logger LOG = Logger.getLogger(PropertyHolder.class);
    private static PropertyHolder propertyHolder;

    private static final String CAPTCHA_PROPERTIES_SOURCE = "com/payments/properties/captcha.properties";
    private static final String DATABASE_PROPERTIES_SOURCE = "com/payments/properties/database.properties";
    private static final String EMAIL_PROPERTIES_SOURCE = "com/payments/properties/email.properties";

    private String jdbcUrl;
    private String dbUserLogin;
    private String dbUserPassword;

    private String driverClassName;
    private int maxPoolSize;
    private boolean autoCommit;


    private int totalChars;
    private int height;
    private int width;
    private int circle;


    private String emailAccount;
    private String emailPassword;

    /**
     *
     */
    private PropertyHolder() {
        loadProperties();
    }

    /**
     * @return instance of PropertyHolder
     */
    public static synchronized PropertyHolder getInstance() {
        if (propertyHolder == null) {
            propertyHolder = new PropertyHolder();
        }

        return propertyHolder;
    }

    /**
     *
     */
    private void loadProperties() {
        Properties prop = new Properties();
        try {
            LOG.debug("Start loading properties for database");
            prop.load(getClass().getClassLoader().getResourceAsStream(DATABASE_PROPERTIES_SOURCE));

            this.driverClassName = prop.getProperty(Constants.DRIVER);
            this.jdbcUrl = prop.getProperty(Constants.JDBC_URL);
            this.dbUserLogin = prop.getProperty(Constants.LOGIN);
            this.dbUserPassword = prop.getProperty(Constants.PASSWORD);

            this.maxPoolSize = Integer.valueOf(prop.getProperty(Constants.MAX_POOL_SIZE));
            this.autoCommit = Boolean.valueOf(prop.getProperty(Constants.AUTO_COMMIT));
            LOG.debug("Finish loading properties for database");
        } catch (IOException io) {
            LOG.error(io.getMessage());
        }

        try {
            LOG.debug("Start loading properties for captcha");
            prop.load(getClass().getClassLoader().getResourceAsStream(CAPTCHA_PROPERTIES_SOURCE));

            this.totalChars = Integer.valueOf(prop.getProperty(Constants.TOTAL_CHARS));
            this.width = Integer.valueOf(prop.getProperty(Constants.WIDTH));
            this.height = Integer.valueOf(prop.getProperty(Constants.HEIGHT));
            this.circle = Integer.valueOf(prop.getProperty(Constants.CIRCLE));
            LOG.debug("Finish loading properties for captcha");
        }catch (IOException io){
            LOG.error(io.getMessage());
        }

        try {
            LOG.debug("Start loading properties for email");
            prop.load(getClass().getClassLoader().getResourceAsStream(EMAIL_PROPERTIES_SOURCE));

            this.emailAccount = prop.getProperty(Constants.EMAIL_ACCOUNT);
            this.emailPassword = prop.getProperty(Constants.EMAIL_PASSWORD);
            LOG.debug("Finish loading properties for email");
        }catch (IOException io){
            LOG.error(io.getMessage());
        }

    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public String getDbUserLogin() {
        return dbUserLogin;
    }

    public String getDbUserPassword() {
        return dbUserPassword;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    /**
     * @return value of maxPoolSize
     */
    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public boolean isAutoCommit() {
        return autoCommit;
    }

    public int getTotalChars() {
        return totalChars;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getCircle() {
        return circle;
    }

    public String getEmailAccount() {
        return emailAccount;
    }

    public String getEmailPassword() {
        return emailPassword;
    }
}
