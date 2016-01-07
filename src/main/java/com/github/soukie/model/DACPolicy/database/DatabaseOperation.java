package com.github.soukie.model.DACPolicy.database;

import com.github.soukie.model.DACPolicy.objects.ACLObject;
import com.github.soukie.model.DACPolicy.objects.ACLSubject;
import com.github.soukie.model.DACPolicy.objects.CapabilityOfSubject;
import com.github.soukie.model.ModelValues;
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
    private String operationTime;
    private String DatabaseDriverPath;

    private Connection connection;

    public static final int QUERY_CAPABILITIES_MOD_SUBJECT_ID = 0;
    public static final int QUERY_CAPABILITIES_MOD_OBJECT_ID = 1;
    public static final int QUERY_CAPABILITIES_MOD_GRANTED_SUBJECT_ID = 2;
    public static final int QUERY_CAPABILITIES_MOD_DEFAULT_CAPABILITY_ID = 4;


    public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";


    public DatabaseOperation(String operationTime) {
        this.operationTime = operationTime;
        this.DatabaseDriverPath = MYSQL_DRIVER;
    }

    public DatabaseOperation(String operationTime, String mySQLDriverPath) {
        this.operationTime = operationTime;
        this.DatabaseDriverPath = mySQLDriverPath;
    }

    /**
     * The method to change operations' connection.
     *
     * @param newConnection: new connection.
     */
    public void changeConnection(@NotNull Connection newConnection) {
        this.connection = newConnection;
    }

    public String getMySQLDriverPath() {
        return DatabaseDriverPath;
    }

    public String getOperationTime() {
        return operationTime;
    }

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

    public void initDatabaseConnection(String url, String user, String pass) throws ClassNotFoundException {
        Class.forName(DatabaseDriverPath);
        try {
            connection = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    /**
     * The following method are adding or modifying subject operations.
     * This methods will change database record, and this operations can not support
     * multi-thread. So you must achieve multi-thread by yourselves' code.
     */

    /**
     * The method to add a subject in database.
     *
     * @param sId:         subject's id
     * @param sName:       subject's name
     * @param sPassword:   subject's password
     * @param subjectInfo: subject's information
     * @return return int 0: add subject error 1: add one subject succeed
     */

    public int addSubject(int sId,
                          String sName,
                          String sPassword,
                          String subjectInfo) {
        String addSubjectRecordSql = "insert into " +
                ModelValues.DAC_SUBJECT_TABLE_NAME +
                "(sId,sName,sPassword,subjectInfo) values('" +
                sId + "','" +
                sName + "','" +
                sPassword + "','" +
                subjectInfo + "');";

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
     * @param sId: the id of subject that will be deleted
     * @return return int 0: operated failed; 1: operated succeed; 2: there is not the record as id = sId in the table
     */

    public int deleteSubject(int sId) {
        String deleteSubjectSql = "delete from +" + ModelValues.DAC_SUBJECT_TABLE_NAME + " where sId=" + sId + ";";
        try (
                Statement statement = connection.createStatement();
                ResultSet checkResultSet = statement.executeQuery("select * from " + ModelValues.DAC_SUBJECT_TABLE_NAME + " where sId=" + sId + ";")
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
     * @param sId:         subject's id
     * @param sName:       subject's name
     * @param sPassword:   subject's password
     * @param subjectInfo: subject's information
     * @return 0: operated failed; 1: operated succeed 2: there is no the record as id = sId in the table
     */
    public int modifySubject(int sId, String sName, String sPassword, String subjectInfo) {
        String modifySubjectSql = "update " + ModelValues.DAC_SUBJECT_TABLE_NAME + " set " +
                "sName=" + sName +
                ",sPassword=" + sPassword +
                ",subjectInfo=" + subjectInfo +
                " where sId=" + sId + ";";
        try (
                Statement statement = connection.createStatement();
                ResultSet checkResultSet = statement.executeQuery("select * from " + ModelValues.DAC_SUBJECT_TABLE_NAME + " where sId=" + sId + ";")
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
     * @param sId: id of a subject that will be queried
     * @return null: queried nothing; ACLSubject: the subject with id = sId
     */
    public ACLSubject queryOneSubject(int sId) {
        String queryOneSubjectSql = "select * from " + ModelValues.DAC_SUBJECT_TABLE_NAME + " where sId=" + sId + ";";
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryOneSubjectSql)
        ) {
            if (!resultSet.next()) {
                return null;
            } else {
                return new ACLSubject(sId,
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
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
                resultACLSubjectArrayList.add(new ACLSubject(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4)));
                while (resultSet.next()) {
                    resultACLSubjectArrayList.add(new ACLSubject(resultSet.getInt(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4)));
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
     * @param oId:             object's id
     * @param oName:           object's name
     * @param objectInfo:      object's information
     * @param createSubjectId: id of the subject that created the object
     * @return 0: operated failed; 1: operated succeed
     */
    public int addObject(int oId,
                         String oName,
                         String objectInfo,
                         int createSubjectId,
                         boolean executable) {
        String addObjectRecordSql = "insert into " +
                ModelValues.DAC_OBJECT_TABLE_NAME +
                "(oId,oName,objectInfo,createSubjectId,executeable) values('" +
                oId + "','" +
                oName + "','" +
                objectInfo + "','" +
                createSubjectId + "','" +
                executable + "');";

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
     * @param oId: object's id
     * @return 0: operated failed; 1: operated succeed; 2: there is no record as id = oId in the table
     */
    public int deleteObject(int oId) {
        String deleteObjectSql = "delete from +" + ModelValues.DAC_OBJECT_TABLE_NAME + " where oId=" + oId + ";";
        try (
                Statement statement = connection.createStatement();
                ResultSet checkResultSet = statement.executeQuery("select * from " +
                        ModelValues.DAC_OBJECT_TABLE_NAME +
                        " where oId=" + oId + ";")
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
     * @param oId:             object's id
     * @param oName:           object's name
     * @param objectInfo:      object's information
     * @param createSubjectId: the id of subject that created the object
     * @return 0: operated failed; 1: operated succeed; 2: there is no the record as id = oId int the table
     */
    public int modifyObject(int oId, String oName, String objectInfo, int createSubjectId, boolean executable) {
        String modifyObjectSql = "update " + ModelValues.DAC_OBJECT_TABLE_NAME + " set " +
                "oName=" + oName +
                ",objectInfo=" + objectInfo +
                ",createSubjectId=" + createSubjectId +
                ",executeable=" + executable +
                " where oId=" + oId + ";";
        try (
                Statement statement = connection.createStatement();
                ResultSet checkResultSet = statement.executeQuery("select * from " +
                        ModelValues.DAC_SUBJECT_TABLE_NAME +
                        " where oId=" + oId + ";")
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
     * The method to query a object with di = oId
     *
     * @param oId: subject's id
     * @return null: queried failed; ACLObject: one ACLObject
     */
    public ACLObject queryOneObject(int oId) {
        String queryOneObjectSql = "select * from " + ModelValues.DAC_OBJECT_TABLE_NAME + " while oId=" + oId + ";";
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryOneObjectSql)
        ) {
            if (!resultSet.next()) {
                return null;
            } else {
                return new ACLObject(oId,
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getBoolean(5));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * The method to query all objects.
     *
     * @return null: queried failed; ArrayList<ACLObject>: ArrayList contains all record objects
     */
    public ArrayList<ACLObject> queryAllObjects() {
        String queryAllObjectsSql = "select from " + ModelValues.DAC_OBJECT_TABLE_NAME + ";";
        ArrayList<ACLObject> resultQueryAllObjects = new ArrayList<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryAllObjectsSql)
        ) {
            if (!resultSet.next()) {
                return null;
            } else {
                resultQueryAllObjects.add(new ACLObject(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getBoolean(5)));
                while (resultSet.next()) {
                    resultQueryAllObjects.add(new ACLObject(resultSet.getInt(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
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
     * @param grantedSubjectName: grated subject's name
     * @param capabilityType:     capability's type
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
                             String capabilityType,
                             String capabilityString,
                             String capabilityInfo) {
        String addCapabilitySql = "insert into " +
                ModelValues.DAC_AL_CAPABILITY_TABLE_NAME +
                "(capabilityId,objectId,objectName,gratedSubjectId,gratedSubjectName,subjectId,subjectName,capabilityType,capabilityString,capabilityInfo) values(" +
                "'" + capabilityId + "'," +
                "'" + objectId + "'," +
                "'" + objectName + "'," +
                "'" + grantedSubjectId + "'," +
                "'" + grantedSubjectName + "'," +
                "'" + subjectId + "'," +
                "'" + subjectName + "'," +
                "'" + capabilityType + "'," +
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
        String deleteCapabilitySql = "delete from " + ModelValues.DAC_AL_CAPABILITY_TABLE_NAME + " where capabilityId=" + capabilityId + ";";
        try (
                Statement statement = connection.createStatement();
                ResultSet checkResultSet = statement.executeQuery("select * from " + ModelValues.DAC_AL_CAPABILITY_TABLE_NAME + " where capabilityId=" + capabilityId + ";")
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
     * The method to modify capability record in table.
     * @param capabilityId: capability's id
     * @param newCapabilityString: new capability string
     * @param newCapabilityInfo: new capability info
     * @return 0: operated failed; 1: operated succeed; 2: there is no capability record with id=capabilityId in the table
     */
    public int modifyCapability(int capabilityId, String newCapabilityString, String newCapabilityInfo) {
        String modifyCapabilitySql = "update " + ModelValues.DAC_AL_CAPABILITY_TABLE_NAME + " set " +
                "capabilityString=" + newCapabilityString +
                ",capabilityInfo=" + newCapabilityInfo + ";";
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
     * @param capabilityId: capability's id
     * @return null: queried failed; CapabilityOfSubject: new capability of subject
     */
    public CapabilityOfSubject queryOneCapabilityOfSubject(int capabilityId) {
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
                return new CapabilityOfSubject(capabilityId,
                        resultSet.getInt(2),
                        resultSet.getString(3),
                        resultSet.getInt(4),
                        resultSet.getString(5),
                        resultSet.getInt(6),
                        resultSet.getString(7),
                        resultSet.getString(8),
                        resultSet.getString(9));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * The method to query all record of capability in the table.
     * @return null: queried failed; ArrayList<CapabilityOfSubject>: queried succeed
     */
    public ArrayList<CapabilityOfSubject> queryAllCapabilitiesOfSubject() {
        String queryAllCapabilityOfSubject = "select * from " + ModelValues.DAC_AL_CAPABILITY_TABLE_NAME + ";";
        ArrayList<CapabilityOfSubject> resultCapabilityOfSubjectArrayList = new ArrayList<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryAllCapabilityOfSubject)
        ) {
            if (!resultSet.next()) {
                return null;
            } else {
                resultCapabilityOfSubjectArrayList.add(new CapabilityOfSubject(resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getString(3),
                        resultSet.getInt(4),
                        resultSet.getString(5),
                        resultSet.getInt(6),
                        resultSet.getString(7),
                        resultSet.getString(8),
                        resultSet.getString(9)));
                while (resultSet.next()) {
                    resultCapabilityOfSubjectArrayList.add(new CapabilityOfSubject(resultSet.getInt(1),
                            resultSet.getInt(2),
                            resultSet.getString(3),
                            resultSet.getInt(4),
                            resultSet.getString(5),
                            resultSet.getInt(6),
                            resultSet.getString(7),
                            resultSet.getString(8),
                            resultSet.getString(9)));
                }
                return resultCapabilityOfSubjectArrayList;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<CapabilityOfSubject> queryCapabilityOfSubjectsBySubjectId(int subjectId) {
        String queryOneCapabilityOfSubjectSql = "select * from " +
                ModelValues.DAC_AL_CAPABILITY_TABLE_NAME +
                " where subjectId=" + subjectId + ";";
        ArrayList<CapabilityOfSubject> result = new ArrayList<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryOneCapabilityOfSubjectSql)
        ) {
            if (!resultSet.next()) {
                return null;
            } else {
                result.add(new CapabilityOfSubject(resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getString(3),
                        resultSet.getInt(4),
                        resultSet.getString(5),
                        resultSet.getInt(6),
                        resultSet.getString(7),
                        resultSet.getString(8),
                        resultSet.getString(9)));
                while (resultSet.next()) {
                    result.add(new CapabilityOfSubject(resultSet.getInt(1),
                            resultSet.getInt(2),
                            resultSet.getString(3),
                            resultSet.getInt(4),
                            resultSet.getString(5),
                            resultSet.getInt(6),
                            resultSet.getString(7),
                            resultSet.getString(8),
                            resultSet.getString(9)));
                }
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<CapabilityOfSubject> queryCapabilityOfSubjectsByObjectId(int objectId) {
        String queryOneCapabilityOfSubjectSql = "select * from " +
                ModelValues.DAC_AL_CAPABILITY_TABLE_NAME +
                " where objectId=" + objectId + ";";
        ArrayList<CapabilityOfSubject> result = new ArrayList<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryOneCapabilityOfSubjectSql)
        ) {
            if (!resultSet.next()) {
                return null;
            } else {
                result.add(new CapabilityOfSubject(resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getString(3),
                        resultSet.getInt(4),
                        resultSet.getString(5),
                        resultSet.getInt(6),
                        resultSet.getString(7),
                        resultSet.getString(8),
                        resultSet.getString(9)));
                while (resultSet.next()) {
                    result.add(new CapabilityOfSubject(resultSet.getInt(1),
                            resultSet.getInt(2),
                            resultSet.getString(3),
                            resultSet.getInt(4),
                            resultSet.getString(5),
                            resultSet.getInt(6),
                            resultSet.getString(7),
                            resultSet.getString(8),
                            resultSet.getString(9)));
                }
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public ArrayList<CapabilityOfSubject> queryCapabilityOfSubjectsByGrantedSubjectId(int grantedSubjectId) {
        String queryOneCapabilityOfSubjectSql = "select * from " +
                ModelValues.DAC_AL_CAPABILITY_TABLE_NAME +
                " where grantedSubjectId=" + grantedSubjectId + ";";
        ArrayList<CapabilityOfSubject> result = new ArrayList<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryOneCapabilityOfSubjectSql)
        ) {
            if (!resultSet.next()) {
                return null;
            } else {
                result.add(new CapabilityOfSubject(resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getString(3),
                        resultSet.getInt(4),
                        resultSet.getString(5),
                        resultSet.getInt(6),
                        resultSet.getString(7),
                        resultSet.getString(8),
                        resultSet.getString(9)));
                while (resultSet.next()) {
                    result.add(new CapabilityOfSubject(resultSet.getInt(1),
                            resultSet.getInt(2),
                            resultSet.getString(3),
                            resultSet.getInt(4),
                            resultSet.getString(5),
                            resultSet.getInt(6),
                            resultSet.getString(7),
                            resultSet.getString(8),
                            resultSet.getString(9)));
                }
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*public ArrayList<CapabilityOfSubject> queryCapabilitiesOfSubjectByIds(int id, int idMod) {
        String querySql = "";
        switch (idMod) {
            case DatabaseOperation.QUERY_CAPABILITIES_MOD_SUBJECT_ID:
                querySql = "select * from " +
                        ModelValues.DAC_AL_CAPABILITY_TABLE_NAME +
                        " where subjectId=" + id + ";";
                break;
            case DatabaseOperation.QUERY_CAPABILITIES_MOD_OBJECT_ID:
                querySql = "select * from " +
                        ModelValues.DAC_AL_CAPABILITY_TABLE_NAME +
                        " where objectId=" + id + ";";
                break;
            case DatabaseOperation.QUERY_CAPABILITIES_MOD_GRANTED_SUBJECT_ID:
                querySql="select * from " +
                        ModelValues.DAC_AL_CAPABILITY_TABLE_NAME +
                        " where grantedSubjectId=" + id + ";";
                break;
            default:
                querySql = "select * from " +
                        ModelValues.DAC_AL_CAPABILITY_TABLE_NAME +
                        " where capabilityId=" + id + ";";
                break;
        }

    }*/
    /*
     *
     * @param sId
     * @param connection
     * @param operationSql
     * @param operationMod int 0: add a subject;
     *                     int 1: delete a subject;
     *                     int 2: modify a subject;
     * @return public int operateSubjectRecords(@NotNull String sId, @NotNull Connection connection, @NotNull String operationSql, int operationMod) {
    try (
    Statement statement = connection.createStatement();
    ResultSet checkResult = statement.executeQuery("select * from " + ModelValues.DAC_SUBJECT_TABLE_NAME + " where sId=" + sId + ";")
    ) {
    //Check is the record exist.
    if (!checkResult.next()) {
    return
    }
    } catch (SQLException e) {
    e.printStackTrace();
    }
    }*/

}
