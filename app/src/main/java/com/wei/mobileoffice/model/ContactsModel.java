package com.wei.mobileoffice.model;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by weiyilin on 16/6/21.
 */
public class ContactsModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String contactsid;
    private String customerid;
    private String contactsname;
    private String contactsage;
    private String contactsgender;
    private String contactsmobile;
    private String contactstelephone;
    private String contactsemail;
    private String contactsaddress;
    private String contactszipcode;
    private String contactsqq;
    private String contactswechat;
    private String contactswangwang;
    private String contactsdeptname;
    private String contactsposition;
    private String contactsremarks;

    public ContactsModel() {
    }

    public ContactsModel(JSONObject jsonObject) {
        this.setContactsid(jsonObject.optString("contactsid"));
        this.setCustomerid(jsonObject.optString("customerid"));
        this.setContactsname(jsonObject.optString("contactsname"));
        this.setContactsage(jsonObject.optString("contactsage"));
        this.setContactsgender(jsonObject.optString("contactsgender"));
        this.setContactsmobile(jsonObject.optString("contactsmobile"));
        this.setContactstelephone(jsonObject.optString("contactstelephone"));
        this.setContactsemail(jsonObject.optString("contactsemail"));
        this.setContactsaddress(jsonObject.optString("contactsaddress"));
        this.setContactszipcode(jsonObject.optString("contactszipcode"));
        this.setContactsqq(jsonObject.optString("contactsqq"));
        this.setContactswechat(jsonObject.optString("contactswechat"));
        this.setContactswangwang(jsonObject.optString("contactswangwang"));
        this.setContactsdeptname(jsonObject.optString("contactsdeptname"));
        this.setContactsposition(jsonObject.optString("contactsposition"));
        this.setContactsremarks(jsonObject.optString("contactsremarks"));
    }

    public String getContactsid() {
        return contactsid;
    }

    public void setContactsid(String contactsid) {
        this.contactsid = contactsid;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getContactsname() {
        return contactsname;
    }

    public void setContactsname(String contactsname) {
        this.contactsname = contactsname;
    }

    public String getContactsage() {
        return contactsage;
    }

    public void setContactsage(String contactsage) {
        this.contactsage = contactsage;
    }

    public String getContactsgender() {
        return contactsgender;
    }

    public void setContactsgender(String contactsgender) {
        this.contactsgender = contactsgender;
    }

    public String getContactsmobile() {
        return contactsmobile;
    }

    public void setContactsmobile(String contactsmobile) {
        this.contactsmobile = contactsmobile;
    }

    public String getContactstelephone() {
        return contactstelephone;
    }

    public void setContactstelephone(String contactstelephone) {
        this.contactstelephone = contactstelephone;
    }

    public String getContactsemail() {
        return contactsemail;
    }

    public void setContactsemail(String contactsemail) {
        this.contactsemail = contactsemail;
    }

    public String getContactsaddress() {
        return contactsaddress;
    }

    public void setContactsaddress(String contactsaddress) {
        this.contactsaddress = contactsaddress;
    }

    public String getContactszipcode() {
        return contactszipcode;
    }

    public void setContactszipcode(String contactszipcode) {
        this.contactszipcode = contactszipcode;
    }

    public String getContactsqq() {
        return contactsqq;
    }

    public void setContactsqq(String contactsqq) {
        this.contactsqq = contactsqq;
    }

    public String getContactswechat() {
        return contactswechat;
    }

    public void setContactswechat(String contactswechat) {
        this.contactswechat = contactswechat;
    }

    public String getContactswangwang() {
        return contactswangwang;
    }

    public void setContactswangwang(String contactswangwang) {
        this.contactswangwang = contactswangwang;
    }

    public String getContactsdeptname() {
        return contactsdeptname;
    }

    public void setContactsdeptname(String contactsdeptname) {
        this.contactsdeptname = contactsdeptname;
    }

    public String getContactsposition() {
        return contactsposition;
    }

    public void setContactsposition(String contactsposition) {
        this.contactsposition = contactsposition;
    }

    public String getContactsremarks() {
        return contactsremarks;
    }

    public void setContactsremarks(String contactsremarks) {
        this.contactsremarks = contactsremarks;
    }
}
