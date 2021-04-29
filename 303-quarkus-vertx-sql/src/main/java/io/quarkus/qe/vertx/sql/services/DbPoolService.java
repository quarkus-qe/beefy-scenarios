package io.quarkus.qe.vertx.sql.services;

import java.util.List;
import java.util.stream.Collectors;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.Pool;
import io.vertx.mutiny.sqlclient.SqlClientHelper;
import io.vertx.sqlclient.PropertyKind;

public class DbPoolService extends Pool {

    private final static PropertyKind<Long> LAST_INSERTED_ID = PropertyKind.create("last-inserted-id", Long.class);
    private final String databaseName;
    private final String selectedDb;

    public DbPoolService(Pool delegate, String dbName, String selectedDb) {
        super(delegate.getDelegate());
        this.databaseName = dbName;
        this.selectedDb = selectedDb;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public Uni<Long> save(String tableName, List<String> fieldsNames, List<Object> fieldsValues) {
        switch (selectedDb) {
            case "mysql":
                return saveMysql(tableName, fieldsNames, fieldsValues);
            case "db2":
                return saveDb2(tableName, fieldsNames, fieldsValues);
            default:
                return savePg(tableName, fieldsNames, fieldsValues);
        }
    }

    protected Uni<Long> savePg(String tableName, List<String> fieldsNames, List<Object> fieldsValues) {
        return SqlClientHelper.inTransactionUni(this, tx -> {
            String fields = tableFieldsToString(fieldsNames);
            String values = tableFieldsValuesToString(fieldsValues);

            return tx
                    .preparedQuery("INSERT INTO " + getDatabaseName() + "." + tableName + " (" + fields + ") VALUES (" + values
                            + ") RETURNING id")
                    .execute().onItem().transform(id -> id.iterator().next().getLong("id"));
        });
    }

    protected Uni<Long> saveMysql(String tableName, List<String> fieldsNames, List<Object> fieldsValues) {
        return SqlClientHelper.inTransactionUni(this, tx -> {
            String fields = tableFieldsToString(fieldsNames);
            String values = tableFieldsValuesToString(fieldsValues);

            return tx
                    .preparedQuery(
                            "INSERT INTO " + getDatabaseName() + "." + tableName + " (" + fields + ") VALUES (" + values + ")")
                    .execute()
                    .onItem().invoke(r -> this.query("SELECT LAST_INSERT_ID();"))
                    .onItem().transform(id -> (Long) id.getDelegate().property(LAST_INSERTED_ID));
        });
    }

    protected Uni<Long> saveDb2(String tableName, List<String> fieldsNames, List<Object> fieldsValues) {
        return SqlClientHelper.inTransactionUni(this, tx -> {
            String fields = tableFieldsToString(fieldsNames);
            String values = tableFieldsValuesToString(fieldsValues);

            return tx
                    .preparedQuery("select id from NEW TABLE (INSERT INTO " + getDatabaseName() + "." + tableName + " ("
                            + fields + ") VALUES (" + values + "))")
                    .execute().onItem().transform(id -> id.iterator().next().getLong("id"));
        });
    }

    private String tableFieldsToString(List<String> fieldsNames) {
        return String.join(",", fieldsNames);
    }

    private String tableFieldsValuesToString(List<Object> fieldsValues) {
        return fieldsValues.stream()
                .map(n -> {
                    String content = String.valueOf(n);
                    if (n instanceof String) {
                        return "'" + n + "'";
                    }

                    return content;
                }).collect(Collectors.joining(",", "", ""));
    }

}
