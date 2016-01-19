package com.github.soukie.model.database;

import com.github.soukie.model.DACPolicy.objects.ACLObject;
import com.github.soukie.model.DACPolicy.objects.ACLSubject;
import com.github.soukie.model.DACPolicy.objects.BlackToken;
import com.github.soukie.model.DACPolicy.objects.Capability;
import com.github.soukie.model.ModelValues;
import com.github.soukie.model.RABCPolicy.subjects.*;
import com.sun.istack.internal.NotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 * One class can operate and management database.
 * Created by qiyiy on 2016/1/5.
 */
public class DatabaseOperation {
    public static final int QUERY_CAPABILITIES_MOD_SUBJECT_ID = 0;
    public static final int QUERY_CAPABILITIES_MOD_OBJECT_ID = 1;
    public static final int QUERY_CAPABILITIES_MOD_GRANTED_SUBJECT_ID = 2;
    public static final int QUERY_CAPABILITIES_MOD_DEFAULT_CAPABILITY_ID = 4;
    public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    public int connectionStatus;
    private long operationTime;
    private String DatabaseDriverPath;
    private Connection connection;


    public DatabaseOperation(long operationTime) {
        this.operationTime = operationTime;
        this.DatabaseDriverPath = MYSQL_DRIVER;
        this.connectionStatus = 1;
    }


    /**
     * The method to change operations' connection.
     *
     * @param newConnection: new connection.
     */
    public void changeConnection(@NotNull Connection newConnection) {
        this.connection = newConnection;
    }

