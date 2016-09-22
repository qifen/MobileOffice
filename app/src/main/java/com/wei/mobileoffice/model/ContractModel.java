package com.wei.mobileoffice.model;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by weiyilin on 16/6/22.
 */
public class ContractModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String contractid;
    private String contracttitle;
    private String opportunityid;
    private String customerid;
    private String totalamount;
    private String startdate;
    private String enddate;
    private String contractstatus;
    private String contractnumber;
    private String contracttype;
    private String paymethod;
    private String clientcontractor;
    private String ourcontractor;
    private String staffid;
    private String signingdate;
    private String attachment;
    private String contractremarks;

    public ContractModel() {}

    public ContractModel(JSONObject jsonObject) {
        this.setContractid(jsonObject.optString("contractid"));
        this.setContracttitle(jsonObject.optString("contracttitle"));
        this.setOpportunityid(jsonObject.optString("opportunityid"));
        this.setCustomerid(jsonObject.optString("customerid"));
        this.setTotalamount(jsonObject.optString("totalamount"));
        this.setStartdate(jsonObject.optString("startdate"));
        this.setEnddate(jsonObject.optString("enddate"));
        this.setContractstatus(jsonObject.optString("contractstatus"));
        this.setContractnumber(jsonObject.optString("contractnumber"));
        this.setContracttype(jsonObject.optString("contracttype"));
        this.setPaymethod(jsonObject.optString("paymethod"));
        this.setClientcontractor(jsonObject.optString("clientcontractor"));
        this.setOurcontractor(jsonObject.optString("ourcontractor"));
        this.setStaffid(jsonObject.optString("staffid"));
        this.setSigningdate(jsonObject.optString("signingdate"));
        this.setAttachment(jsonObject.optString("attachment"));
        this.setContractremarks(jsonObject.optString("contractremarks"));
    }

    public String getContractid() {
        return contractid;
    }

    public void setContractid(String contractid) {
        this.contractid = contractid;
    }

    public String getContracttitle() {
        return contracttitle;
    }

    public void setContracttitle(String contracttitle) {
        this.contracttitle = contracttitle;
    }

    public String getOpportunityid() {
        return opportunityid;
    }

    public void setOpportunityid(String opportunityid) {
        this.opportunityid = opportunityid;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(String totalamount) {
        this.totalamount = totalamount;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getContractstatus() {
        return contractstatus;
    }

    public void setContractstatus(String contractstatus) {
        this.contractstatus = contractstatus;
    }

    public String getContractnumber() {
        return contractnumber;
    }

    public void setContractnumber(String contractnumber) {
        this.contractnumber = contractnumber;
    }

    public String getContracttype() {
        return contracttype;
    }

    public void setContracttype(String contracttype) {
        this.contracttype = contracttype;
    }

    public String getPaymethod() {
        return paymethod;
    }

    public void setPaymethod(String paymethod) {
        this.paymethod = paymethod;
    }

    public String getClientcontractor() {
        return clientcontractor;
    }

    public void setClientcontractor(String clientcontractor) {
        this.clientcontractor = clientcontractor;
    }

    public String getOurcontractor() {
        return ourcontractor;
    }

    public void setOurcontractor(String ourcontractor) {
        this.ourcontractor = ourcontractor;
    }

    public String getStaffid() {
        return staffid;
    }

    public void setStaffid(String staffid) {
        this.staffid = staffid;
    }

    public String getSigningdate() {
        return signingdate;
    }

    public void setSigningdate(String signingdate) {
        this.signingdate = signingdate;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getContractremarks() {
        return contractremarks;
    }

    public void setContractremarks(String contractremarks) {
        this.contractremarks = contractremarks;
    }
}
