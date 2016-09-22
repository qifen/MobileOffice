package com.wei.mobileoffice.model;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by weiyilin on 16/6/23.
 */
public class FollowupModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String followupid;
    private String sourceid;
    private String sourcetype;
    private String followuptype;
    private String createtime;
    private String creatorid;
    private String content;
    private String followupremarks;

    public FollowupModel() {}

    public FollowupModel(JSONObject jsonObject) {
        this.setFollowupid(jsonObject.optString("followupid"));
        this.setSourceid(jsonObject.optString("sourceid"));
        this.setSourcetype(jsonObject.optString("sourcetype"));
        this.setFollowuptype(jsonObject.optString("followuptype"));
        this.setCreatetime(jsonObject.optString("createtime"));
        this.setCreatorid(jsonObject.optString("creatorid"));
        this.setContent(jsonObject.optString("content"));
        this.setFollowupremarks(jsonObject.optString("followupremarks"));
    }

    public String getFollowupid() {
        return followupid;
    }

    public void setFollowupid(String followupid) {
        this.followupid = followupid;
    }

    public String getSourceid() {
        return sourceid;
    }

    public void setSourceid(String sourceid) {
        this.sourceid = sourceid;
    }

    public String getSourcetype() {
        return sourcetype;
    }

    public void setSourcetype(String sourcetype) {
        this.sourcetype = sourcetype;
    }

    public String getFollowuptype() {
        return followuptype;
    }

    public void setFollowuptype(String followuptype) {
        this.followuptype = followuptype;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getCreatorid() {
        return creatorid;
    }

    public void setCreatorid(String creatorid) {
        this.creatorid = creatorid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFollowupremarks() {
        return followupremarks;
    }

    public void setFollowupremarks(String followupremarks) {
        this.followupremarks = followupremarks;
    }
}