    /**
     * The method to close database's connection
     *
     * @return 0: close failed; 1: close succeed.
     */
    public int closeConnection() {
        try {
            connection.close();
            this.connectionStatus = 0;

            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * The method to return operation's database connection.
     *
     * @return
     */
    public Connection getConnection() {
        return connection;
    }

    public String getMySQLDriverPath() {
        return DatabaseDriverPath;
    }

    public long getOperationTime() {
        return operationTime;
    }

    /**
     * Initialize the database connection.
     *
     * @param databasePropertiesFilePath: database properties file path.
     * @throws ClassNotFoundException
     */
    public void initDatabaseConnection(String databasePropertiesFilePath) throws ClassNotFoundException {
        Class.forName(DatabaseDriverPath);
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(databasePropertiesFilePath));

            String url = properties.getProperty("url");
            String user = properties.getProperty("user");
            String pass = properties.getProperty("pass");
            connection = DriverManager.getConnection(url, user, pass);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize the database connection.
     *
     * @param url:  database connection url
     * @param user: database user name
     * @param pass: database user pass
     * @throws ClassNotFoundException
     */
    public void initDatabaseConnection(String url, String user, String pass) throws ClassNotFoundException {
        Class.forName(DatabaseDriverPath);
        try {
            connection = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    /************************************************************************************************************
     * The following method are adding or modifying subject operations.
     * This methods will change database record, and this operations can not support
     * multi-thread. So you must achieve multi-thread by yourselves' code.
     */

    /**
     * The method to add a subject in database.
     *
     * @param id:       subject's id
     * @param name:     subject's name
     * @param password: subject's password
     * @param info:     subject's information
     * @return return int 0: add subject error 1: add one subject succeed
     */

    public int addSubject(int id,
                          String name,
                          String password,
                          String info,
                          long createdTime,
                          long lastUpdateTime) {
        String addSubjectRecordSql = "insert into " +
                ModelValues.DAC_SUBJECT_TABLE_NAME +
                "(id,name,password,info,createdTime,lastUpdateTime) values('" +
                id + "','" +
                name + "','" +
                password + "','" +
                info + "','" +
                createdTime + "','" +
                lastUpdateTime + "');";

        try (
                Statement statement = connection.createStatement();
                ResultSet checkResultSet = statement.executeQuery("show tables like '" + ModelValues.DAC_SUBJECT_TABLE_NAME + "';")
        ) {

            //Check is the table empty.
            if (!checkResultSet.next()) {
                statement.executeUpdate(ModelValues.DAC_SUBJECT_TABLE_CREATE_SQL);
                return statement.executeUpdate(addSubjectRecordSql);

            } else {
                return statement.executeUpdate(addSubjectRecordSql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }

    }

    /**
     * The method to delete a subject from table.
     *
     * @param id: the id of subject that will be deleted
     * @return return int 0: operated failed; 1: operated succeed; 2: there is not the record as id = id in the table
     */

    public int deleteSubject(int id) {
        String deleteSubjectSql = "delete from " + ModelValues.DAC_SUBJECT_TABLE_NAME + " where id=" + id + ";";
        try (
                Statement statement = connection.createStatement();
                ResultSet checkResultSet = statement.executeQuery("select * from " +
                        ModelValues.DAC_SUBJECT_TABLE_NAME + " where id=" + id + ";")
        ) {
            //Check fi the table has the record.
            if (!checkResultSet.next()) {
                return 2;
            } else {
                return statement.executeUpdate(deleteSubjectSql);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * The method to modify a subject' values.
     *
     * @param id:       subject's id
     * @param name:     subject's name
     * @param password: subject's password
     * @param info:     subject's information
     * @return 0: operated failed; 1: operated succeed 2: there is no the record as id = id in the table
     */
    public int modifySubject(int id, String name, String password, String info, long lastUpdateTime) {
        String modifySubjectSql = "update " + ModelValues.DAC_SUBJECT_TABLE_NAME + " set " +
                "name='" + name +
                "',password='" + password +
                "',info='" + info +
                "',lastUpdateTime='" + lastUpdateTime +
                "' where id=" + id + ";";
        try (
                Statement statement = connection.createStatement();
                ResultSet checkResultSet = statement.executeQuery("select * from " +
                        ModelValues.DAC_SUBJECT_TABLE_NAME + " where id=" + id + ";")
        ) {
            //Check if there is the capability record in the table.
            if (!checkResultSet.next()) {
                return 2;
            } else {
                return statement.executeUpdate(modifySubjectSql);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * The method to query a subject.
     *
     * @param id: id of a subject that will be queried
     * @return null: queried nothing; ACLSubject: the subject with id = id
     */
    public ACLSubject queryOneSubject(int id) {
        String queryOneSubjectSql = "select * from " + ModelValues.DAC_SUBJECT_TABLE_NAME + " where id=" + id + ";";
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryOneSubjectSql)
        ) {
            if (!resultSet.next()) {
                return null;
            } else {
                return new ACLSubject(id,
                        resultSet.getString("name"),
                        resultSet.getString("password"),
                        resultSet.getLong("createdTime"),
                        resultSet.getLong("lastUpdateTime"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * The method to query one subject by subject's name
     *
     * @param name subject's name
     * @return Succeed: ACLSubject; Failed: null
     */
    public ACLSubject queryOneSubjectByName(String name) {
        String queryOneSubjectByName = "select * from " + ModelValues.DAC_SUBJECT_TABLE_NAME + " where name='" + name + "';";
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryOneSubjectByName)
        ) {
            if (!resultSet.next()) {
                return null;
            } else {
                return new ACLSubject(resultSet.getInt("id"),
                        name,
                        resultSet.getString("password"),
                        resultSet.getLong("createdTime"),
                        resultSet.getLong("lastUpdateTime"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * The method to query subject's id by subject's name.
     *
     * @param name: subject's name
     * @return -1: queried no record or failed; != -1: subject's id
     */
    public int querySubjectIdByName(String name) {
        String querySubjectIdByNameSql = "select id from " + ModelValues.DAC_SUBJECT_TABLE_NAME + " where name='" + name + "';";
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(querySubjectIdByNameSql)
        ) {
            if (!resultSet.next()) {
                return -1;
            } else {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * The method to query all of subjects.
     *
     * @return null: queried failed or there is not one subject; ArrayList<ACLSubject>: return a ArrayList of ACLSubjects
     */

    public ArrayList<ACLSubject> queryAllSubjects() {
        String queryAllSubjectsSql = "select * from " + ModelValues.DAC_SUBJECT_TABLE_NAME + ";";
        ArrayList<ACLSubject> resultACLSubjectArrayList = new ArrayList<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryAllSubjectsSql)
        ) {
            if (!resultSet.next()) {
                return null;
            } else {
                resultACLSubjectArrayList.add(new ACLSubject(resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("password"),
                        resultSet.getLong("createdTime"),
                        resultSet.getLong("lastUpdateTime")));
                while (resultSet.next()) {
                    resultACLSubjectArrayList.add(new ACLSubject(resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("password"),
                            resultSet.getLong("createdTime"),
                            resultSet.getLong("lastUpdateTime")));
                }
                return resultACLSubjectArrayList;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * The method to add a object.
     *
     * @param id:               object's id
     * @param name:             object's name
     * @param info:             object's information
     * @param createdSubjectId: id of the subject that created the object
     * @return 0: operated failed; 1: operated succeed
     */
    public int addObject(int id,
                         String name,
                         String info,
                         int createdSubjectId,
                         String createdSubjectName,
                         long createdTime,
                         boolean executable) {
        String addObjectRecordSql = "insert into " +
                ModelValues.DAC_OBJECT_TABLE_NAME +
                "(id,name,info,createdSubjectId,createdSubjectName,createdTime,lastUpdateTime,executable) values('" +
                id + "','" +
                name + "','" +
                info + "','" +
                createdSubjectId + "','" +
                createdSubjectName + "','" +
                createdTime + "','" +
                createdTime + "'," +
                executable + ");";

        try (
                Statement statement = connection.createStatement();
                ResultSet checkResultSet = statement.executeQuery("show tables like '" + ModelValues.DAC_OBJECT_TABLE_NAME + "';")
        ) {

            //Check is the table empty.
            if (!checkResultSet.next()) {
                statement.executeUpdate(ModelValues.DAC_OBJECT_TABLE_CREATE_SQL);
                return statement.executeUpdate(addObjectRecordSql);

            } else {
                return statement.executeUpdate(addObjectRecordSql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }

    }

    /**
     * The method to delete a object.
     *
     * @param id: object's id
     * @return 0: operated failed; 1: operated succeed; 2: there is no record as id = id in the table
     */
    public int deleteObject(int id) {
        String deleteObjectSql = "delete from " + ModelValues.DAC_OBJECT_TABLE_NAME + " where id=" + id + ";";
        try (
                Statement statement = connection.createStatement();
                ResultSet checkResultSet = statement.executeQuery("select * from " +
                        ModelValues.DAC_OBJECT_TABLE_NAME +
                        " where id=" + id + ";")
        ) {
            //Check is the table empty.
            if (!checkResultSet.next()) {
                return 2;
            } else {
                return statement.executeUpdate(deleteObjectSql);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * The method to modify a object
     *
     * @param id:             object's id
     * @param name:           object's name
     * @param info:           object's information
     * @param lastUpdateTime: the last update time
     * @return 0: operated failed; 1: operated succeed; 2: there is no the record as id = id int the table
     */
    public int modifyObject(int id,
                            String name,
                            String info,
                            long lastUpdateTime,
                            boolean executable) {
        String modifyObjectSql = "update " + ModelValues.DAC_OBJECT_TABLE_NAME + " set " +
                "name='" + name +
                "',info='" + info +
                "',lastUpdateTime='" + lastUpdateTime +
                "',executable=" + executable +
                " where id=" + id + ";";
        try (
                Statement statement = connection.createStatement();
                ResultSet checkResultSet = statement.executeQuery("select * from " +
                        ModelValues.DAC_SUBJECT_TABLE_NAME +
                        " where id=" + id + ";")
        ) {
            //Check is the table empty.
            if (!checkResultSet.next()) {
                return 2;
            } else {
                return statement.executeUpdate(modifyObjectSql);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * The method to query a object with di = id
     *
     * @param id: subject's id
     * @return null: queried failed; ACLObject: one ACLObject
     */
    public ACLObject queryOneObject(int id) {
        String queryOneObjectSql = "select * from " + ModelValues.DAC_OBJECT_TABLE_NAME + " while id=" + id + ";";
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryOneObjectSql)
        ) {
            if (!resultSet.next()) {
                return null;
            } else {
                return new ACLObject(id,
                        resultSet.getString("name"),
                        resultSet.getInt("createdSubjectId"),
                        resultSet.getString("createdSubjectName"),
                        resultSet.getLong("createdTime"),
                        resultSet.getBoolean(5));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * The method to query one object by object's name
     *
     * @param name: object's name
     * @return Succeed: ACLObject; Failed: null
     */
    public ACLObject queryOneObjectByName(String name) {
        String queryOneObjectByNameSQL = "select * from " + ModelValues.DAC_OBJECT_TABLE_NAME + " where name='" + name + "';";
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryOneObjectByNameSQL)
        ) {
            if (!resultSet.next()) {
                return null;
            } else {
                return new ACLObject(resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("createdSubjectId"),
                        resultSet.getString("createdSubjectName"),
                        resultSet.getLong("createdTime"),
                        resultSet.getBoolean(5));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int queryObjectIdByName(String name) {
        String queryObjectNameByIdSql = "select id from " + ModelValues.DAC_OBJECT_TABLE_NAME + " where name='" + name + "';";
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryObjectNameByIdSql)
        ) {
            if (resultSet.next()) {
                return resultSet.getInt("id");
            } else {
                return -1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * The method to query all objects.
     *
     * @return null: queried failed; ArrayList<ACLObject>: ArrayList contains all record objects
     */
    public ArrayList<ACLObject> queryAllObjects() {
        String queryAllObjectsSql = "select * from " + ModelValues.DAC_OBJECT_TABLE_NAME + ";";
        ArrayList<ACLObject> resultQueryAllObjects = new ArrayList<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryAllObjectsSql)
        ) {
            if (!resultSet.next()) {
                return null;
            } else {
                resultQueryAllObjects.add(new ACLObject(resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("createdSubjectId"),
                        resultSet.getString("createdSubjectName"),
                        resultSet.getLong("createdTime"),
                        resultSet.getBoolean(5)));
                while (resultSet.next()) {
                    resultQueryAllObjects.add(new ACLObject(resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getInt("createdSubjectId"),
                            resultSet.getString("createdSubjectName"),
                            resultSet.getLong("createdTime"),
                            resultSet.getBoolean(5)));
                }
                return resultQueryAllObjects;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * The method to add capability record.
     *
     * @param capabilityId        : capability's id
     * @param objectId:           object's id
     * @param objectName:         object's name
     * @param grantedSubjectId:   granted subject's id
     * @param grantedSubjectName: granted subject's name
     * @param capabilityString:   capability's string
     * @param capabilityInfo:     capability's information
     * @return 0: operated failed; 1: operated succeed
     */
    public int addCapability(int capabilityId,
                             int objectId,
                             String objectName,
                             int grantedSubjectId,
                             String grantedSubjectName,
                             int subjectId,
                             String subjectName,
                             long createdTime,
                             long lastUpdateTime,
                             String capabilityString,
                             String capabilityInfo) {
        String addCapabilitySql = "insert into " +
                ModelValues.DAC_AL_CAPABILITY_TABLE_NAME +
                "(capabilityId,objectId,objectName,grantedSubjectId,grantedSubjectName,subjectId,subjectName," +
                "createdTime,lastUpdateTime,capabilityString,capabilityInfo) values(" +
                "'" + capabilityId + "'," +
                "'" + objectId + "'," +
                "'" + objectName + "'," +
                "'" + grantedSubjectId + "'," +
                "'" + grantedSubjectName + "'," +
                "'" + subjectId + "'," +
                "'" + subjectName + "'," +
                "'" + createdTime + "'," +
                "'" + lastUpdateTime + "'," +
                "'" + capabilityString + "'," +
                "'" + capabilityInfo + "');";
        try (
                Statement statement = connection.createStatement();
                ResultSet checkResult = statement.executeQuery("show tables like '" + ModelValues.DAC_AL_CAPABILITY_TABLE_NAME + "';")
        ) {
            if (!checkResult.next()) {
                statement.executeUpdate(ModelValues.DAC_AL_CAPABILITY_TABLE_CREATE_SQL);
                return statement.executeUpdate(addCapabilitySql);
            } else {
                return statement.executeUpdate(addCapabilitySql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * The method to delete capability from table.
     *
     * @param capabilityId: capability's id
     * @return 0: operated failed; 1: operated succeed; 2: there is no record as id = capabilityId in table.
     */
    public int deleteCapability(int capabilityId) {
        String deleteCapabilitySql = "delete from " + ModelValues.DAC_AL_CAPABILITY_TABLE_NAME +
                " where capabilityId=" + capabilityId + ";";
        try (
                Statement statement = connection.createStatement();
                ResultSet checkResultSet = statement.executeQuery("select * from " +
                        ModelValues.DAC_AL_CAPABILITY_TABLE_NAME + " where capabilityId=" + capabilityId + ";")
        ) {
            //Check if the table has the record.
            if (checkResultSet.next()) {
                return 2;
            } else {
                return statement.executeUpdate(deleteCapabilitySql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * The method to delete capabilities by granted subject's id
     *
     * @param grantedSubjectId: granted subject id
     * @return 0: failed; >0: succeed
     */
    public int deleteCapabilityByGrantedSubjectId(int grantedSubjectId) {
        String deleteCapabilityByGrantedSubjectIdSql = "delete from " + ModelValues.DAC_AL_CAPABILITY_TABLE_NAME +
                " where grantedSubjectId=" + grantedSubjectId + ";";
        try (
                Statement statement = connection.createStatement()
        ) {
            return statement.executeUpdate(deleteCapabilityByGrantedSubjectIdSql);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * The method to delete capabilities by subject's id.
     *
     * @param subjectId: subject id
     * @return 0: failed; >0:succeed
     */
    public int deleteCapabilityBySubjectId(int subjectId) {
        String deleteCapabilityBySubjectIdSql = "delete from " + ModelValues.DAC_AL_CAPABILITY_TABLE_NAME +
                " where subjectId=" + subjectId + ';';
        try (
                Statement statement = connection.createStatement()
        ) {
            return statement.executeUpdate(deleteCapabilityBySubjectIdSql);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * The method to delete capabilities by object's id
     *
     * @param objectId
     * @return
     */
    public int deleteCapabilityByObjectId(int objectId) {
        String deleteCapabilityByObjectIdSql = "delete from " + ModelValues.DAC_AL_CAPABILITY_TABLE_NAME +
                " where objectId=" + objectId + ";";
        try (
                Statement statement = connection.createStatement()
        ) {
            return statement.executeUpdate(deleteCapabilityByObjectIdSql);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * The method to delete capability by granted subject's id, object's id and capability string. And will recursively
     * delete all capability from granted subject.
     *
     * @param grantedSubjectId: granted subject's id
     * @param objectId:         object's id
     * @param capabilityString: capability string
     * @return 0: deleted failed or exception; >0: number of deleted capability record
     */
    public int deleteCapabilityByGrantedSubjectIdObjectIdCapabilityString(int grantedSubjectId, int objectId, String capabilityString) {
        try (
                Statement statement = connection.createStatement()
        ) {
            return deleteCapabilityByGrantedSubjectIdObjectIdCapabilityString(grantedSubjectId, objectId, capabilityString, statement);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private int deleteCapabilityByGrantedSubjectIdObjectIdCapabilityString(int grantedSubjectId, int objectId, String capabilityString, Statement statement) {
        int result = 0;
        String deleteCapabilityByGrantedSubjectIdObjectIdCapabilityString = "select * from" + ModelValues.DAC_AL_CAPABILITY_TABLE_NAME +
                " where grantedSubjectId=" + grantedSubjectId +
                " and objectId=" + objectId +
                " and capabilityString='" + capabilityString + "';";
        try (ResultSet resultSet = statement.executeQuery(deleteCapabilityByGrantedSubjectIdObjectIdCapabilityString)) {
            while (resultSet.next()) {
                result += deleteCapabilityByGrantedSubjectIdObjectIdCapabilityString(resultSet.getInt("subjectId"), objectId, capabilityString, statement);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * The method to delete capability record by granted subject's id, subject's id, object's id and capability string.
     *
     * @param grantedSubjectId: granted subject's id
     * @param subjectId:        subject's id
     * @param objectId"         object's id
     * @param capabilityString: capability string
     * @return 0: deleted failed >0:deleted succeed.
     */
    public int deleteCapabilityByGSSOCSId(int grantedSubjectId, int subjectId, int objectId, String capabilityString) {
        String deleteCapabilityByGSSOIdSql = "delete from " + ModelValues.DAC_AL_CAPABILITY_TABLE_NAME +
                " where grantedSubjectId=" + grantedSubjectId +
                " and subjectId=" + subjectId +
                " and objectId=" + objectId +
                " and capabilityString='" + capabilityString + "';";
        try (
                Statement statement = connection.createStatement()
        ) {
            return statement.executeUpdate(deleteCapabilityByGSSOIdSql);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * The method to modify capability record in table.
     *
     * @param capabilityId:        capability's id
     * @param newCapabilityString: new capability string
     * @param newCapabilityInfo:   new capability info
     * @return 0: operated failed; 1: operated succeed; 2: there is no capability record with id=capabilityId in the table
     */
    public int modifyCapability(int capabilityId,
                                long lastUpdateTime,
                                String newCapabilityString,
                                String newCapabilityInfo) {
        String modifyCapabilitySql = "update " + ModelValues.DAC_AL_CAPABILITY_TABLE_NAME + " set " +
                "lastUpdateTime=" + lastUpdateTime +
                ",capabilityString='" + newCapabilityString +
                "',capabilityInfo='" + newCapabilityInfo +
                "' where capabilityId=" + capabilityId + ";";
        try (
                Statement statement = connection.createStatement();
                ResultSet checkResultSet = statement.executeQuery("select * from " +
                        ModelValues.DAC_AL_CAPABILITY_TABLE_NAME +
                        " where capabilityId=" +
                        capabilityId + ";")
        ) {
            //Check if there is the capability record in the table.
            if (!checkResultSet.next()) {
                return 2;
            } else {
                return statement.executeUpdate(modifyCapabilitySql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }

    }

    /**
     * The method to query a capability record according capability's id
     *
     * @param capabilityId: capability's id
     * @return null: queried failed; Capability: new capability of subject
     */
    public Capability queryOneCapability(int capabilityId) {
        String queryOneCapabilityOfSubjectSql = "select * from " +
                ModelValues.DAC_AL_CAPABILITY_TABLE_NAME +
                " where capabilityId=" +
                capabilityId + ";";
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryOneCapabilityOfSubjectSql)
        ) {
            if (!resultSet.next()) {
                return null;
            } else {
                return new Capability(capabilityId,
                        resultSet.getInt("objectId"),
                        resultSet.getString("objectName"),
                        resultSet.getInt("grantedSubjectId"),
                        resultSet.getString("grantedSubjectName"),
                        resultSet.getInt("subjectId"),
                        resultSet.getString("subjectName"),
                        resultSet.getLong("createdTime"),
                        resultSet.getString("capabilityString"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * The method to query all record of capability in the table.
     *
     * @return ArrayList<Capability>
     */
    public ArrayList<Capability> queryAllCapabilities() {
        String queryAllCapabilityOfSubject = "select * from " + ModelValues.DAC_AL_CAPABILITY_TABLE_NAME + ";";
        ArrayList<Capability> resultCapabilityArrayList = new ArrayList<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryAllCapabilityOfSubject)
        ) {
            if (!resultSet.next()) {
                return resultCapabilityArrayList;
            } else {
                resultCapabilityArrayList.add(new Capability(resultSet.getInt("capabilityId"),
                        resultSet.getInt("objectId"),
                        resultSet.getString("objectName"),
                        resultSet.getInt("grantedSubjectId"),
                        resultSet.getString("grantedSubjectName"),
                        resultSet.getInt("subjectId"),
                        resultSet.getString("subjectName"),
                        resultSet.getLong("createdTime"),
                        resultSet.getString("capabilityString")));
                while (resultSet.next()) {
                    resultCapabilityArrayList.add(new Capability(resultSet.getInt("capabilityId"),
                            resultSet.getInt("objectId"),
                            resultSet.getString("objectName"),
                            resultSet.getInt("grantedSubjectId"),
                            resultSet.getString("grantedSubjectName"),
                            resultSet.getInt("subjectId"),
                            resultSet.getString("subjectName"),
                            resultSet.getLong("createdTime"),
                            resultSet.getString("capabilityString")));
                }
                return resultCapabilityArrayList;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return resultCapabilityArrayList;
        }
    }

    /**
     * The method to query capability by subject's id
     *
     * @param subjectId: subject's id
     * @return ArrayList of capabilities that had queried;
     */
    public ArrayList<Capability> queryCapabilitiesBySubjectId(int subjectId) {
        String queryOneCapabilityOfSubjectSql = "select * from " +
                ModelValues.DAC_AL_CAPABILITY_TABLE_NAME +
                " where subjectId=" + subjectId + ";";
        ArrayList<Capability> result = new ArrayList<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryOneCapabilityOfSubjectSql)
        ) {
            if (!resultSet.next()) {
                return result;
            } else {
                result.add(new Capability(resultSet.getInt("capabilityId"),
                        resultSet.getInt("objectId"),
                        resultSet.getString("objectName"),
                        resultSet.getInt("grantedSubjectId"),
                        resultSet.getString("grantedSubjectName"),
                        resultSet.getInt("subjectId"),
                        resultSet.getString("subjectName"),
                        resultSet.getLong("createdTime"),
                        resultSet.getString("capabilityString")));
                while (resultSet.next()) {
                    result.add(new Capability(resultSet.getInt("capabilityId"),
                            resultSet.getInt("objectId"),
                            resultSet.getString("objectName"),
                            resultSet.getInt("grantedSubjectId"),
                            resultSet.getString("grantedSubjectName"),
                            resultSet.getInt("subjectId"),
                            resultSet.getString("subjectName"),
                            resultSet.getLong("createdTime"),
                            resultSet.getString("capabilityString")));
                }
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return result;
        }
    }

    /**
     * The method query capabilities by subject's name
     *
     * @param subjectName: subject's name
     * @return ArrayList<Capability>
     */
    public ArrayList<Capability> queryCapabilitiesBySubjectName(String subjectName) {
        String queryCapabilitiesBySubjectNameSql = "select * from " + ModelValues.DAC_AL_CAPABILITY_TABLE_NAME +
                " where subjectName='" + subjectName + "';";
        ArrayList<Capability> result = new ArrayList<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryCapabilitiesBySubjectNameSql)
        ) {
            if (!resultSet.next()) {
                return result;
            } else {
                result.add(new Capability(resultSet.getInt("capabilityId"),
                        resultSet.getInt("objectId"),
                        resultSet.getString("objectName"),
                        resultSet.getInt("grantedSubjectId"),
                        resultSet.getString("grantedSubjectName"),
                        resultSet.getInt("subjectId"),
                        resultSet.getString("subjectName"),
                        resultSet.getLong("createdTime"),
                        resultSet.getString("capabilityString")));
                while (resultSet.next()) {
                    result.add(new Capability(resultSet.getInt("capabilityId"),
                            resultSet.getInt("objectId"),
                            resultSet.getString("objectName"),
                            resultSet.getInt("grantedSubjectId"),
                            resultSet.getString("grantedSubjectName"),
                            resultSet.getInt("subjectId"),
                            resultSet.getString("subjectName"),
                            resultSet.getLong("createdTime"),
                            resultSet.getString("capabilityString")));
                }
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return result;
        }
    }

    /**
     * The method to query capabilities by object's id
     *
     * @param objectId: object's id
     * @return ArrayList of capabilities that had queried
     */
    public ArrayList<Capability> queryCapabilitiesByObjectId(int objectId) {
        String queryOneCapabilityOfSubjectSql = "select * from " +
                ModelValues.DAC_AL_CAPABILITY_TABLE_NAME +
                " where objectId=" + objectId + ";";
        ArrayList<Capability> result = new ArrayList<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryOneCapabilityOfSubjectSql)
        ) {
            if (!resultSet.next()) {
                return result;
            } else {
                result.add(new Capability(resultSet.getInt("capabilityId"),
                        resultSet.getInt("objectId"),
                        resultSet.getString("objectName"),
                        resultSet.getInt("grantedSubjectId"),
                        resultSet.getString("grantedSubjectName"),
                        resultSet.getInt("subjectId"),
                        resultSet.getString("subjectName"),
                        resultSet.getLong("createdTime"),
                        resultSet.getString("capabilityString")));
                while (resultSet.next()) {
                    result.add(new Capability(resultSet.getInt("capabilityId"),
                            resultSet.getInt("objectId"),
                            resultSet.getString("objectName"),
                            resultSet.getInt("grantedSubjectId"),
                            resultSet.getString("grantedSubjectName"),
                            resultSet.getInt("subjectId"),
                            resultSet.getString("subjectName"),
                            resultSet.getLong("createdTime"),
                            resultSet.getString("capabilityString")));
                }
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return result;
        }
    }

    /**
     * The method to query capabilities by object's name
     *
     * @param objectName: object's name
     * @return ArrayList<Capability>
     */
    public ArrayList<Capability> queryCapabilitiesByObjectName(String objectName) {
        String queryCapabilitiesByObjectName = "select * from " + ModelValues.DAC_AL_CAPABILITY_TABLE_NAME +
                " where objectName='" + objectName + "';";
        ArrayList<Capability> result = new ArrayList<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryCapabilitiesByObjectName)
        ) {
            if (!resultSet.next()) {
                return result;
            } else {
                result.add(new Capability(resultSet.getInt("capabilityId"),
                        resultSet.getInt("objectId"),
                        resultSet.getString("objectName"),
                        resultSet.getInt("grantedSubjectId"),
                        resultSet.getString("grantedSubjectName"),
                        resultSet.getInt("subjectId"),
                        resultSet.getString("subjectName"),
                        resultSet.getLong("createdTime"),
                        resultSet.getString("capabilityString")));
                while (resultSet.next()) {
                    result.add(new Capability(resultSet.getInt("capabilityId"),
                            resultSet.getInt("objectId"),
                            resultSet.getString("objectName"),
                            resultSet.getInt("grantedSubjectId"),
                            resultSet.getString("grantedSubjectName"),
                            resultSet.getInt("subjectId"),
                            resultSet.getString("subjectName"),
                            resultSet.getLong("createdTime"),
                            resultSet.getString("capabilityString")));
                }
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return result;
        }
    }

    /**
     * The method to query capabilities by granted subject's id
     *
     * @param grantedSubjectId: granted subject's id
     * @return ArrayList of capabilities that had queried
     */
    public ArrayList<Capability> queryCapabilitiesByGrantedSubjectId(int grantedSubjectId) {
        String queryOneCapabilityOfSubjectSql = "select * from " +
                ModelValues.DAC_AL_CAPABILITY_TABLE_NAME +
                " where grantedSubjectId=" + grantedSubjectId + ";";
        ArrayList<Capability> result = new ArrayList<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryOneCapabilityOfSubjectSql)
        ) {
            if (!resultSet.next()) {
                return result;
            } else {
                result.add(new Capability(resultSet.getInt("capabilityId"),
                        resultSet.getInt("objectId"),
                        resultSet.getString("objectName"),
                        resultSet.getInt("grantedSubjectId"),
                        resultSet.getString("grantedSubjectName"),
                        resultSet.getInt("subjectId"),
                        resultSet.getString("subjectName"),
                        resultSet.getLong("createdTime"),
                        resultSet.getString("capabilityString")));
                while (resultSet.next()) {
                    result.add(new Capability(resultSet.getInt("capabilityId"),
                            resultSet.getInt("objectId"),
                            resultSet.getString("objectName"),
                            resultSet.getInt("grantedSubjectId"),
                            resultSet.getString("grantedSubjectName"),
                            resultSet.getInt("subjectId"),
                            resultSet.getString("subjectName"),
                            resultSet.getLong("createdTime"),
                            resultSet.getString("capabilityString")));
                }
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return result;
        }
    }

    /**
     * The method to query capabilities by granted subject's name
     *
     * @param grantedSubjectName: granted subject's name
     * @return ArrayList<Capability>
     */
    public ArrayList<Capability> queryCapabilitiesByGrantedSubjectName(String grantedSubjectName) {
        String queryCapabilitiesByGrantedSubjectNameSql = "select * from " +
                ModelValues.DAC_AL_CAPABILITY_TABLE_NAME +
                " where grantedSubjectName='" + grantedSubjectName + "';";
        ArrayList<Capability> result = new ArrayList<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryCapabilitiesByGrantedSubjectNameSql)
        ) {
            if (!resultSet.next()) {
                return result;
            } else {
                result.add(new Capability(resultSet.getInt("capabilityId"),
                        resultSet.getInt("objectId"),
                        resultSet.getString("objectName"),
                        resultSet.getInt("grantedSubjectId"),
                        resultSet.getString("grantedSubjectName"),
                        resultSet.getInt("subjectId"),
                        resultSet.getString("subjectName"),
                        resultSet.getLong("createdTime"),
                        resultSet.getString("capabilityString")));
                while (resultSet.next()) {
                    result.add(new Capability(resultSet.getInt("capabilityId"),
                            resultSet.getInt("objectId"),
                            resultSet.getString("objectName"),
                            resultSet.getInt("grantedSubjectId"),
                            resultSet.getString("grantedSubjectName"),
                            resultSet.getInt("subjectId"),
                            resultSet.getString("subjectName"),
                            resultSet.getLong("createdTime"),
                            resultSet.getString("capabilityString")));
                }
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return result;
        }
    }

    /**
     * The method to query capabilities by subject's and object's id
     *
     * @param subjectId: subject's id
     * @param objectId:  object's id
     * @return ArrayList of capabilities that had queried
     */
    public ArrayList<Capability> queryCapabilitiesBySubjectIdAndObjectId(int subjectId, int objectId) {
        String queryCapabilitiesBySubjectIdAndObjectIdSql = "select from " + ModelValues.DAC_AL_CAPABILITY_TABLE_NAME +
                " where subjectId=" + subjectId + " and objectId=" + objectId + ";";
        ArrayList<Capability> result = new ArrayList<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryCapabilitiesBySubjectIdAndObjectIdSql)
        ) {
            if (!resultSet.next()) {
                return result;
            } else {
                result.add(new Capability(resultSet.getInt("capabilityId"),
                        resultSet.getInt("objectId"),
                        resultSet.getString("objectName"),
                        resultSet.getInt("grantedSubjectId"),
                        resultSet.getString("grantedSubjectName"),
                        resultSet.getInt("subjectId"),
                        resultSet.getString("subjectName"),
                        resultSet.getLong("createdTime"),
                        resultSet.getString("capabilityString")));
                while (resultSet.next()) {
                    result.add(new Capability(resultSet.getInt("capabilityId"),
                            resultSet.getInt("objectId"),
                            resultSet.getString("objectName"),
                            resultSet.getInt("grantedSubjectId"),
                            resultSet.getString("grantedSubjectName"),
                            resultSet.getInt("subjectId"),
                            resultSet.getString("subjectName"),
                            resultSet.getLong("createdTime"),
                            resultSet.getString("capabilityString")));
                }
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return result;
        }
    }

    /**
     * The method to query capabilities by granted subject and object's id.
     *
     * @param grantedSubjectId: granted subject's id
     * @param objectId:         object's id
     * @return Succeed: ArrayList of capabilities that had queried; Failed: null
     */
    public ArrayList<Capability> queryCapabilitiesByGrantedSubjectIdAndObjectId(int grantedSubjectId, int objectId) {
        String queryCapabilitiesBySubjectIdAndObjectIdSql = "select from " + ModelValues.DAC_AL_CAPABILITY_TABLE_NAME +
                " where greantedSubjectId=" + grantedSubjectId + " and objectId=" + objectId + ";";
        ArrayList<Capability> result = new ArrayList<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryCapabilitiesBySubjectIdAndObjectIdSql)
        ) {
            if (!resultSet.next()) {
                return result;
            } else {
                result.add(new Capability(resultSet.getInt("capabilityId"),
                        resultSet.getInt("objectId"),
                        resultSet.getString("objectName"),
                        resultSet.getInt("grantedSubjectId"),
                        resultSet.getString("grantedSubjectName"),
                        resultSet.getInt("subjectId"),
                        resultSet.getString("subjectName"),
                        resultSet.getLong("createdTime"),
                        resultSet.getString("capabilityString")));
                while (resultSet.next()) {
                    result.add(new Capability(resultSet.getInt("capabilityId"),
                            resultSet.getInt("objectId"),
                            resultSet.getString("objectName"),
                            resultSet.getInt("grantedSubjectId"),
                            resultSet.getString("grantedSubjectName"),
                            resultSet.getInt("subjectId"),
                            resultSet.getString("subjectName"),
                            resultSet.getLong("createdTime"),
                            resultSet.getString("capabilityString")));
                }
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return result;
        }
    }


    /**
     * The method to query capabilities by granted subject and subject's id
     *
     * @param grantedSubjectId: granted subject's id
     * @param subjectId:        subject's id
     * @return Succeed: ArrayList of capabilities that had queried; Failed: null
     */
    public ArrayList<Capability> queryCapabilitiesByGrantedSubjectIdAndSubjectID(int grantedSubjectId, int subjectId) {
        String queryCapabilitiesBySubjectIdAndObjectIdSql = "select from " + ModelValues.DAC_AL_CAPABILITY_TABLE_NAME +
                " where grantedSubjectId=" + grantedSubjectId + " and subjectId=" + subjectId + ";";
        ArrayList<Capability> result = new ArrayList<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryCapabilitiesBySubjectIdAndObjectIdSql)
        ) {
            if (!resultSet.next()) {
                return result;
            } else {
                result.add(new Capability(resultSet.getInt("capabilityId"),
                        resultSet.getInt("objectId"),
                        resultSet.getString("objectName"),
                        resultSet.getInt("grantedSubjectId"),
                        resultSet.getString("grantedSubjectName"),
                        resultSet.getInt("subjectId"),
                        resultSet.getString("subjectName"),
                        resultSet.getLong("createdTime"),
                        resultSet.getString("capabilityString")));
                while (resultSet.next()) {
                    result.add(new Capability(resultSet.getInt("capabilityId"),
                            resultSet.getInt("objectId"),
                            resultSet.getString("objectName"),
                            resultSet.getInt("grantedSubjectId"),
                            resultSet.getString("grantedSubjectName"),
                            resultSet.getInt("subjectId"),
                            resultSet.getString("subjectName"),
                            resultSet.getLong("createdTime"),
                            resultSet.getString("capabilityString")));
                }
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return result;
        }
    }

    public ArrayList<Capability> queryCapabilitiesByGrantedSubjectNameAndSubjectName(String grantedSubecjtName,
                                                                                     String subjectName) {
        String queryCapabilitiesByGrantedSubjectNameAndSubjectNameSql = "select * from " +
                ModelValues.DAC_AL_CAPABILITY_TABLE_NAME +
                " where grantedSubjectName='" + grantedSubecjtName +
                "' and subjectName='" + subjectName + "';";
        ArrayList<Capability> result = new ArrayList<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryCapabilitiesByGrantedSubjectNameAndSubjectNameSql)
        ) {
            if (!resultSet.next()) {
                return result;
            } else {
                result.add(new Capability(resultSet.getInt("capabilityId"),
                        resultSet.getInt("objectId"),
                        resultSet.getString("objectName"),
                        resultSet.getInt("grantedSubjectId"),
                        resultSet.getString("grantedSubjectName"),
                        resultSet.getInt("subjectId"),
                        resultSet.getString("subjectName"),
                        resultSet.getLong("createdTime"),
                        resultSet.getString("capabilityString")));
                while (resultSet.next()) {
                    result.add(new Capability(resultSet.getInt("capabilityId"),
                            resultSet.getInt("objectId"),
                            resultSet.getString("objectName"),
                            resultSet.getInt("grantedSubjectId"),
                            resultSet.getString("grantedSubjectName"),
                            resultSet.getInt("subjectId"),
                            resultSet.getString("subjectName"),
                            resultSet.getLong("createdTime"),
                            resultSet.getString("capabilityString")));
                }
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return result;
        }
    }

    public ArrayList<Capability> queryCapabilitiesByGrantedSubjectIdAndSubjectIdAndObjectId(int grantedSubjectId,
                                                                                            int subjectId,
                                                                                            int objectId) {
        String queryCapabilitiesByGrantedSubjectIdAndSubjectIdAndObjectIdSql = "select * from " +
                ModelValues.DAC_AL_CAPABILITY_TABLE_NAME +
                " where grantedSubjectId=" + grantedSubjectId +
                " and subjectId=" + subjectId +
                " and objectId=" + objectId + ";";
        ArrayList<Capability> result = new ArrayList<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryCapabilitiesByGrantedSubjectIdAndSubjectIdAndObjectIdSql)
        ) {
            if (!resultSet.next()) {
                return result;
            } else {
                result.add(new Capability(resultSet.getInt("capabilityId"),
                        resultSet.getInt("objectId"),
                        resultSet.getString("objectName"),
                        resultSet.getInt("grantedSubjectId"),
                        resultSet.getString("grantedSubjectName"),
                        resultSet.getInt("subjectId"),
                        resultSet.getString("subjectName"),
                        resultSet.getLong("createdTime"),
                        resultSet.getString("capabilityString")));
                while (resultSet.next()) {
                    result.add(new Capability(resultSet.getInt("capabilityId"),
                            resultSet.getInt("objectId"),
                            resultSet.getString("objectName"),
                            resultSet.getInt("grantedSubjectId"),
                            resultSet.getString("grantedSubjectName"),
                            resultSet.getInt("subjectId"),
                            resultSet.getString("subjectName"),
                            resultSet.getLong("createdTime"),
                            resultSet.getString("capabilityString")));
                }
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return result;
        }
    }

    public ArrayList<Capability> queryCapabilitiesByGrantedSubjectNameAndSubjectNameAndObjectName(String grantedSubjectName,
                                                                                                  String subjectName,
                                                                                                  String objectName) {
        String queryCapabilitiesByGrantedSubjectNameAndSubjectNameAndObjectNameSql = "select * from " +
                ModelValues.DAC_AL_CAPABILITY_TABLE_NAME +
                " where grantedSubjectName='" + grantedSubjectName +
                "' and subjectName='" + subjectName +
                "' and objectName='" + objectName + "';";
        ArrayList<Capability> result = new ArrayList<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryCapabilitiesByGrantedSubjectNameAndSubjectNameAndObjectNameSql)
        ) {
            if (!resultSet.next()) {
                return result;
            } else {
                result.add(new Capability(resultSet.getInt("capabilityId"),
                        resultSet.getInt("objectId"),
                        resultSet.getString("objectName"),
                        resultSet.getInt("grantedSubjectId"),
                        resultSet.getString("grantedSubjectName"),
                        resultSet.getInt("subjectId"),
                        resultSet.getString("subjectName"),
                        resultSet.getLong("createdTime"),
                        resultSet.getString("capabilityString")));
                while (resultSet.next()) {
                    result.add(new Capability(resultSet.getInt("capabilityId"),
                            resultSet.getInt("objectId"),
                            resultSet.getString("objectName"),
                            resultSet.getInt("grantedSubjectId"),
                            resultSet.getString("grantedSubjectName"),
                            resultSet.getInt("subjectId"),
                            resultSet.getString("subjectName"),
                            resultSet.getLong("createdTime"),
                            resultSet.getString("capabilityString")));
                }
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return result;
        }
    }

    /**
     * The method to add one black token.
     *
     * @param blackTokenId:     blackTokenId
     * @param objectId:         object' id
     * @param grantedSubjectId: granted subject's id
     * @param subjectId:        subject's id
     * @param createdTime:      created time
     * @param lastUpdateTime:   last update time
     * @param capabilityString: capability string
     * @param blackToken:       black token true or false
     * @return 0: added failed; >0: added succeed
     */
    public int addBlackToken(int blackTokenId,
                             int objectId,
                             int grantedSubjectId,
                             int subjectId,
                             long createdTime,
                             long lastUpdateTime,
                             String capabilityString,
                             boolean blackToken) {
        String addBlackTokenSql = "insert into " + ModelValues.DAC_AL_BLACK_TOKEN_TABLE_NAME +
                "(blackTokenId,objectId,grantedSubjectId,subjectId,createdTime,lastUpdateTime,capabilityString,blackToken) " +
                "values('" + blackTokenId + "'," +
                "'" + objectId + "'," +
                "'" + grantedSubjectId + "'," +
                "'" + subjectId + "'," +
                "'" + createdTime + "'," +
                "'" + lastUpdateTime + "'," +
                "'" + capabilityString + "'," +
                "'" + blackToken + "'" +
                ")";
        try (
                Statement statement = connection.createStatement();
                ResultSet checkResultSet = statement.executeQuery("show tables like " + ModelValues.DAC_AL_BLACK_TOKEN_TABLE_NAME + ";")
        ) {
            if (!checkResultSet.next()) {
                statement.executeUpdate(ModelValues.DAC_AL_BLACK_TOKEN_TABLE_CREATE_SQL);
            }
            return statement.executeUpdate(addBlackTokenSql);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * The method delete a black token record by black token's id.
     *
     * @param blackTokenId: black token's id
     * @return 0: deleted failed; >0:deleted succeed
     */
    public int deleteBlackToken(int blackTokenId) {
        String deleteBlackTokenSql = "delete from " + ModelValues.DAC_AL_BLACK_TOKEN_TABLE_NAME +
                " where blackTokenId=" + blackTokenId + ";";
        try (
                Statement statement = connection.createStatement()
        ) {
            return statement.executeUpdate(deleteBlackTokenSql);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * The method to modify a black token.
     *
     * @param blackTokenId:     black token's id
     * @param capabilityString: capability string
     * @param blackToken:       black token true or false
     * @return 0: modified failed; >0: modified succeed
     */
    public int modifyBlackToken(int blackTokenId, String capabilityString, boolean blackToken) {
        String modifyBlackTokenSql = "update " + ModelValues.DAC_AL_BLACK_TOKEN_TABLE_NAME + " set " +
                "capabilityString='" + capabilityString +
                "',blackToken=" + blackToken +
                " where blackTokenId=" + blackTokenId + ";";
        try (
                Statement statement = connection.createStatement()
        ) {
            return statement.executeUpdate(modifyBlackTokenSql);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * The method to query one black token by id
     *
     * @param blackTokenId: black token'id
     * @return Succeed: a black token; Failed: null
     */
    public BlackToken queryOneBlackToken(int blackTokenId) {
        String queryOneBlackTokenSql = "select * from " + ModelValues.DAC_AL_BLACK_TOKEN_TABLE_NAME +
                " where blackId=" + blackTokenId + ";";
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryOneBlackTokenSql)
        ) {
            if (!resultSet.next()) {
                return null;
            } else {
                return new BlackToken(resultSet.getInt("blackTokenId"),
                        resultSet.getInt("objectId"),
                        resultSet.getInt("grantedSubjectId"),
                        resultSet.getInt("subjectId"),
                        resultSet.getLong("createdTime"),
                        resultSet.getString("capabilityString"),
                        resultSet.getBoolean("blackToken"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * The method to query all black token record.
     *
     * @return Succeed: ArrayList of all black tokens
     */
    public ArrayList<BlackToken> queryAllBlackTokens() {
        String queryAllBlackTokensSql = "select * from " + ModelValues.DAC_AL_BLACK_TOKEN_TABLE_NAME + ";";
        ArrayList<BlackToken> result = new ArrayList<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryAllBlackTokensSql)
        ) {
            if (!resultSet.next()) {
                return result;
            } else {

                result.add(new BlackToken(resultSet.getInt("blackTokenId"),
                        resultSet.getInt("objectId"),
                        resultSet.getInt("grantedSubjectId"),
                        resultSet.getInt("subjectId"),
                        resultSet.getLong("createdTime"),
                        resultSet.getString("capabilityString"),
                        resultSet.getBoolean("blackToken")));
                while (resultSet.next()) {
                    result.add(new BlackToken(resultSet.getInt("blackTokenId"),
                            resultSet.getInt("objectId"),
                            resultSet.getInt("grantedSubjectId"),
                            resultSet.getInt("subjectId"),
                            resultSet.getLong("createdTime"),
                            resultSet.getString("capabilityString"),
                            resultSet.getBoolean("blackToken")));
                }
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return result;
        }
    }

    /**
     * The method to query black token by object'id, granted subject's id, subject's id and capabilityString
     *
     * @param objectId:         object's id
     * @param grantedSubjectId: granted subject's id
     * @param subjectId:        subject's id
     * @param capabilityString: capability string
     * @return Succeed: A black token; Failed: null
     */
    public BlackToken queryBlackTokenByObjectIdGrantedSubjectIdSubjectId(int objectId,
                                                                         int grantedSubjectId,
                                                                         int subjectId,
                                                                         String capabilityString) {
        String queryBlackTokenByObjectIdGrantedSubjectIdSubjectIdSql = "select * from " +
                ModelValues.DAC_AL_BLACK_TOKEN_TABLE_NAME + " where objectId=" + objectId +
                " and grantedSubjectId=" + grantedSubjectId +
                " and subjectId=" + subjectId +
                " and capabilityString='" + capabilityString + "';";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(queryBlackTokenByObjectIdGrantedSubjectIdSubjectIdSql)) {
            if (!resultSet.next()) {
                return null;
            } else {
                return new BlackToken(resultSet.getInt("blackTokenId"),
                        resultSet.getInt("objectId"),
                        resultSet.getInt("grantedSubjectId"),
                        resultSet.getInt("subjectId"),
                        resultSet.getLong("createdTime"),
                        resultSet.getString("capabilityString"),
                        resultSet.getBoolean("blackToken"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * The method to check if subject have control of object.
     *
     * @param subjectId: subject's id
     * @param objectId:  object's id
     * @return true or false
     */
    public boolean ifSubjectHaveControlOfObject(int subjectId, int objectId) {
        String ifSubjectHaveControlOfObjectSql = "select * from " + ModelValues.DAC_AL_CAPABILITY_TABLE_NAME +
                " where subjectId=" + subjectId +
                " and objectId=" + objectId +
                " and capabilityString like " + "\"___c_\"";
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(ifSubjectHaveControlOfObjectSql)
        ) {
            if (resultSet.next()) {
                return true;
            } else
                return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * The method to check if subject have capability string of object
     *
     * @param subjectId:        subject's id
     * @param objectId:         object's id
     * @param capabilityString: capability string
     * @return true: yes; false: no
     */
    public boolean ifSubjectHaveCapabilitiesOfObject(int subjectId, int objectId, String capabilityString) {
        String ifSubjectHaveCapabilitiesOfObjectSql = "select * from " + ModelValues.DAC_AL_CAPABILITY_TABLE_NAME +
                " where subjectId=" + subjectId +
                " and objectId=" + objectId +
                " and capabilityString='" + capabilityString + "';";
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(ifSubjectHaveCapabilitiesOfObjectSql)
        ) {
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * The method to query if there is cyclical capability between granted subject and subject on object's control
     *
     * @param grantedSubjectId: granted subject's id
     * @param subjectId:        subject's id
     * @param object:           object
     * @return true: cyclical capability; false: no cyclical capability
     */
    public boolean ifCyclicalCapability(int grantedSubjectId, int subjectId, ACLObject object) {
        try (Statement statement = connection.createStatement()) {
            return ifCyclicalCapability(grantedSubjectId, subjectId, object, statement);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    private boolean ifCyclicalCapability(int grantedSubjectId, int subjectId, ACLObject object, Statement statement) {
        String ifCyclicalCapabilitySql = "select * from " + ModelValues.DAC_AL_CAPABILITY_TABLE_NAME +
                " where subjectId=" + grantedSubjectId +
                " and objectId=" + object.getId() +
                " and capabilityString like " + "\"___c_\"";
        try (ResultSet resultSet = statement.executeQuery(ifCyclicalCapabilitySql)) {
            while (resultSet.next()) {
                if (resultSet.getInt("grantedSubjectId") == subjectId) {
                    return true;
                } else if (object.getCreatedSubjectId() == resultSet.getInt("grantedSubjectId")) {
                    return false;
                }
                return ifCyclicalCapability(resultSet.getInt("grantedSubjectId"), subjectId, object, statement);
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }


    /*****************************************************************************************************************
     *
     * This following methods to operate rbac policy's database record.
     */

    /**
     * The method to add user record.
     *
     * @param userName:    user name
     * @param password:    password
     * @param userInfo:    user info
     * @param createdTime: created time
     * @return 0: added failed; 1: added succeed
     */
    public int addUser(String userName, String password, String userInfo, long createdTime) {
        String addUserSql = "insert into " + ModelValues.RBAC_USER_TABLE_NAME +
                "(userName,password,userInfo,createdTime) values('" +
                userName + "','" +
                password + "','" +
                userInfo + "','" +
                createdTime + "');";
        try (
                Statement statement = connection.createStatement()
        ) {
            return statement.executeUpdate(addUserSql);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * The method to delete user record by user name
     *
     * @param userName: user name
     * @return 0: deleted failed 1: deleted succeed
     */
    public int deleteUser(String userName) {
        String deleteUserSql = "delete from " + ModelValues.RBAC_USER_TABLE_NAME +
                " where userName='" + userName + "';";
        try (
                Statement statement = connection.createStatement()
        ) {
            return statement.executeUpdate(deleteUserSql);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * The method to query User by user name
     *
     * @param userName: user name
     * @return Failed: null; Succeed: User
     */
    public User queryUser(String userName) {
        String queryUserSql = "select * from " + ModelValues.RBAC_USER_TABLE_NAME +
                " where userName='" + userName + "';";
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryUserSql)
        ) {
            if (resultSet.next()) {
                return new User(resultSet.getString("userName"),
                        resultSet.getString("password"),
                        resultSet.getLong("createdTime"));
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * The method to query all users
     *
     * @return ArrayList<User>
     */
    public ArrayList<User> queryAllUser() {
        ArrayList<User> allUser = new ArrayList<>();
        String queryAllUserSql = "select * from " + ModelValues.RBAC_USER_TABLE_NAME + ";";
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryAllUserSql)
        ) {
            while (resultSet.next()) {
                allUser.add(new User(resultSet.getString("userName"),
                        resultSet.getString("password"),
                        resultSet.getLong("createdTime")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return allUser;
        }
        return allUser;
    }

    /**
     * The method to add role record
     *
     * @param roleName:    role name
     * @param roleInfo:    role info
     * @param createdTime: created time
     * @return 0: added failed; 1: added succeed
     */
    public int addRole(String roleName, String roleInfo, long createdTime) {
        String addRoleSql = "insert into " + ModelValues.RBAC_ROLE_TABLE_NAME +
                "(roleName,roleInfo,createdTime) values('" +
                roleName + "','" +
                roleInfo + "','" +
                createdTime + "');";
        try (
                Statement statement = connection.createStatement()
        ) {
            return statement.executeUpdate(addRoleSql);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * The method to delete role record by role name
     *
     * @param roleName: role name
     * @return 0: deleted failed; 1: deleted succeed
     */
    public int deleteRole(String roleName) {
        String deleteRoleSql = "delete from " + ModelValues.RBAC_ROLE_TABLE_NAME +
                " where roleName='" + roleName + "';";
        try (
                Statement statement = connection.createStatement()
        ) {
            return statement.executeUpdate(deleteRoleSql);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * The method to query role by role name
     *
     * @param roleName: role name
     * @return Succeed: role; Failed: null
     */
    public Role queryRole(String roleName) {
        String queryRoleSql = "select * from " + ModelValues.RBAC_ROLE_TABLE_NAME +
                " where roleName='" + roleName + "';";
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryRoleSql)
        ) {
            if (resultSet.next()) {
                return new Role(resultSet.getString("roleName"),
                        resultSet.getLong("createdTime"));
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * The method to query all roles
     *
     * @return ArrayList<Role>
     */
    public ArrayList<Role> queryAllRole() {
        ArrayList<Role> allRoles = new ArrayList<>();
        String queryAllRoleSql = "select * from " + ModelValues.RBAC_ROLE_TABLE_NAME + ";";
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryAllRoleSql)
        ) {
            while (resultSet.next()) {
                allRoles.add(new Role(resultSet.getString("roleName"),
                        resultSet.getLong("createdName")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return allRoles;
        }
        return allRoles;
    }

    /**
     * The method to add permission record
     *
     * @param permissionName: permission name
     * @param permissionInfo: permission info
     * @param createdTime:    created Time
     * @return 0: added failed; 1: added succeed
     */
    public int addPermission(String permissionName, String permissionInfo, long createdTime) {
        String addPermissionSql = "insert into " + ModelValues.RBAC_PERMISSION_TABLE_NAME +
                "(permissionName,permissionInfo,createdTime) values('" +
                permissionName + "','" +
                permissionInfo + "','" +
                createdTime + "');";
        try (
                Statement statement = connection.createStatement()
        ) {
            return statement.executeUpdate(addPermissionSql);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * The method to delete permission by permission name
     *
     * @param permissionName: permission name
     * @return 0:deleted failed; 1: deleted succeed
     */
    public int deletePermission(String permissionName) {
        String deletePermissionSql = "delete from " + ModelValues.RBAC_PERMISSION_TABLE_NAME +
                " where permissionName='" + permissionName + "';";
        try (
                Statement statement = connection.createStatement()
        ) {
            return statement.executeUpdate(deletePermissionSql);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * The method to query permission by permission name.
     *
     * @param permissionName: permission name
     * @return Succeed: permission; Failed: null
     */
    public Permission queryPermission(String permissionName) {
        String queryPermissionSql = "select * from " + ModelValues.RBAC_PERMISSION_TABLE_NAME +
                " where permissionName='" + permissionName + "';";
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryPermissionSql)
        ) {
            if (resultSet.next()) {
                return new Permission(resultSet.getString("permissionName"),
                        resultSet.getLong("createdTime"));
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * The method to query all permission.
     *
     * @return ArrayList<Permission>
     */
    public ArrayList<Permission> queryAllPermission() {
        ArrayList<Permission> allPermissions = new ArrayList<>();
        String queryAllPermissionSql = "select * from " + ModelValues.RBAC_PERMISSION_TABLE_NAME + ";";
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryAllPermissionSql)
        ) {
            while (resultSet.next()) {
                allPermissions.add(new Permission(resultSet.getString("permissionName"),
                        resultSet.getLong("createdTime")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return allPermissions;
        }
        return allPermissions;
    }

    /**
     * The method to add URA record
     *
     * @param username: user name
     * @param roleName: role name
     * @return 0: added failed; 1: added succeed
     */
    public int addURA(String username, String roleName) {
        String addURASql = "insert into " + ModelValues.RBAC_URA_TABLE_NAME +
                "(userName, roleName) values('" +
                username + "','" +
                roleName + "');";
        try (
                Statement statement = connection.createStatement()
        ) {
            return statement.executeUpdate(addURASql);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * The method to delete URA record by user name and role name
     *
     * @param userName: user name
     * @param roleName: role name
     * @return 0:deleted failed; 1: deleted succeed
     */
    public int deleteURA(String userName, String roleName) {
        String deleteURASql = "delete from " + ModelValues.RBAC_URA_TABLE_NAME +
                " where userName='" + userName +
                "' and roleName='" + roleName + "';";
        try (
                Statement statement = connection.createStatement()
        ) {
            return statement.executeUpdate(deleteURASql);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * The method to delete URA record by user name
     *
     * @param userName: user name
     * @return 0: deleted failed; >0: deleted succeed
     */
    public int deleteURAByUserName(String userName) {
        String deleteURAByUserNameSql = "delete from " + ModelValues.RBAC_URA_TABLE_NAME +
                " where userName='" + userName + "';";
        try (
                Statement statement = connection.createStatement()
        ) {
            return statement.executeUpdate(deleteURAByUserNameSql);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * The method to delete URA record by role name
     *
     * @param roleName: role name
     * @return 0: deleted failed; >0: deleted succeed
     */
    public int deleteURAByRoleName(String roleName) {
        String deleteURAByRoleNameSql =
                "delete from " + ModelValues.RBAC_URA_TABLE_NAME +
                        " where roleName='" + roleName + "';";
        try (
                Statement statement = connection.createStatement()
        ) {
            return statement.executeUpdate(deleteURAByRoleNameSql);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * The method to query URA by user name and role name
     *
     * @param userName: user name
     * @param roleName: role name
     * @return Succeed: URA; Failed:null
     */
    public URA queryURA(String userName, String roleName) {
        String queryURASql = "select * from " + ModelValues.RBAC_URA_TABLE_NAME +
                " where userName='" + userName +
                "' and roleName='" + roleName + "';";
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryURASql)
        ) {
            if (resultSet.next()) {
                return new URA(resultSet.getString("userName"),
                        resultSet.getString("roleName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * The method to query all URA.
     *
     * @return ArrayList<URA>
     */
    public ArrayList<URA> queryAllURA() {
        String queryAllURASql = "select * from " + ModelValues.RBAC_URA_TABLE_NAME + ";";
        ArrayList<URA> allURA = new ArrayList<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryAllURASql)
        ) {
            while (resultSet.next()) {
                allURA.add(new URA(resultSet.getString("userName"),
                        resultSet.getString("roleName")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return allURA;
        }
        return allURA;
    }

    /**
     * The method to query URA by user name.
     *
     * @param userName: user name
     * @return ArrayList<URA>
     */
    public ArrayList<URA> queryURAByUserName(String userName) {
        String queryURAByUserNameSql = "select * from " + ModelValues.RBAC_URA_TABLE_NAME +
                " where userName='" + userName + "';";
        ArrayList<URA> allURA = new ArrayList<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryURAByUserNameSql)
        ) {
            while (resultSet.next()) {
                allURA.add(new URA(resultSet.getString("userName"),
                        resultSet.getString("roleName")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return allURA;
        }
        return allURA;
    }

    /**
     * The method to query URA by role name
     *
     * @param roleName: role name
     * @return ArrayList<URA>
     */
    public ArrayList<URA> queryURAByRoleName(String roleName) {
        String queryURAByRoleNameSql = "select * from " + ModelValues.RBAC_URA_TABLE_NAME +
                " where roleName='" + roleName + "';";
        ArrayList<URA> allURA = new ArrayList<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryURAByRoleNameSql)
        ) {
            while (resultSet.next()) {
                allURA.add(new URA(resultSet.getString("userName"),
                        resultSet.getString("roleName")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return allURA;
        }
        return allURA;
    }

    /**
     * The method to add PRA record
     *
     * @param roleName:       role name
     * @param permissionName: permission name
     * @return 0: added failed; 1: added succeed
     */
    public int addPRA(String roleName, String permissionName) {
        String addPRASql = "insert into " + ModelValues.RBAC_PRA_TABLE_NAME +
                "(roleName,permissionName) values('" +
                roleName + "','" +
                permissionName + "');";
        try (
                Statement statement = connection.createStatement()
        ) {
            return statement.executeUpdate(addPRASql);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * The method to delete PRA record by role name and permission name
     *
     * @param roleName:       role name
     * @param permissionName: permission name
     * @return 0: deleted failed; 1: deleted succeed
     */
    public int deletePRA(String roleName, String permissionName) {
        String deletePRASql = "delete from " + ModelValues.RBAC_PRA_TABLE_NAME +
                " where roleName='" + roleName +
                "' and permissionName='" + permissionName + "';";
        try (
                Statement statement = connection.createStatement()
        ) {
            return statement.executeUpdate(deletePRASql);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * The method to delete PRA record by role name
     *
     * @param roleName: role name
     * @return 0: deleted failed; >0: deleted succeed
     */
    public int deletePRAByRoleName(String roleName) {
        String deletePRASql = "delete from " + ModelValues.RBAC_PRA_TABLE_NAME +
                " where roleName='" + roleName + "';";
        try (
                Statement statement = connection.createStatement()
        ) {
            return statement.executeUpdate(deletePRASql);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * The method to delete PRA record by permission name
     *
     * @param permissionName: permission name
     * @return 0: deleted failed; >0: deleted succeed
     */
    public int deletePRAByPermissionName(String permissionName) {
        String deletePRASql = "delete from " + ModelValues.RBAC_PRA_TABLE_NAME +
                " where permissionName='" + permissionName + "';";
        try (
                Statement statement = connection.createStatement()
        ) {
            return statement.executeUpdate(deletePRASql);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * The method to query PRA by role name adn permission name
     *
     * @param roleName:       role name
     * @param permissionName: permission name
     * @return Succeed: PRA; Failed: null
     */
    public PRA queryPRA(String roleName, String permissionName) {
        String queryPRASql = "select * from " + ModelValues.RBAC_PRA_TABLE_NAME +
                " where roleName='" + roleName +
                "' and permissionName='" + permissionName + "';";
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryPRASql)

        ) {
            if (resultSet.next()) {
                return new PRA(resultSet.getString("roleName"),
                        resultSet.getString("permissionName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * The method to query all PRA.
     *
     * @return ArrayList<PRA>
     */
    public ArrayList<PRA> queryAllPRA() {
        String queryAllPRASql = "select * from " + ModelValues.RBAC_PRA_TABLE_NAME + ";";
        ArrayList<PRA> allPRA = new ArrayList<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryAllPRASql)
        ) {
            while (resultSet.next()) {
                allPRA.add(new PRA(resultSet.getString("roleName"),
                        resultSet.getString("permissionName")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return allPRA;
        }
        return allPRA;

    }

    /**
     * The method to query PRA by role name.
     *
     * @param roleName: role name
     * @return ArrayList<PRA>
     */
    public ArrayList<PRA> queryPRAByRoleName(String roleName) {
        String queryPRAByRoleNameSql = "select * from " + ModelValues.RBAC_PRA_TABLE_NAME +
                " where roleName='" + roleName + "';";
        ArrayList<PRA> allPRA = new ArrayList<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryPRAByRoleNameSql)
        ) {
            while (resultSet.next()) {
                allPRA.add(new PRA(resultSet.getString("roleName"),
                        resultSet.getString("permissionName")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return allPRA;
        }
        return allPRA;
    }

    /**
     * The method to query PRA by permission name.
     *
     * @param permissionName: permission name
     * @return ArrayList<PRA>
     */
    public ArrayList<PRA> queryPRAByPermissoinName(String permissionName) {
        String queryPRAByPermissionNameSql = "select * from " + ModelValues.RBAC_PRA_TABLE_NAME +
                " where permissionName='" + permissionName + "';";
        ArrayList<PRA> allPRA = new ArrayList<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryPRAByPermissionNameSql)
        ) {
            while (resultSet.next()) {
                allPRA.add(new PRA(resultSet.getString("roleName"),
                        resultSet.getString("permissionName")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return allPRA;
        }
        return allPRA;
    }

    /**
     * The method to add RRA record
     *
     * @param fatherRoleName:   father role name
     * @param childrenRoleName: children role name
     * @return 0: added failed; 1: added succeed
     */
    public int addRRA(String fatherRoleName, String childrenRoleName) {
        String addRRASql = "insert into " + ModelValues.RBAC_RRA_TABLE_NAME +
                "(fatherRoleName,childrenRoleName) values('" +
                fatherRoleName + "','" +
                childrenRoleName + "');";
        try (
                Statement statement = connection.createStatement()
        ) {
            return statement.executeUpdate(addRRASql);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * The method to delete RRA record by father role name and children role name
     *
     * @param fatherRoleName:   father role name
     * @param childrenRoleName: children role name
     * @return 0: deleted failed; 1: deleted succeed
     */
    public int deleteRRA(String fatherRoleName, String childrenRoleName) {
        String deleteRRASql = "delete from " + ModelValues.RBAC_RRA_TABLE_NAME +
                " where fatherRoleName='" + fatherRoleName +
                "' and childrenRoleName='" + childrenRoleName + "';";
        try (
                Statement statement = connection.createStatement()
        ) {
            return statement.executeUpdate(deleteRRASql);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * The method to delete RRA record by father role name
     *
     * @param fatherRoleName: father name
     * @return 0: deleted failed; >0: deleted succeed
     */
    public int deleteRRAByFatherRoleName(String fatherRoleName) {
        String deleteRRAByFatherRoleNameSql = "delete from " + ModelValues.RBAC_RRA_TABLE_NAME +
                " where fatherRoleName='" + fatherRoleName + "';";
        try (
                Statement statement = connection.createStatement()
        ) {
            return statement.executeUpdate(deleteRRAByFatherRoleNameSql);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * The method to delete RRA record by children role name
     *
     * @param childrenRoleName: children role name
     * @return 0: deleted failed; >0: deleted succeed
     */
    public int deleteRRAByChildrenRoleName(String childrenRoleName) {
        String deleteRRAByChildrenRoleNameSql = "delete from " + ModelValues.RBAC_RRA_TABLE_NAME +
                " where childrenRoleName='" + childrenRoleName + "';";
        try (
                Statement statement = connection.createStatement()
        ) {
            return statement.executeUpdate(deleteRRAByChildrenRoleNameSql);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * The method to query RRA by father role name and children role name
     *
     * @param fatherRoleName:  father role name
     * @param childrenRoleName children role name
     * @return Succeed: RRA; Failed: null
     */
    public RRA queryRRA(String fatherRoleName, String childrenRoleName) {
        String queryRRASql = "select * form " + ModelValues.RBAC_RRA_TABLE_NAME +
                " where fatherRoleName='" + fatherRoleName +
                "' and childrenRoleName='" + childrenRoleName + "';";
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryRRASql)
        ) {
            if (resultSet.next()) {
                return new RRA(resultSet.getString("fatherRoleName"),
                        resultSet.getNString("childrenRoleName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * The method to query all RRA.
     *
     * @return ArrayList<RRA>
     */
    public ArrayList<RRA> queryAllRRA() {
        String queryAllRRASql = "select * from " + ModelValues.RBAC_RRA_TABLE_NAME + ";";
        ArrayList<RRA> allRRA = new ArrayList<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryAllRRASql)
        ) {
            while (resultSet.next()) {
                allRRA.add(new RRA(resultSet.getString("fatherRoleName"),
                        resultSet.getString("childrenRoleName")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return allRRA;
        }
        return allRRA;
    }

    /**
     * The method to query RRA by father role name
     *
     * @param fatherRoleName: father role name
     * @return ArrayList<RRA>
     */
    public ArrayList<RRA> queryRRAByFatherRoleName(String fatherRoleName) {
        String queryRRAByFatherRoleNameSql = "select * from " + ModelValues.RBAC_RRA_TABLE_NAME +
                " where fatherRoleName='" + fatherRoleName + "';";
        ArrayList<RRA> allRRA = new ArrayList<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryRRAByFatherRoleNameSql)
        ) {
            while (resultSet.next()) {
                allRRA.add(new RRA(resultSet.getString("fatherRoleName"),
                        resultSet.getString("childrenRoleName")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return allRRA;
        }
        return allRRA;
    }

    /**
     * The method to query RRA by children role name.
     *
     * @param childrenRoleName: children role name
     * @return PRA whit children role named childrenRoleName Failed:null
     */
    public RRA queryRRAByChildrenRoleName(String childrenRoleName) {
        String queryRRAByChildrenRoleNameSql = "select * from " + ModelValues.RBAC_RRA_TABLE_NAME +
                " where childrenRoleName='" + childrenRoleName + "';";
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryRRAByChildrenRoleNameSql)
        ) {
            new RRA(resultSet.getString("fatherRoleName"),
                    resultSet.getString("childrenRoleName"));


        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }


}
