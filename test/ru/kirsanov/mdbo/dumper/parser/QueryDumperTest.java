package ru.kirsanov.mdbo.dumper.parser;

import org.junit.Test;
import ru.kirsanov.mdbo.dumper.composer.Encoding;
import ru.kirsanov.mdbo.dumper.composer.PlainComposer;
import ru.kirsanov.mdbo.dumper.exception.IncorrectDumper;
import ru.kirsanov.mdbo.dumper.exception.NoColumnForDumpException;
import ru.kirsanov.mdbo.synchronize.utility.ConnectionData;
import ru.kirsanov.mdbo.synchronize.utility.ConnectionManger;

public class QueryDumperTest {
    private ConnectionManger cm = new ConnectionManger(new ConnectionData(ConnectionData.getBaseName(), "postgresql"));

    @Test(expected = IncorrectDumper.class)
    public void executeModelDumperWithIncorrectDumperShouldThrowExceptionTest() throws Exception, IncorrectDumper, NoColumnForDumpException {
        QueryDumper queryDumper = new QueryDumper(cm.getConnection(), "/home/lqip32222/", Encoding.UTF8, new PlainComposer(','));
        queryDumper.execute(new Dumpable() {
        });
    }
}
