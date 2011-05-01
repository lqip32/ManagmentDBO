package ru.kirsanov.mdbo.synchronize.synchronizers.mysql;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.kirsanov.mdbo.metamodel.constraint.ForeignKey;
import ru.kirsanov.mdbo.metamodel.datatype.DataType;
import ru.kirsanov.mdbo.metamodel.datatype.SimpleDatatype;
import ru.kirsanov.mdbo.metamodel.entity.*;
import ru.kirsanov.mdbo.metamodel.exception.ColumnAlreadyExistsException;
import ru.kirsanov.mdbo.metamodel.exception.ColumnNotFoundException;
import ru.kirsanov.mdbo.synchronize.utility.ConnectionData;
import ru.kirsanov.mdbo.synchronize.utility.ConnectionManger;

import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;

public class MySQLForeignKeySynchronizerTest {
    private MysqlModel testModel;
    private ConnectionManger cm;

    @Before
    public void setUp() throws ColumnAlreadyExistsException, ColumnNotFoundException, SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        cm = new ConnectionManger(new ConnectionData("localhost", "information_schema", "mysql", "lqip32", "4f3v6"));
        testModel = new MysqlModel("testbase");
    }

    @Test
    public void executeTest() throws Throwable {
        ConnectionManger conn = new ConnectionManger(new ConnectionData("localhost", "testbase", "mysql", "lqip32", "4f3v6"));
        Statement statement = null;
        try {
            statement = conn.getConnection().createStatement();
            statement
                    .executeUpdate("DROP TABLE IF EXISTS childs;");

            statement
                    .executeUpdate("DROP TABLE IF EXISTS parents;");
            statement
                    .executeUpdate("CREATE TABLE parents (id INT NOT NULL,\n" +
                            "                     PRIMARY KEY (id)\n" +
                            ") ENGINE=INNODB;\n");
            statement
                    .executeUpdate("CREATE TABLE childs (id INT, parent_id INT,\n" +
                            "                    FOREIGN KEY (parent_id) REFERENCES parents(id)\n" +
                            "                      ON DELETE CASCADE\n" +
                            ") ENGINE=INNODB;");
            Model model = new MysqlModel("testbase");
            ISchema schema = testModel.createSchema("testbase");
            ITable parentsTable = new Table("parents");
            schema.addTable(parentsTable);
            DataType intDataType = new SimpleDatatype("INT", 11);
            IColumn parentsIdColumn = parentsTable.createColumn("id", intDataType);
            parentsIdColumn.setNullable(false);
            parentsTable.createPrimaryKey(parentsIdColumn);
            ITable childsTable = new Table("childs");
            schema.addTable(childsTable);
            IColumn childsIdColumn = childsTable.createColumn("id", intDataType);
            childsIdColumn.setNullable(true);
            IColumn childsParentIdColumn = childsTable.createColumn("parent_id", intDataType);
            childsParentIdColumn.setNullable(true);
            MySQLTableSynchronizer mySQLTableSynchronizer = new MySQLTableSynchronizer(cm.getConnection());
            MySQLPrimaryKeySynchronizer mySQLPrimaryKeySynchronizer = new MySQLPrimaryKeySynchronizer(cm.getConnection());
            MySQLForeignKeySynchronizer mySQLForeignKeySynchronizer = new MySQLForeignKeySynchronizer(cm.getConnection());
            Model synchronizeModel = mySQLForeignKeySynchronizer.execute(mySQLPrimaryKeySynchronizer.execute(mySQLTableSynchronizer.execute(model)));
            ForeignKey fk = schema.createForeignKey("childs_ibfk_1", childsTable, parentsTable);
            fk.addColumnMapping(childsParentIdColumn, parentsIdColumn);
            assertEquals(testModel,synchronizeModel);
        } finally {
            statement.close();
        }

    }

    @After
    public void tearDown
            () throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        ConnectionManger conn = new ConnectionManger(new ConnectionData("localhost", "testbase", "mysql", "lqip32", "4f3v6"));
        Statement statement = conn.getConnection().createStatement();
        statement
                .executeUpdate("DROP TABLE IF EXISTS childs;");
        statement
                .executeUpdate("DROP TABLE IF EXISTS parents;");
        statement.close();
        conn.getConnection().close();
    }

}