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
public class DACDatabaseOperationTest extends TestCase{

    DACDatabaseOperation testDACDatabaseOperation;

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
        testDACDatabaseOperation = new DACDatabaseOperation(new Date().getTime());
        testDACDatabaseOperation.initDatabaseConnection("jdbc:mysql://127.0.0.1:3306/access_control_cd", "root", "142123");

        Statement statement = testDACDatabaseOperation.getConnection().createStatement();
        statement.executeUpdate(ModelValues.DAC_AL_CAPABILITY_TABLE_CREATE_SQL);
    }
}