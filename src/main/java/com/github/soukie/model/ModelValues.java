package com.github.soukie.model;

/**
 * Created by qiyiy on 2016/1/5.
 */
public class ModelValues {
    /**
     * Whole Module Values.
     */

    public static final String DATABASE_MYSQL_PROPERTIES_FILE_PATH = "res/properties/mysql.ini";

    /**
     * DAC Policy Values.
     */
    public static final String CENTRALIZED_ACL = "centralized_acl_policy";
    public static final String DISTRIBUTED_ACL = "distributed_acl_policy";
    public static final String LIMITED_DISTRIBUTED_ACL = "limited_distributed_acl_policy";
    public static final String ACCESS_CONTROL_DATABASE_NAME = "access_control_cd";
    public static final String DAC_SUBJECT_TABLE_NAME = "dac_subjects";
    public static final String DAC_OBJECT_TABLE_NAME = "dac_objects";
    public static final String DAC_AL_CAPABILITY_TABLE_NAME = "dac_al_capabilities";

    public static final String DAC_SUBJECT_TABLE_CREATE_SQL = "create table +" +
            DAC_SUBJECT_TABLE_NAME +
            "(" +
            "id int not null primary key," +
            "name varchar(255) not null ," +
            "password varchar(255) not null," +
            "info varchar(255)," +
            "createdTime long," +
            "lastUpdateTime" +
            ");";

    public static final String DAC_OBJECT_TABLE_CREATE_SQL = "create table " +
            DAC_OBJECT_TABLE_NAME +
            "(" +
            "id int not null primary key," +
            "name varchar(255) not null ," +
            "info varchar(255) not null," +
            "createdSubjectId int not null," +
            "createdSubjectName varchar(255) not null," +
            "createdTime long not null," +
            "lastUpdateTime long not null," +
            "executable boolean" +
            ");";

    public static final String DAC_AL_CAPABILITY_TABLE_CREATE_SQL = "create table " +
            DAC_AL_CAPABILITY_TABLE_NAME +
            "(" +
            "capabilityId int not null primary key," +
            "objectId int not null," +
            "objectName varchar(255) not null," +
            "grantedSubjectId int not null," +
            "grantedSubjectName varchar(255) not null," +
            "subjectId int not null," +
            "subjectName varchar(255) not null," +
            "capabilityType varchar(255) not null," +
            "capabilityString varchar(255) not null," +
            "capabilityInfo varchar(255)" +
            ");";
}
