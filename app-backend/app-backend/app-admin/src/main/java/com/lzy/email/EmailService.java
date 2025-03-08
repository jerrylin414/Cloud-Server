package com.lzy.email;

import cn.hutool.core.util.StrUtil;
import com.lzy.common.ResultCode;
import com.lzy.exception.ServiceException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    /**
     * 发送Gmail邮件
     * @param from 发送者邮箱
     * @param password 应用程序密码
     * @param to 接收者邮箱
     * @param subject 邮件主题
     * @param text 邮件内容
     * @return 发送是否成功
     */
    public boolean sendGmailSimple(String from, String password, String to, String subject, String text) {
        try {
            JavaMailSender mailSender = DynamicMailSender.createGmailSender(from, password);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);
            return true;
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FAILED.getCode(),"EmailService 发送gmail失败." + e);
        }
    }

    /**
     * 发送Gmail邮件
     * @param fromEmail 发送者邮箱
     * @param fromName 发送者昵称
     * @param password 应用程序密码
     * @param to 接收者邮箱
     * @param subject 邮件主题
     * @param text 邮件内容
     * @return 发送是否成功
     */
    public boolean sendGmailSimple(String fromEmail, String fromName, String password, String to, String subject, String text) {
        if (StrUtil.isBlank(fromName)) {
            return sendGmailSimple(fromEmail, password, to, subject, text);
        }
        return sendGmailSimple(fromEmail, fromName, password, to, subject, text, false);
    }

    /**
     * 发送Gmail邮件
     * @param fromEmail 发送者邮箱
     * @param fromName 发送者昵称
     * @param password 应用程序密码
     * @param to 接收者邮箱
     * @param subject 邮件主题
     * @param text 邮件内容
     * @param html 内容是否使用html格式
     * @return 发送是否成功
     */
    public boolean sendGmailSimple(String fromEmail, String fromName, String password, String to, String subject, String text, boolean html) {
        try {
            JavaMailSender mailSender = DynamicMailSender.createGmailSender(fromEmail, password);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail, fromName);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, html);

            mailSender.send(message);
            return true;
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FAILED.getCode(),"EmailService 发送gmail失败." + e);
        }
    }
}


