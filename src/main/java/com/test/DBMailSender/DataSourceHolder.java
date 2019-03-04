package com.test.DBMailSender;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

//import oracle.jdbc.driver.OracleDriver;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DB connection pool 설정. 싱글톤 방식으로 관리
 */
public class DataSourceHolder 
{
    private static final Logger logger = LoggerFactory.getLogger(DataSourceHolder.class);
    private final BasicDataSource ds;
    
    private DataSourceHolder()
    {
        ds = new BasicDataSource();
        ds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        ds.setUrl("jdbc:oracle:thin:TEST/testpass@//192.168.99.99:1521/TEST");
        
        ds.setInitialSize(1);
        ds.setMinIdle(1);
        ds.setMaxIdle(4);
        ds.setMaxTotal(4);
        ds.setTestOnBorrow(true);
        ds.setValidationQuery("select 1 from dual");
    }

    public static DataSource getDataSource()
    {
        return LazyHolder.INSTANCE.ds;
    }
    
    public static Connection getConnection() throws SQLException
    {
        return LazyHolder.INSTANCE.ds.getConnection();
    }
    
    public static void destroy() throws SQLException
    {
        // BasicDataSource.close 메소드에 대한 설명은 아래 공식 문서 참고
        // http://commons.apache.org/proper/commons-dbcp/api-2.5.0/org/apache/commons/dbcp2/BasicDataSource.html#close--
        LazyHolder.INSTANCE.ds.close();
        
        logger.info("close dataSource.");
    }
    
    public static class LazyHolder
    { 
        private static final DataSourceHolder INSTANCE = new DataSourceHolder();
    }
}
