package com.DATN.FiveITViec.services;

import com.DATN.FiveITViec.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    @Autowired
    JavaMailSender sender;

    public void sendEmailResetPassword(User user,String newPass, String emailFrom, String emailTo) throws MessagingException {
        MimeMessage message = sender.createMimeMessage();
        // Sử dụng Helper để thiết lập các thông tin cần thiết cho message
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        String subject  = "FiveITViec - Recovery password /n ";
        String content = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;\n" +
                "            color: #333333;\n" +
                "            line-height: 1.5;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "        .container {\n" +
                "            max-width: 600px;\n" +
                "            margin: 0 auto;\n" +
                "            padding: 20px;\n" +
                "        }\n" +
                "        h1 {\n" +
                "            font-size: 24px;\n" +
                "            margin-bottom: 20px;\n" +
                "            color: white;\n"+
                "        }\n" +
                "        p {\n" +
                "            font-size: 16px;\n" +
                "            margin-bottom: 10px;\n" +
                "        }\n" +
                "        strong {\n" +
                "            color: #000000;\n" +
                "            font-weight: bold;\n" +
                "        }\n" +
                "        .header {\n" +
                "            background-color: #198754;\n" +
                "            color: #fff;\n" +
                "            padding: 20px;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .content {\n" +
                "            background-color: #f5f5f5;\n" +
                "            padding: 20px;\n" +
                "            border: 1px solid #e0e0e0;\n" +
                "        }\n" +
                "        .button {\n" +
                "            display: inline-block;\n" +
                "            padding: 10px 20px;\n" +
                "            background-color: #007bff;\n" +
                "            color: #fff;\n" +
                "            text-decoration: none;\n" +
                "            border-radius: 5px;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            background-color: #333333;\n" +
                "            color: #fff;\n" +
                "            padding: 10px;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">\n" +
                "            <h1>Welcome, " + user.getEmail() + "!</h1>\n" +
                "        </div>\n" +
                "        <div class=\"content\">\n" +
                "            <p>Thank you for joining our website. We have generated a new password for you:</p>\n" +
                "            <p><strong>" + newPass + "</strong></p>\n" +
                "            <p>Please keep this password confidential and ensure its security.</p>\n" +
                "            <p>If you did not request a password reset, please contact our support team immediately.</p>\n" +
                "        </div>\n" +
                "        <div class=\"footer\">\n" +
                "            <p>Best regards,</p>\n" +
                "            <p>FiveItViec Team</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";

        helper.setFrom(emailFrom);
        helper.setTo(emailTo);
        helper.setSubject(subject);
        helper.setText(content, true);
        helper.setReplyTo(emailFrom);
        // Gửi message đến SMTP server
        sender.send(message);
    }

    public void sendEmailApprovalEmployer(User user, String emailFrom) throws MessagingException {
        MimeMessage message = sender.createMimeMessage();
        // Sử dụng Helper để thiết lập các thông tin cần thiết cho message
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        String subject  = "FiveITViec - Recovery password /n ";
        String content = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;\n" +
                "            color: #333333;\n" +
                "            line-height: 1.5;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "        .container {\n" +
                "            max-width: 600px;\n" +
                "            margin: 0 auto;\n" +
                "            padding: 20px;\n" +
                "        }\n" +
                "        h1 {\n" +
                "            font-size: 24px;\n" +
                "            margin-bottom: 20px;\n" +
                "            color: white;\n"+
                "        }\n" +
                "        p {\n" +
                "            font-size: 16px;\n" +
                "            margin-bottom: 10px;\n" +
                "        }\n" +
                "        strong {\n" +
                "            color: #000000;\n" +
                "            font-weight: bold;\n" +
                "        }\n" +
                "        .header {\n" +
                "            background-color: #198754;\n" +
                "            color: #fff;\n" +
                "            padding: 20px;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .content {\n" +
                "            background-color: #f5f5f5;\n" +
                "            padding: 20px;\n" +
                "            border: 1px solid #e0e0e0;\n" +
                "        }\n" +
                "        .button {\n" +
                "            display: inline-block;\n" +
                "            padding: 10px 20px;\n" +
                "            background-color: #007bff;\n" +
                "            color: #fff;\n" +
                "            text-decoration: none;\n" +
                "            border-radius: 5px;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            background-color: #333333;\n" +
                "            color: #fff;\n" +
                "            padding: 10px;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">\n" +
                "            <h1>Welcome, " + user.getEmail() + "!</h1>\n" +
                "        </div>\n" +
                "        <div class=\"content\">\n" +
                "            <p>Thank you for joining our website. We have checked and approved your employer account</p>\n" +
                "            <p>Now you can enjoy our website</p>\n" +
                "        </div>\n" +
                "        <div class=\"footer\">\n" +
                "            <p>Best regards,</p>\n" +
                "            <p>FiveItViec Team</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";

        helper.setFrom(emailFrom);
        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        helper.setText(content, true);
        helper.setReplyTo(emailFrom);
        // Gửi message đến SMTP server
        sender.send(message);
    }
    
    public void sendEmailNoticeUserLoginWithDifferentWays(String passWord, String emailFrom, String emailTo) throws MessagingException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        String subject  = "FiveITViec - Welcome to our website /n ";
        String content = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;\n" +
                "            color: #333333;\n" +
                "            line-height: 1.5;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "        .container {\n" +
                "            max-width: 600px;\n" +
                "            margin: 0 auto;\n" +
                "            padding: 20px;\n" +
                "        }\n" +
                "        h1 {\n" +
                "            font-size: 24px;\n" +
                "            margin-bottom: 20px;\n" +
                "            color: white;\n"+
                "        }\n" +
                "        p {\n" +
                "            font-size: 16px;\n" +
                "            margin-bottom: 10px;\n" +
                "        }\n" +
                "        strong {\n" +
                "            color: #000000;\n" +
                "            font-weight: bold;\n" +
                "        }\n" +
                "        .header {\n" +
                "            background-color: #198754;\n" +
                "            color: #fff;\n" +
                "            padding: 20px;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .content {\n" +
                "            background-color: #f5f5f5;\n" +
                "            padding: 20px;\n" +
                "            border: 1px solid #e0e0e0;\n" +
                "        }\n" +
                "        .button {\n" +
                "            display: inline-block;\n" +
                "            padding: 10px 20px;\n" +
                "            background-color: #007bff;\n" +
                "            color: #fff;\n" +
                "            text-decoration: none;\n" +
                "            border-radius: 5px;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            background-color: #333333;\n" +
                "            color: #fff;\n" +
                "            padding: 10px;\n" +
                "            text-align: center;\n" +
                "        }\n" +
"    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">\n" +
                "            <h1>Welcome, " + emailTo + "!</h1>\n" +
                "        </div>\n" +
                "        <div class=\"content\">\n" +
                "            <p>Thank you for joining our website. This is the username and password for you to login our website :</p>\n" +
                "            <p>Username: <strong>" + emailTo + "</strong></p>\n" +
                "            <p><strong>" + passWord + "</strong></p>\n" +
                "            <p>Please keep this password confidential and ensure its security.</p>\n" +
                "            <p>If you did not request a password reset, please contact our support team immediately.</p>\n" +
                "        </div>\n" +
                "        <div class=\"footer\">\n" +
                "            <p>Best regards,</p>\n" +
                "            <p>FiveItViec Team</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";

        helper.setFrom(emailFrom);
        helper.setTo(emailTo);
        helper.setSubject(subject);
        helper.setText(content, true);
        helper.setReplyTo(emailFrom);
        sender.send(message);
    }


}
