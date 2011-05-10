package ru.kirsanov.mdbo.metamodel.entity;

import ru.kirsanov.mdbo.metamodel.constraint.PrimaryKey;
import ru.kirsanov.mdbo.metamodel.constraint.UniqueKey;
import ru.kirsanov.mdbo.metamodel.exception.ColumnNotFoundException;
import ru.kirsanov.mdbo.metamodel.exception.ElementNotFoundException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Table extends ColumnContainer implements ITable {

    private PrimaryKey primaryKey = null;
    private UniqueKey uniqueKey = null;

    public Table(final String name) {
        super(name);
        columns = new LinkedList<IColumn>();
    }

    public Table(final String name, final ISchema schema) {
        super(name);
        this.container = schema;
        columns = new LinkedList<IColumn>();
    }

    @Override
    public PrimaryKey createPrimaryKey(IColumn column) throws ColumnNotFoundException {
        if (columns.contains(column)) {
            primaryKey = new PrimaryKey(this, column.getName());
            primaryKey.addColumn(column);
            return primaryKey;
        } else {
            throw new ColumnNotFoundException();
        }
    }



    @Override
    public UniqueKey createUniqueKey(IColumn column) throws ColumnNotFoundException {
        uniqueKey = new UniqueKey(this, column.getName());
        uniqueKey.addColumn(column);
        return uniqueKey;
    }

    @Override
    public UniqueKey getUniqueKey() {
        return uniqueKey;
    }

    @Override
    public PrimaryKey getPrimaryKey() {
        return primaryKey;
    }

    @Override
    public void addTuple(String... value) throws IllegalArgumentException {
        if (value.length == columns.size()) {
            int i = 0;
            for (String currentStr : value) {
                columns.get(i).addVariable(currentStr);
                i++;
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public List<String> getTuple(int id) throws ElementNotFoundException {
        List<String> tuple = new ArrayList<String>();
        if (id < columns.size()) {
            for (IColumn column : columns) {
                tuple.add(column.getVariable(id));
            }
        } else {
            throw new ElementNotFoundException();
        }
        return tuple;
    }

    @Override
    public void removeTuple(int id) throws ElementNotFoundException {
        if (id < columns.size()) {
            for (IColumn column : columns) {
                column.removeVariable(id);
            }
        } else {
            throw new ElementNotFoundException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Table table = (Table) o;

        if (columns != null ? !columns.equals(table.columns) : table.columns != null) return false;
        if (container != null ? !container.getName().equals(table.container.getName()) : table.container != null)
            return false;
        if (primaryKey != null ? !primaryKey.equals(table.primaryKey) : table.primaryKey != null) return false;
        if (uniqueKey != null ? !uniqueKey.equals(table.uniqueKey) : table.uniqueKey != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = columns != null ? columns.hashCode() : 0;
        result = 31 * result + (container != null ? container.hashCode() : 0);
        result = 31 * result + (primaryKey != null ? primaryKey.hashCode() : 0);
        result = 31 * result + (uniqueKey != null ? uniqueKey.hashCode() : 0);
        return result;
    }
}
