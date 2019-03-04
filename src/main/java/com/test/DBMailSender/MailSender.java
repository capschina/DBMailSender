package com.test.DBMailSender;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MailSender 
{
    private static final Logger logger = LoggerFactory.getLogger(MailSender.class);
    
    static
    {
        // 긴 첨부파일 일름이 메일에서 깨지는 현상이 발생한다.
        // 아래 시스템 프로퍼티를 추가하면 문제가 해결됨.
        // 이유는 javax.mail.internet.ParameterList 코드를 보면 알 수 있음
        System.getProperties().setProperty("mail.mime.splitlongparameters", "false");

    }

    public static void sendSimpleMail(String receiver, String subject, String msg)
        throws EmailException
    {
        Email email = new SimpleEmail();
        email.setHostName("smtp.gmail.com");
        email.setSmtpPort(465);
        email.setAuthenticator(new DefaultAuthenticator("user", "pass"));
        email.setSSLOnConnect(true);
        email.setFrom("test@gmail.com");
        email.setSubject(subject);
        email.setMsg(msg);
        email.addTo(receiver);

        email.send();
    }

    /**
     * 첨부 파일 이메일 전송
     * @param file
     * @param receivers
     * @throws EmailException
     */
    public static void sendEmalWithAttachments(File file, List<String> receivers)
            throws EmailException 
    {
        String fileName = file.getName();
        
        EmailAttachment attachment = new EmailAttachment();
        attachment.setPath(file.getAbsolutePath());
        attachment.setDisposition(EmailAttachment.ATTACHMENT);
        attachment.setDescription(file.getName()); // 이건 머하는건지 모르겠네
        attachment.setName(file.getName());
        
        MultiPartEmail email = new MultiPartEmail();
        email.setHostName("smtp.gmail.com");
        email.setSmtpPort(465);
        email.setFrom("test@gmail.com");
        email.setAuthenticator(new DefaultAuthenticator("user", "pass"));
        email.setSSLOnConnect(true);
        
        String msg = "test attachments(" + fileName + ")";
        email.setSubject(msg);
        email.setMsg(msg);
        email.addTo(receivers.stream().toArray(String[]::new));
        
        email.attach(attachment);
        email.send();
        
        logger.info("Send mail success. fileName = {}", file.getName());
    }
}
