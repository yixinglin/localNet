package org.hsgt.pricing.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.javassist.NotFoundException;
import org.hsgt.core.domain.ResponseResult;
import org.hsgt.pricing.domain.BulkEmailContact;
import org.hsgt.pricing.services.impl.BulkEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.utils.IoUtils;

import java.util.List;

@Api(tags = "Bulk Email")
@RestController
@RequestMapping("/bulk")
public class BulkEmailController {

    @Autowired
    BulkEmailService bulkEmailService;

    // Append new subscribed Emails to database
    @ApiOperation(value = "appendNewSubscribedEmailContact", notes = "Append new emails to the database. " +
            "Skip if emails exist")
    @PostMapping("/append")
    public ResponseResult<Integer> appendNewSubscribedEmailContact(
            @RequestBody List<BulkEmailContact> bulkEmailContactList) {

        Integer n = 0;
        for (BulkEmailContact b: bulkEmailContactList) {
            b.setSubscribed(true);
            b.setSentAt(null);
            n += bulkEmailService.insertOrSkip(b);
        }
        ResponseResult response = ResponseResult.success().setData(n);
        return response;
    }


    // Mark an email as unsubscribed
    @ApiOperation(value = "markAsUnsubscribed", notes = "Unsubscribe if the email exists in the database." +
            "The given email should be encoded url-safe int the base64 format")
    @GetMapping("/unsubscribed/{base64Email}")
    public String markAsUnsubscribed(@PathVariable String base64Email) {
        String email = IoUtils.base64UrlDecode(base64Email);
        BulkEmailContact bulkEmailContact = bulkEmailService.queryById(email);
        int n = 0;
        String ans;
        try {
            if (bulkEmailContact != null) {
                bulkEmailContact.setSubscribed(false);
                n = bulkEmailService.insertOrUpdate(bulkEmailContact);
                ans = "You have removed your email address from our list.";
            } else {
                ans = "Sorry. We can't find your email on our list";
            }
        } catch (Exception e) {
            throw new RuntimeException("Internal server error.");
        }
        return ans;
    }

    @ApiOperation(value = "markAsSubscribed", notes = "Resubscribe if the email exists in the database." +
            "The given email should be encoded url-safe in base64 format")
    @GetMapping("/resubscribed/{base64Email}")
    public String markAsSubscribed(@PathVariable String base64Email) {
        String email = IoUtils.base64UrlDecode(base64Email);
        BulkEmailContact bulkEmailContact = bulkEmailService.queryById(email);
        int n = 0;
        String ans;
        try {
            if (bulkEmailContact != null) {
                bulkEmailContact.setSubscribed(true);
                n = bulkEmailService.insertOrUpdate(bulkEmailContact);
                ans = "Your email address is on our list. Thank you for your subscription!";
            } else {
                ans = "Sorry. We can't find your email on our list";
            }
        } catch (Exception e) {
            throw new RuntimeException("Internal server error.");
        }
        return ans;
    }

    // Mark an email as sent today
    @ApiOperation(value = "markAsSentToday", notes = "Mark the email as sent if it exists in the database." +
            "The given email should be encoded url-safe in base64 format")
    @GetMapping("/sent/{base64Email}")
    public ResponseResult<Integer> markAsSentToday(@PathVariable String base64Email) {
        String email = IoUtils.base64UrlDecode(base64Email);
        BulkEmailContact bulkEmailContact = bulkEmailService.queryById(email);
        int n = 0;
        ResponseResult ans;
        if (bulkEmailContact != null) {
            bulkEmailContact.setSentAt(IoUtils.currentDateTime());
            n = bulkEmailService.insertOrUpdate(bulkEmailContact);
            ans = ResponseResult.success().setData(n);
        } else {
            ans = ResponseResult.error(new NotFoundException("Email not found")).setData(n);
        }
        return ans;
    }

    // Get the list of all email
    @GetMapping("/contacts")
    public ResponseResult<List<BulkEmailContact>> queryAllEmailContact() {
        List<BulkEmailContact> bulkEmailContacts = bulkEmailService.queryAll();
        bulkEmailContacts.stream().forEach(b -> this.addUnsubscribedLink(b));
        return ResponseResult.success().setData(bulkEmailContacts);
    }

    private void addUnsubscribedLink(BulkEmailContact bulkEmailContact) {
        String b64 = IoUtils.base64UrlEncode(bulkEmailContact.getEmail());
        bulkEmailContact.setUnsubscribedLink("/unsubscribed/" + b64);
    }
}
