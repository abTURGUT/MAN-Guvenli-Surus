package org.tensorflow.lite.examples.detection.MailService;


import android.os.StrictMode;

import org.tensorflow.lite.examples.detection.env.Logger;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.Properties;

public class MailService {

    private static final Logger LOGGER = new Logger();

    public void sendKurumsalMail(String firstName, String lastName, String mail, String password){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String mailUsername = "87.srap.87@gmail.com";
        String mailPassword = "xvxmoergakhuaeiv";

        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.host", "smtp.gmail.com");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.debug", "true");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.socketFactory.fallback", "false");
        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator(){
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication(){
                        return new PasswordAuthentication(mailUsername, mailPassword);
                    }
                });
        try{
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailUsername));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail));
            message.setSubject("MAN Güvenli Sürüş - Kurumsal Hesap Giriş Şifreniz");
            message.setText("Merhabalar " + firstName + " " + lastName + ",\n\nKurumsal hesap için yaptığınız başvuru kabul edilmiştir. "
                    + "Uygulamaya giriş için kullanacağınız şifre " + password + " olarak belirlenmiştir. Bu şifreyi kullanarak giriş yapabilirsiniz. "
            );
            Transport.send(message);
            LOGGER.i("Mail gönderildi");
        }
        catch (Exception ex){ LOGGER.i("Mail hata : " + ex.getMessage()); }
    }

    public void sendBireyselMail(String firstName, String lastName, String mail, String password){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String mailUsername = "87.srap.87@gmail.com";
        String mailPassword = "xvxmoergakhuaeiv";

        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.host", "smtp.gmail.com");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.debug", "true");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.socketFactory.fallback", "false");
        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator(){
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication(){
                        return new PasswordAuthentication(mailUsername, mailPassword);
                    }
                });
        try{
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailUsername));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail));
            message.setSubject("MAN Güvenli Sürüş - Uygulama Daveti");
            message.setText("Merhabalar,\n\n" + firstName + " " + lastName + " tarafından uygulama daveti aldınız."
                    + "Uygulamaya bireysel hesap üzerinden " + password + " kodunu kullanarak kayıt olabilirsiniz."
            );
            Transport.send(message);
            LOGGER.i("Mail gönderildi");
        }
        catch (Exception ex){ LOGGER.i("Mail hata : " + ex.getMessage()); }
    }

}
