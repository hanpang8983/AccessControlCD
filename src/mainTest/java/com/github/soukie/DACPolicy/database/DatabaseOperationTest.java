package com.github.soukie.DACPolicy.database;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
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
        testDatabaseOperation = new DatabaseOperation(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        testDatabaseOperation.initDatabaseConnection("jdbc:mysql://127.0.0.1:3306/accesscontrolcd", "root", "142123");

        Assert.assertEquals(1, testDatabaseOperation.addSubject(1, "s1", "123456", "test"));
    }
}