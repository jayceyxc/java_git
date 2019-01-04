package com.linus.tools;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author yuxuecheng
 * @Title: DBCPMysqlPool
 * @ProjectName java_git
 * @Description: 利用Apache DBCP库的数据库连接池
 * @date 2019-01-03 17:04
 */
public class DBCPMysqlPool {
    /**
     * 建立连接的驱动驱动名称
     */
    public static final String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
    /**
     * 数据库链接数据库的url
     */
    public static final String URL = "jdbc:mysql://localhost:33966/af";
    /**
     * 链接的数据库账号
     */
    public static final String USERNAME = "root";
    /**
     * 链接的数据库密码
     */
    public static final String PASSWORD = "Mypassword@2qq";
    /**
     * 最大空闲链接
     */
    private static final int MAX_IDLE = 3;
    /**
     * 最大等待时间
     */
    private static final long MAX_WAIT = 5000;
    /**
     * 最大活动链接
     */
    private static final int MAX_TOTAL = 5;
    /**
     * 初始化时链接池的数量
     */
    private static final int INITIAL_SIZE = 10;

    /**
     * 链接实例
     */
    private static BasicDataSource dataSource = new BasicDataSource();

    /**
     * 初始化链接参数
     */
    static{
        dataSource.setDriverClassName(DRIVER_CLASS_NAME);
        dataSource.setUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaxIdle(MAX_IDLE);
        dataSource.setMaxWaitMillis(MAX_WAIT);
        dataSource.setMaxTotal(MAX_TOTAL);
        dataSource.setInitialSize(INITIAL_SIZE);
    }

    /**
     * 提供获得数据源
     * @return 数据源
     */
    public static DataSource getDateSource(){
        return dataSource;
    }

    /**
     * 提供获得链接
     * @return 数据库连接
     * @throws SQLException 数据库异常
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
