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
                    Long.valueOf(properties.getProperty("adminCreateTime")));

            defaultSystemAdminUser.setAdminEmail(properties.getProperty("adminEmail"));
            defaultSystemAdminUser.setAdminPersonalWebsiteUrl(properties.getProperty("adminPersonalWebsiteUrl"));
            defaultSystemAdminUser.setAdminProfileUrl(properties.getProperty("adminProfileUrl"));

            return defaultSystemAdminUser;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void modifySystemAdminUser(@NotNull String systemAdminUserPropertiesFilePath, SystemAdminUser newSystemAdminUser) {
        Properties properties = new Properties();
        properties.setProperty("adminId", String.valueOf(newSystemAdminUser.adminId));
        properties.setProperty("adminName", newSystemAdminUser.adminName);
        properties.setProperty("adminPass", newSystemAdminUser.getAdminPass());
        properties.setProperty("adminCreateTime", new Date(newSystemAdminUser.adminCreateTime).toString());
        properties.setProperty("adminEmail", newSystemAdminUser.adminEmail);
        properties.setProperty("adminProfileUrl", newSystemAdminUser.adminProfileUrl);
        properties.setProperty("adminPersonalWebsiteUrl", newSystemAdminUser.adminPersonalWebsiteUrl);
        properties.setProperty("adminLastUpdateTime", new Date(newSystemAdminUser.adminLastUpdateTime).toString());
        try (
                OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(systemAdminUserPropertiesFilePath))
                ) {
            properties.store(outputStream,"Modified at" + new Date());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
