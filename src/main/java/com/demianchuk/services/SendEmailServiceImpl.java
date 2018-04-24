package com.demianchuk.services;

import com.demianchuk.model.MailClient;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmailServiceImpl implements SendEmailService {

    @Override
    public void sendEmail(MailClient client, String addressList,
                          String subject, String emailBody) throws Exception {
        if (addressList.isEmpty())
            throw new Exception("Add at least 1 recipient!");
        if(subject.isEmpty())
            throw new Exception("Your message has no subject!");
        if(emailBody.isEmpty())
            throw new Exception("Your email body has no content!");
        Message email = new MimeMessage(client.getSession());
        email.setSubject(subject);
        email.setText(emailBody);
        try {
            email.setRecipients(Message.RecipientType.TO, InternetAddress.parse(addressList));
            new Thread(new SendEmailThread(email)).start();
        } catch (Exception e) {
            throw new Exception("Email delivery failure!");
        }
    }

    class SendEmailThread implements Runnable {

        private Message email;

        protected SendEmailThread(Message email) {
            this.email = email;
        }

        @Override
        public void run() {
            try {
                Transport.send(email);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }
}
