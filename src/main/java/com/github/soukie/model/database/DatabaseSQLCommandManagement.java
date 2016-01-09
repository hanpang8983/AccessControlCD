package com.github.soukie.model.database;

/**One instruction class to create or check database command
 * Created by qiyiy on 2016/1/9.
 */
public class DatabaseSQLCommandManagement {

    /**
     * The method to return a sql command to create database according databaseName.
     * @param databaseName: database name
     * @return create a database sql command
     */
    public static String createDatabase(String databaseName) {
        return "create database " + databaseName + ";";
    }

    /**
     * The method to return a sql command to drop (delete) a database according databaseName.
     * @param databaseName database name
     * @return drop (delete) a database sql command
     */
    public static String deleteDatabase(String databaseName) {
        return "drop database " + databaseName + ";";
    }

    /**
     * The method to return a sql command to create a table according table name.
     * @param tableName: table name
     * @return create a table sql command.
     */
    public static String createTable(String tableName) {
        return "create table " + tableName + ";";
    }

    /**
     * The method to return a sql command to delete a table according table name.
     * @param tableName: table name
     * @return delete a table sql command
     */
    public static String deleteTable(String tableName) {
        return "drop table " + tableName + ";";
    }

    /**
     * The method to return a sql command to check if a table named table name in database.
     * @param tableName: table name
     * @return check a table sql command
     */
    public static String checkTable(String tableName) {
        return "show tables like " + "'" + tableName + "'" + ";";
    }

    /*public static String insertOneDACSubjectRecord(String tableName, ACLSubject subject) {

    }

    public static String deleteOneDACSubjectRecord(String tableName, int subjectId) {

    }

    public static String modifyOneDACSubjectRecord(String tableName, int subjectId) {

    }*/

    /**
     * The method to return a sql command to delete one record in table named tableName.
     * @param tableName: table name
     * @param id: record id
     * @return delete one record sql command
     */
    public static String deleteOneRecord(String tableName, int id) {
        return "delete from " + tableName + " where id=" + id + ";";
    }




}
