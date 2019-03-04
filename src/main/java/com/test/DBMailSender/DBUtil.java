package com.test.DBMailSender;

import java.math.BigDecimal;

public class DBUtil 
{
    public static String toNotNullString(Object obj)
    {
        return (obj == null) ? "" : (String)obj;
    }
    
    public static int toNotNullInt(Object obj)
    {
        return (obj == null) ? 0 : ((BigDecimal)obj).intValue();
    }
    
    public static double toNotNullDouble(Object obj)
    {
        return (obj == null) ? 0 : ((BigDecimal)obj).doubleValue();
    }
    
    public static long toNotNullLong(Object obj)
    {
        return (obj == null) ? 0 : ((BigDecimal)obj).longValue();
    }
    
    public static boolean toNotNullBoolean(Object obj)
    {
        return (obj == null) ? false : (boolean)obj; 
    }
    
    // null 허용
    public static Integer toInteger(BigDecimal bd)
    {
        return (bd == null) ? null : bd.intValue(); 
    }
    
    public static Double toDouble(BigDecimal bd)
    {
        return (bd == null) ? null : bd.doubleValue(); 
    }
}
