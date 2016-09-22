package com.wei.mobileoffice.model;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by weiyilin on 16/6/22.
 */
public class OpportunityModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String opportunityid;
    private String opportunitytitle;
    private String customerid;
    private String estimatedamount;
    private String successrate;
    private String expecteddate;
    private String opportunitystatus;
    private String channel;
    private String businesstype;
    private String acquisitiondate;
    private String opportunitiessource;
    private String staffid;
    private String opportunityremarks;

    public OpportunityModel() {}

    public OpportunityModel(JSONObject jsonObject) {
        this.setOpportunityid(jsonObject.optString("opportunityid"));
        this.setOpportunitytitle(jsonObject.optString("opportunitytitle"));
        this.setCustomerid(jsonObject.optString("customerid"));
        this.setEstimatedamount(jsonObject.optString("estimatedamount"));
        this.setSuccessrate(jsonObject.optString("successrate"));
        this.setExpecteddate(jsonObject.optString("expecteddate"));
        this.setOpportunitystatus(jsonObject.optString("opportunitystatus"));
        this.setChannel(jsonObject.optString("channel"));
        this.setBusinesstype(jsonObject.optString("businesstype"));
        this.setAcquisitiondate(jsonObject.optString("acquisitiondate"));
        this.setOpportunitiessource(jsonObject.optString("opportunitiessource"));
        this.setStaffid(jsonObject.optString("staffid"));
        this.setOpportunityremarks(jsonObject.optString("opportunityremarks"));
    }

    public String getOpportunityid() {
        return opportunityid;
    }

    public void setOpportunityid(String opportunityid) {
        this.opportunityid = opportunityid;
    }

    public String getOpportunitytitle() {
        return opportunitytitle;
    }

    public void setOpportunitytitle(String opportunitytitle) {
        this.opportunitytitle = opportunitytitle;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getEstimatedamount() {
        return estimatedamount;
    }

    public void setEstimatedamount(String estimatedamount) {
        this.estimatedamount = estimatedamount;
    }

    public String getSuccessrate() {
        return successrate;
    }

    public void setSuccessrate(String successrate) {
        this.successrate = successrate;
    }

    public String getExpecteddate() {
        return expecteddate;
    }

    public void setExpecteddate(String expecteddate) {
        this.expecteddate = expecteddate;
    }

    public String getOpportunitystatus() {
        return opportunitystatus;
    }

    public void setOpportunitystatus(String opportunitystatus) {
        this.opportunitystatus = opportunitystatus;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getBusinesstype() {
        return businesstype;
    }

    public void setBusinesstype(String businesstype) {
        this.businesstype = businesstype;
    }

    public String getAcquisitiondate() {
        return acquisitiondate;
    }

    public void setAcquisitiondate(String acquisitiondate) {
        this.acquisitiondate = acquisitiondate;
    }

    public String getOpportunitiessource() {
        return opportunitiessource;
    }

    public void setOpportunitiessource(String opportunitiessource) {
        this.opportunitiessource = opportunitiessource;
    }

    public String getStaffid() {
        return staffid;
    }

    public void setStaffid(String staffid) {
        this.staffid = staffid;
    }

    public String getOpportunityremarks() {
        return opportunityremarks;
    }

    public void setOpportunityremarks(String opportunityremarks) {
        this.opportunityremarks = opportunityremarks;
    }
}
