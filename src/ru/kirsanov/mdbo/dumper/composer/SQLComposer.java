package ru.kirsanov.mdbo.dumper.composer;

import ru.kirsanov.mdbo.dumper.validator.Validator;

import java.util.List;

public class SQLComposer implements IComposer {
    private StringBuilder stringBuilder;

    public SQLComposer() {
        stringBuilder = new StringBuilder();
    }

    @Override
    public void addHeader(String tableName, List<String> columns) {
        StringBuilder headerString = new StringBuilder("INSERT INTO ").append(tableName).append("(");
        int i = 0;
        for (String column : columns) {
            headerString.append(column);
            i++;
            if (i < columns.size()) {
                headerString.append(", ");
            }
        }
        headerString.append(") VALUES");
    }

    @Override
    public void addBody(String[] line) {
        stringBuilder.append("(");
        stringBuilder.append(Validator.prepareData(line));
        stringBuilder.append(")");

    }

    @Override
    public void addEndLine() {
        stringBuilder.append(",");
    }

    @Override
    public void addEnd() {
        stringBuilder.append(";");
    }

    @Override
    public String getResults() {
        return stringBuilder.toString();
    }
}
