package ru.kirsanov.mdbo.metamodel.entity;

import org.junit.Test;
import ru.kirsanov.mdbo.metamodel.constraint.ForeignKey;
import ru.kirsanov.mdbo.metamodel.datatype.DataType;
import ru.kirsanov.mdbo.metamodel.datatype.SimpleDatatype;
import ru.kirsanov.mdbo.metamodel.exception.*;

import static org.junit.Assert.assertEquals;

public class SchemaTest {
    private String myTable;

    @Test
    public void tableTest() throws Exception {
        ISchema schema = new Schema("mySchema");
        ITable table = new Table("myTable");
        schema.addTable(new Table("myTable"));
        schema.removeTable(table);
        assertEquals(0, schema.getTables().size());
    }

    @Test
    public void getTableTest() throws Exception, TableNotFound {
        ISchema schema = new Schema("mySchema");
        myTable = "myTable";
        ITable table = new Table(myTable);
        schema.addTable(table);
        assertEquals(table, schema.getTable(myTable));
    }

    @Test
    public void foreignKeyTest() throws Exception, TableNotFound, ColumnAlreadyExistsException, ColumnNotFoundException, ForeignKeyNotFound {
        ISchema schema = new Schema("mySchema");
        myTable = "myTable";
        ITable table = new Table(myTable);
        String secondMyTable = "secondMyTable";
        DataType dataType = new SimpleDatatype("int");
        IColumn firstColumn = table.createColumn("fisrtColumn", dataType);
        ITable secondTable = new Table(secondMyTable);
        IColumn secondColumn = secondTable.createColumn("secondColumn", dataType);
        String myForeignKey = "myForeignKey";
        ForeignKey foreignKey = schema.createForeignKey(myForeignKey, table, secondTable);
        foreignKey.addColumnMapping(firstColumn, secondColumn);
        assertEquals(foreignKey, schema.getForeignKey(myForeignKey));
    }

    @Test
    public void viewTest() throws ViewNotFoundException, ColumnAlreadyExistsException {
        ISchema schema = new Schema("mySchema");
        String viewName = "view";
        IView view = schema.createView(viewName, "Select test");
        assertEquals(view, schema.getView(viewName));
    }

    @Test(expected = ViewNotFoundException.class)
    public void notFoundViewMustThrowExceptionTest() throws ViewNotFoundException, ColumnAlreadyExistsException {
        ISchema schema = new Schema("mySchema");
        myTable = "myTable";
        String viewName = "view";
        String otherViewName = "view2";
        schema.createView(viewName, "Select test");
        schema.getView(otherViewName);
    }

    @Test
    public void indexTest() throws ColumnAlreadyExistsException, IndexNotFoundException {
        ISchema schema = new Schema("mySchema");
        myTable = "myTable";
        ITable table = new Table(myTable);
        DataType dataType = new SimpleDatatype("int");
        IColumn firstColumn = table.createColumn("fisrtColumn", dataType);
        String indexName = "index";
        IIndex index = schema.createIndex(indexName, firstColumn);
        assertEquals(index, schema.getIndex(indexName));
    }

    @Test(expected = IndexNotFoundException.class)
    public void getNotExistsIndexSholdBeThrowsExceptionTest() throws ColumnAlreadyExistsException, IndexNotFoundException {
        ISchema schema = new Schema("mySchema");
        myTable = "myTable";
        ITable table = new Table(myTable);
        DataType dataType = new SimpleDatatype("int");
        IColumn firstColumn = table.createColumn("fisrtColumn", dataType);
        String indexName = "index";
        String notExistsIndexName = "index2";
        schema.createIndex(indexName, firstColumn);
        schema.getIndex(notExistsIndexName);
    }

    @Test(expected = ForeignKeyNotFound.class)
    public void getNotExistsForeignKeyShouldBeThrowsExceptionTest() throws ColumnAlreadyExistsException, ColumnNotFoundException, ForeignKeyNotFound {
        ISchema schema = new Schema("mySchema");
        myTable = "myTable";
        ITable table = new Table(myTable);
        String secondMyTable = "secondMyTable";
        DataType dataType = new SimpleDatatype("int");
        IColumn firstColumn = table.createColumn("fisrtColumn", dataType);
        ITable secondTable = new Table(secondMyTable);
        IColumn secondColumn = secondTable.createColumn("secondColumn", dataType);
        String correctForeignKeyName = "myForeignKey";
        String inCorrectForeignKeyName = "inCorrect";
        ForeignKey foreignKey = schema.createForeignKey(correctForeignKeyName, table, secondTable);
        foreignKey.addColumnMapping(firstColumn, secondColumn);
        schema.getForeignKey(inCorrectForeignKeyName);
    }
}
