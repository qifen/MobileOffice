package com.wei.mobileoffice.model;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by weiyilin on 16/6/21.
 */
public class CustomerModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String customerid;
    private String customername;
    private String profile;
    private String customertype;
    private String customerstatus;
    private String regionid;
    private String parentcustomerid;
    private String customersource;
    private String size;
    private String telephone;
    private String email;
    private String website;
    private String address;
    private String zipcode;
    private String staffid;
    private String customerremarks;

    public CustomerModel() {}

    public CustomerModel(JSONObject jsonObject){
        this.setCustomerid(jsonObject.optString("customerid"));
        this.setCustomername(jsonObject.optString("customername"));
        this.setProfile(jsonObject.optString("profile"));
        this.setCustomertype(jsonObject.optString("customertype"));
        this.setCustomerstatus(jsonObject.optString("customerstatus"));
        this.setRegionid(jsonObject.optString("regionid"));
        this.setParentcustomerid(jsonObject.optString("parentcustomerid"));
        this.setCustomersource(jsonObject.optString("customersource"));
        this.setSize(jsonObject.optString("size"));
        this.setTelephone(jsonObject.optString("telephone"));
        this.setEmail(jsonObject.optString("email"));
        this.setWebsite(jsonObject.optString("website"));
        this.setAddress(jsonObject.optString("address"));
        this.setZipcode(jsonObject.optString("zipcode"));
        this.setStaffid(jsonObject.optString("staffid"));
        this.setCustomerremarks(jsonObject.optString("customerremarks"));
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getCustomertype() {
        return customertype;
    }

    public void setCustomertype(String customertype) {
        this.customertype = customertype;
    }

    public String getCustomerstatus() {
        return customerstatus;
    }

    public void setCustomerstatus(String customerstatus) {
        this.customerstatus = customerstatus;
    }

    public String getRegionid() {
        return regionid;
    }

    public void setRegionid(String regionid) {
        this.regionid = regionid;
    }

    public String getParentcustomerid() {
        return parentcustomerid;
    }

    public void setParentcustomerid(String parentcustomerid) {
        this.parentcustomerid = parentcustomerid;
    }

    public String getCustomersource() {
        return customersource;
    }

    public void setCustomersource(String customersource) {
        this.customersource = customersource;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getStaffid() {
        return staffid;
    }

    public void setStaffid(String staffid) {
        this.staffid = staffid;
    }

    public String getCustomerremarks() {
        return customerremarks;
    }

    public void setCustomerremarks(String customerremarks) {
        this.customerremarks = customerremarks;
    }
}
