package ru.kirsanov.mdbo.dumper.writer;

import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SingleSQLWriterTest {
    private PrintWriter mockedPrintWriter;
    private SingleSQLWriter writer;
    private List<String> columns;
    private String tableName;

    @Before
    public void setUp() throws FileNotFoundException, UnsupportedEncodingException {
        mockedPrintWriter = mock(PrintWriter.class);
        columns = new ArrayList<String>();
        columns.add("id");
        columns.add("name");
        tableName = "table";
        writer = new SingleSQLWriter(mockedPrintWriter, tableName, columns);
    }

    @Test
    public void writeTest() throws Exception {
        writer.write(new String[]{"1", "Alexandr"});
        writer.setEnd();
        writer.write(new String[]{"2", "Andrey"});
        String firstLine = "INSERT INTO " + tableName + "(id, name) ";
        String secondLine = " VALUES";
        String thirdLine ="('1','Alexandr'),";
        String fourthLine = "('2','Andrey');";
        verify(mockedPrintWriter).write(firstLine);
        verify(mockedPrintWriter).write(secondLine);
        verify(mockedPrintWriter).write(thirdLine);
        verify(mockedPrintWriter).write(fourthLine);
    }
}