package com.test.DBMailSender;

import ch.qos.logback.classic.Level;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.util.Properties;

public class App 
{
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static final Properties appProps;
    private static Worker task = null;
    static
    {
        appProps = new Properties();
        
        try(FileInputStream in = new FileInputStream("./conf/config.properties"))
        {
            appProps.load(in);
        }
        catch (Exception e)
        {
            logger.error("failed to load config.properties", e);
            System.exit(0);
        }
    }
    
    public static void main( String[] args ) throws Exception
    {
        addShutdownHook();

        setLogLevel();

        // start worker task
        task = new Worker();
        task.setName("Worker Task");
        task.start();

        task.join();
        
        logger.info("End of main() !!!");
    }
    
    public static Properties getConfigurations()
    {
        return appProps;
    }
    
    private static void setLogLevel()
    {
        String mode = appProps.getProperty("releaseMode");
       
        ch.qos.logback.classic.Logger root = 
            (ch.qos.logback.classic.Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        
        if (StringUtils.equals(mode, "DEBUG"))
            root.setLevel(Level.DEBUG);
        else if (StringUtils.equals(mode, "RELEASE"))
            root.setLevel(Level.INFO);
    }
    
    // graceful shutdown을 위한 ShutdownHook 구현
    private static void addShutdownHook()
    {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> 
        {
            logger.info("ShutdownHook start !!");
            
            try 
            {
                // 자원해제
                DataSourceHolder.destroy();

                task.requestStop();
                task.join();

                Thread.sleep(1000);
            }
            catch (Exception e) 
            {
                logger.error("error occurred on ShutdownHook", e);
            }
            
            logger.info("End of ShutdownHook !!!");
        }));
    }
}
