package org.hsgt.utils;


import org.hsgt.config.AccountConfig;
import org.hsgt.entities.common.BulkEmailContact;
import org.hsgt.entities.common.Email;
import org.hsgt.services.impl.BulkEmailService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.utils.IoUtils;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@SpringBootTest
public class EmailTest {

    @Autowired
    BulkEmailService bulkEmailService;

    @Test
    public void testSmtpEmail() throws MessagingException, UnsupportedEncodingException {
        JSONObject config = AccountConfig.getConfigInstance();
        JSONObject notification = config.getJSONObject("notification");
        JSONObject outgoing = notification.getJSONObject("outgoing");
        String receiver = notification.getJSONObject("receiver").getJSONArray("emails").getString(0);
        Email email = (Email) IoUtils.jsonToObject(outgoing, Email.class);
        email.setSubject("Test");
        email.setBody("<h1>Hey!!</h1>");
        EmailClient client = new EmailClient(email);
        client.applySmtpSession();
        // client.send(InternetAddress.parse(receiver));
    }

    @Test
    public void testDatabase() {
        BulkEmailContact bulkEmailContact = new BulkEmailContact();
        bulkEmailContact.setEmail("1230qqq8@qq.com");
        bulkEmailContact.setFirstName("sss");
        bulkEmailContact.setLastName("aaaa");
        bulkEmailContact.setSubscribed(false);
        bulkEmailContact.setSentAt(IoUtils.currentDateTime());
        bulkEmailService.insertOrUpdate(bulkEmailContact);
        // bulkEmailService.insertOrSkip(bulkEmailContact);
    }

}
