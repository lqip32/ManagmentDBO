package ru.kirsanov.mdbo.metamodel.datatype;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SimpleDataTypeTest {

    private DataType dataType;

    @Before
    public void setUp() {
        dataType = new SimpleDatatype("VARCHAR", 10);
    }

    @Test
    public void getNameTest() throws Exception {
        assertEquals("VARCHAR", dataType.getName());
    }

    @Test
    public void sqlStringTest() throws Exception {
        assertEquals("VARCHAR (10)", dataType.getSqlString());
        dataType = new SimpleDatatype("NUMERIC", 10, 12);
        assertEquals("NUMERIC (10, 12)", dataType.getSqlString());
    }

}
