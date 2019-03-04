package com.test.DBMailSender;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBQueryer 
{
    private static final Logger logger = LoggerFactory.getLogger(DBQueryer.class);


    public static Map<String, Object> getEmailInfo(String userId) throws SQLException
    {

        String sql = "select TITLE, USER_EMAIL, MSG from TB_EMAIL where USER_ID = ?";

        QueryRunner run = new QueryRunner(DataSourceHolder.getDataSource(), true);
        ResultSetHandler<List<Map<String, Object>>> h = new MapListHandler();

        List<Map<String, Object>> rs = run.query(sql, h, userId);
        if (rs == null  | rs.isEmpty())
        {
            logger.error("no such user. user id = {}", userId);
            return null;
        }

        return rs.get(0);
    }
}
