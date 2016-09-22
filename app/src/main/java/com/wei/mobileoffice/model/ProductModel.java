package com.wei.mobileoffice.model;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by weiyilin on 16/6/22.
 */
public class ProductModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String productid;
    private String productname;
    private String productsn;
    private String standardprice;
    private String salesunit;
    private String unitcost;
    private String classification;
    private String picture;
    private String introduction;
    private String productremarks;

    public ProductModel() {}

    public ProductModel(JSONObject jsonObject) {
        this.setProductid(jsonObject.optString("productid"));
        this.setProductname(jsonObject.optString("productname"));
        this.setProductsn(jsonObject.optString("productsn"));
        this.setStandardprice(jsonObject.optString("standardprice"));
        this.setSalesunit(jsonObject.optString("salesunit"));
        this.setUnitcost(jsonObject.optString("unitcost"));
        this.setClassification(jsonObject.optString("classification"));
        this.setPicture(jsonObject.optString("picture"));
        this.setIntroduction(jsonObject.optString("introduction"));
        this.setProductremarks(jsonObject.optString("productremarks"));
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductsn() {
        return productsn;
    }

    public void setProductsn(String productsn) {
        this.productsn = productsn;
    }

    public String getStandardprice() {
        return standardprice;
    }

    public void setStandardprice(String standardprice) {
        this.standardprice = standardprice;
    }

    public String getSalesunit() {
        return salesunit;
    }

    public void setSalesunit(String salesunit) {
        this.salesunit = salesunit;
    }

    public String getUnitcost() {
        return unitcost;
    }

    public void setUnitcost(String unitcost) {
        this.unitcost = unitcost;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getProductremarks() {
        return productremarks;
    }

    public void setProductremarks(String productremarks) {
        this.productremarks = productremarks;
    }
}
