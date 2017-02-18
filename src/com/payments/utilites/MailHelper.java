package com.payments.utilites;


import com.payments.model.*;
import org.apache.log4j.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.PasswordAuthentication;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import java.util.Locale;
import java.util.Properties;


import java.io.ByteArrayOutputStream;
import java.util.ResourceBundle;


import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

/**
 * @author Kostiantyn Dubovik
 */
public class MailHelper {
    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger.getLogger(MailHelper.class);

    private static final PropertyHolder PROPERTY_HOLDER = PropertyHolder.getInstance();

    /**
     * @param user with all information about activation
     * @throws MessagingException Look -> {@link MessagingException}
     */
    public static void sendMailOfRegistrationGreeting(User user)
            throws MessagingException {
        Properties properties = formMessageOfRefistration(user);
        Message msg = new MimeMessage(getSession());
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(properties.getProperty("mail")));
        msg.setSubject(properties.getProperty("subject"));
        msg.setText(properties.getProperty("message"));
        Transport.send(msg);
    }

    /**
     * @param user for form message to
     * @return array where first element is subject of message, and second element is a massage
     */
    private static Properties formMessageOfRefistration(User user) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("com.payments.i18n.mailMessages", new Locale(user.getLanguage().split("_")[0], user.getLanguage().split("_")[1]));
        Properties properties = new Properties();
        properties.setProperty("mail", user.getEmail());
        properties.setProperty("subject", resourceBundle.getString("mail.activation.subject"));
        properties.setProperty("message", resourceBundle.getString("mail.activation.message"));
        return properties;
    }

    /**
     * @param payment with all information about payment
     */
    public static void sendMailWithPdfReport(Payment payment, String language, String email) {
        ByteArrayOutputStream outputStream = GeneratePdf.createPdf(createPropertiesForPdf(payment, language));

        MimeMultipart mimeMultipart = new MimeMultipart();

        try {
            DataSource dataSource = new ByteArrayDataSource(outputStream.toByteArray(), "application/pdf");
            MimeBodyPart pdfBodyPart = new MimeBodyPart();
            pdfBodyPart.setDataHandler(new DataHandler(dataSource));
            pdfBodyPart.setFileName("Report.pdf");

            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText("Report");

            mimeMultipart.addBodyPart(textBodyPart);
            mimeMultipart.addBodyPart(pdfBodyPart);

            MimeMessage message = new MimeMessage(getSession());

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("subject");
            message.setContent(mimeMultipart);

            Transport.send(message);
        } catch (MessagingException e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
        }
    }


    private static Properties createPropertiesForPdf(Payment payment, String language) {

        ResourceBundle resourceBundle = ResourceBundle.getBundle("com.payments.i18n.mailMessages", new Locale(language.split("_")[0], language.split("_")[1]));

        String fromBill;
        String toBill;
        String balanceBefore;
        String balanceAfter;
        Bill bill;
        if (payment instanceof OutgoingPayment) {
            bill = ((OutgoingPayment) payment).getBillWhereFromIsPayment();
            fromBill = bill.getBillNumber();
            toBill = ((OutgoingPayment) payment).getNumberOfBillWherePaymentGoing();
            balanceBefore = bill.getBalance().subtract(payment.getSumOfPayment().add(payment.getCommission())).toString();
            balanceAfter = bill.getBalance().toString();
        } else {
            bill = ((IncomingPayment) payment).getPaymentToBill();
            fromBill = ((IncomingPayment) payment).getPaymentFromBill();
            toBill = ((IncomingPayment) payment).getPaymentToBill().getBillNumber();
            balanceBefore = bill.getBalance().subtract(payment.getSumOfPayment()).toString();
            balanceAfter = bill.getBalance().toString();
        }

        Properties properties = new Properties();
        properties.setProperty("fromBillMessage", resourceBundle.getString("mail.label.bill_from"));
        properties.setProperty("fromBill", fromBill);
        properties.setProperty("toBillMessage", resourceBundle.getString("mail.label.to_bill"));
        properties.setProperty("toBill", toBill);
        properties.setProperty("sumMessage", resourceBundle.getString("mail.label.sum"));
        properties.setProperty("sum", payment.getSumOfPayment().toString());
        properties.setProperty("commissionMessage", resourceBundle.getString("mail.label.commission"));
        properties.setProperty("commission", payment.getCommission().toString());
        properties.setProperty("dateMessage", resourceBundle.getString("mail.label.date"));
        properties.setProperty("date", payment.getDateOfPayment().toString());
        properties.setProperty("balanceBeforeMessage", resourceBundle.getString("mail.label.balance_before"));
        properties.setProperty("balanceBefore", balanceBefore);
        properties.setProperty("balanceAfterMessage", resourceBundle.getString("mail.label.balance_after"));
        properties.setProperty("balanceAfter", balanceAfter);
        properties.setProperty("commentMessage", resourceBundle.getString("mail.label.comment"));
        properties.setProperty("comment", payment.getComment());

        return properties;

    }

    /**
     * @return s
     */
    private static Session getSession() {
        return Session.getDefaultInstance(getPropertiesOfMail(),
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(PROPERTY_HOLDER.getEmailAccount(), PROPERTY_HOLDER.getEmailPassword());
                    }
                });
    }

    /**
     * @return properties for configure email
     */
    private static Properties getPropertiesOfMail() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");
        return properties;
    }
}
