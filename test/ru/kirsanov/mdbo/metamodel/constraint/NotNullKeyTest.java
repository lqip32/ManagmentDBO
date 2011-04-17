package ru.kirsanov.mdbo.metamodel.constraint;

import org.junit.Before;
import org.junit.Test;
import ru.kirsanov.mdbo.metamodel.datatype.SimpleDatatype;
import ru.kirsanov.mdbo.metamodel.entity.Column;
import ru.kirsanov.mdbo.metamodel.entity.Table;
import ru.kirsanov.mdbo.metamodel.exception.ColumnNotFoundException;

import static org.junit.Assert.assertEquals;

public class NotNullKeyTest {
    private Table table;
    private Column column;
    private String notNullKeyName;
    private NotNullKey notNullKey;

    @Before
    public void setUp() {
        table = new Table("test");
        column = table.createColumn("testColumn", new SimpleDatatype("Smallint"));
        notNullKeyName = "nnKey";
        notNullKey = new NotNullKey(table, notNullKeyName);
    }

    @Test
    public void nameTest() {
        assertEquals(notNullKeyName, notNullKey.getName());
    }

    @Test
    public void columnTest() throws ColumnNotFoundException {
        notNullKey.addColumn(column);
        assertEquals(column, notNullKey.getColumns().get(0));
    }

}