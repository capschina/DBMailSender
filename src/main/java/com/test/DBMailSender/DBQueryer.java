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


    public static List<Map<String, Object>> getEmailInfo(String userId) throws SQLException
    {

        String sql =
            "select SEQ, TITLE, USER_EMAIL, MSG from TB_EMAIL where USER_ID = ? and IS_NEW = 1";

        QueryRunner run = new QueryRunner(DataSourceHolder.getDataSource(), true);
        ResultSetHandler<List<Map<String, Object>>> h = new MapListHandler();

        return run.query(sql, h, userId);
    }

    public static void deleteMail(Long seq) throws SQLException
    {
        String sql = "update TB_EMAIL set IS_NEW = 0 where SEQ = ?";
        QueryRunner run = new QueryRunner(DataSourceHolder.getDataSource(), true);

        int cnt = run.update(sql, new Object[] {seq});
        if (cnt != 1)
            logger.error("failed to deleteMail. seq = {}", seq);
    }
}
