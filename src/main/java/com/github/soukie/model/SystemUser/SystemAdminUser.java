package com.github.soukie.model.SystemUser;

import java.util.Date;

/**
 * Created by qiyiy on 2016/1/7.
 */
public class SystemAdminUser {
    public  int adminId ;
    public String adminName;
    private String adminPass;
    public long adminCreateTime;
    public String adminEmail;
    public String adminProfileUrl;
    public long adminLastUpdateTime;
    public String adminPersonalWebsiteUrl;

    public SystemAdminUser() {

    }

    public SystemAdminUser(int adminId,String adminName, String adminPass, long adminCreateTime) {
        this.adminId = adminId;
        this.adminName = adminName;
        this.adminPass = adminPass;
        this.adminLastUpdateTime = adminCreateTime;
        this.adminCreateTime = adminCreateTime;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
        this.adminLastUpdateTime = new Date().getTime();
    }

    public void setAdminProfileUrl(String newAdminProfileUrl) {
        this.adminProfileUrl = newAdminProfileUrl;
        this.adminLastUpdateTime = new Date().getTime();
    }

    public void setAdminPersonalWebsiteUrl(String adminPersonalWebsiteUrl) {
        this.adminPersonalWebsiteUrl = adminPersonalWebsiteUrl;
        this.adminLastUpdateTime = new Date().getTime();
    }

    public void setAdminPass(String adminPass) {
        this.adminPass = adminPass;
        this.adminLastUpdateTime = new Date().getTime();
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
        this.adminLastUpdateTime = new Date().getTime();

    }

    public String getAdminPass() {
        return adminPass;
    }





}
