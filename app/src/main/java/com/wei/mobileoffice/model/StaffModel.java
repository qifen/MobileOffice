package com.wei.mobileoffice.model;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by weiyilin on 16/6/20.
 */
public class StaffModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String staffid;
    public static String STAFF_ID = "staffid";

    private String userid;
    public static String USER_ID = "userid";

    private String name;
    public static String NAME = "name";

    private String departmentname;
    public static String DEPARTMENT_NAME = "departmentname";

    private String departmentid;
    public static String DEPARTMENT_ID = "departmentid";

    private String position;
    public static String POSITION = "position";

    private String gender;
    public static String GENDER = "gender";

    private String mobile;
    public static String MOBILE = "mobile";

    private String weixinid;
    public static String WEIXIN_ID = "weixinid";

    private String avatar;
    public static String AVATAR = "avatar";

    public StaffModel() {}

    public StaffModel(JSONObject jsonObject) {
        this.setStaffid(jsonObject.optString(STAFF_ID));
        this.setUserid(jsonObject.optString(USER_ID));
        this.setName(jsonObject.optString(NAME));
        this.setDepartmentname(jsonObject.optString(DEPARTMENT_NAME));
        this.setDepartmentid(jsonObject.optString(DEPARTMENT_ID));
        this.setGender(jsonObject.optString(GENDER));
        this.setMobile(jsonObject.optString(MOBILE));
        this.setWeixinid(jsonObject.optString(WEIXIN_ID));
        this.setAvatar(jsonObject.optString(AVATAR));
        this.setPosition(jsonObject.optString(POSITION));
    }

    public String getStaffid() {
        return staffid;
    }

    public void setStaffid(String staffid) {
        this.staffid = staffid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartmentname() {
        return departmentname;
    }

    public void setDepartmentname(String departmentname) {
        this.departmentname = departmentname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getWeixinid() {
        return weixinid;
    }

    public void setWeixinid(String weixinid) {
        this.weixinid = weixinid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartmentid() {
        return departmentid;
    }

    public void setDepartmentid(String departmentid) {
        this.departmentid = departmentid;
    }
}
