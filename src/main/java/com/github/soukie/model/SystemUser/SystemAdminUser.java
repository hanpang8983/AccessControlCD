package com.github.soukie.model.SystemUser;

/**
 * Created by qiyiy on 2016/1/7.
 */
public class SystemAdminUser {
    public  int adminId ;
    public String adminName;
    private String adminPass;
    public String adminCreateTime;
    public String adminEmail;
    public String adminProfileUrl;
    public String lastUpdateTime;
    public String adminPersonalWebsiteUrl;

    public SystemAdminUser() {

    }

    public SystemAdminUser(int adminId,String adminName, String adminPass, String adminCreateTime) {
        this.adminId = adminId;
        this.adminName = adminName;
        this.adminPass = adminPass;
        this.lastUpdateTime = adminCreateTime;
        this.adminCreateTime = adminCreateTime;
    }

    public void setAdminEmail(String adminEmail, String lastUpdateTime) {
        this.adminEmail = adminEmail;
        this.lastUpdateTime = lastUpdateTime;
    }

    public void setAdminProfileUrl(String newAdminProfileUrl, String lastUpdateTime) {
        this.adminProfileUrl = newAdminProfileUrl;
        this.lastUpdateTime = lastUpdateTime;
    }

    public void setAdminPersonalWebsiteUrl(String adminPersonalWebsiteUrl, String lastUpdateTime) {
        this.adminPersonalWebsiteUrl = adminPersonalWebsiteUrl;
        this.lastUpdateTime = lastUpdateTime;
    }

    public void setAdminPass(String adminPass, String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
        this.adminPass = adminPass;
    }

    /*public void setAdminCreateTime(String adminCreateTime, String lastUpdateTime) {
        this.adminCreateTime = adminCreateTime;
        this.lastUpdateTime = lastUpdateTime;
    }*/

    public void setAdminName(String adminName, String lastUpdateTime) {
        this.adminName = adminName;
        this.lastUpdateTime = lastUpdateTime;

    }

    public String getAdminPass() {
        return adminPass;
    }





}
