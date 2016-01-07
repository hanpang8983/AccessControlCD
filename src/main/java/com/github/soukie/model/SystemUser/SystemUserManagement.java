package com.github.soukie.model.SystemUser;

import com.sun.istack.internal.NotNull;

import java.io.*;
import java.util.Date;
import java.util.Properties;

/**
 * Created by qiyiy on 2016/1/7.
 */
public class SystemUserManagement {

    /**
     * The method to load default system admin user from properties file.
     * @param systemAdminUserPropertiesFilePath: system admin usr properties file path
     * @return
     */
    public static SystemAdminUser getDefaultSystemAdminUser(@NotNull String systemAdminUserPropertiesFilePath) {
        Properties properties = new Properties();
        try (
                InputStream inputStream = new BufferedInputStream(new FileInputStream(systemAdminUserPropertiesFilePath))
                ) {
            properties.load(inputStream);
            SystemAdminUser defaultSystemAdminUser = new SystemAdminUser(Integer.parseInt(properties.getProperty("adminId")),
                    properties.getProperty("adminName"),
                    properties.getProperty("adminPass"),
                    properties.getProperty("adminCreateTime"));

            defaultSystemAdminUser.setAdminEmail(properties.getProperty("adminEmail"), String.valueOf(new Date().getTime()));
            defaultSystemAdminUser.setAdminPersonalWebsiteUrl(properties.getProperty("adminPersonalWebsiteUrl"),
                    String.valueOf(new Date().getTime()));
            defaultSystemAdminUser.setAdminProfileUrl(properties.getProperty("adminProfileUrl"),
                    String.valueOf(new Date().getTime()));

            return defaultSystemAdminUser;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
