package com.github.soukie.model.database;

import com.github.soukie.model.ModelValues;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.sql.Statement;
import java.util.Date;

/**
 * Created by qiyiy on 2016/1/5.
 */
public class DatabaseOperationTest extends TestCase{

    DatabaseOperation testDatabaseOperation;

    @Test
    public void testChangeConnection() throws Exception {

    }

    @Test
    public void testGetMySQLDriverPath() throws Exception {

    }

    @Test
    public void testGetOperationTime() throws Exception {

    }

    @Test
    public void testInitDatabaseConnection() throws Exception {

    }

    @Before
    public void init() {

    }



    @Test
    public void testAddSubject() throws Exception {
        System.out.println("test start.");
        testDatabaseOperation = new DatabaseOperation(new Date().getTime());
        testDatabaseOperation.initDatabaseConnection("jdbc:mysql://127.0.0.1:3306/access_control_cd", "root", "142123");

        Statement statement = testDatabaseOperation.getConnection().createStatement();
        statement.executeUpdate(ModelValues.RBAC_RRA_TABLE_CREATE_SQL);
    }
}
