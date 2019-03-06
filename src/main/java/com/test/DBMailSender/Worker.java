package com.test.DBMailSender;

import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class Worker extends Thread
{
    private static final Logger logger = LoggerFactory.getLogger(Worker.class);
    private boolean isThreadRun = true;

    public void requestStop()
    {
        logger.info("Worker.requestStop()");
        this.interrupt();
    }

    @Override
    public void run()
    {
        logger.info("Worker Start");

        while((! Thread.currentThread().isInterrupted()) && isThreadRun)
        {
            try
            {
                List<Map<String, Object>> rows = DBQueryer.getEmailInfo("user");

                for (Map<String, Object> row : rows)
                {
                    MailSender.sendSimpleMail(
                        (String) row.get("USER_EMAIL"),
                        (String) row.get("TITLE"),
                        (String) row.get("MSG"));

                    DBQueryer.deleteMail(DBUtil.toNotNullLong(row.get("SEQ")));
                }
            }
            catch (SQLException | EmailException e)
            {
                logger.error("An error occurred on Worker", e);
            }


            if (! isThreadRun)
                break; // break -> while((! Thread.currentThread().isInterrupted()) && isThreadRun)

            try
            {
                logger.debug("Worker sleep");
                Thread.sleep(60 * 1000); // 1 min
            }
            catch (InterruptedException e)
            {
                isThreadRun = false;
                Thread.currentThread().interrupt();
                logger.info("Worker InterruptedException !");
            }
        }

        logger.info("end of Worker. isInterrupted = {}, isThreadRun = {}",
            Thread.currentThread().isInterrupted(), isThreadRun);
    }


}
