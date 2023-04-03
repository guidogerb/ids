package com.bpm.util;

import java.util.ArrayList;
import java.util.List;

public class QueryBuilders {
    private QueryBuilders() {}

    public static List<String> getCreateTableStatements(int length, int dimension) {
        List<String> stmts = new ArrayList<>();

        for (int d = 1; d <= dimension; d++) {
            for (int l = 1; l <= length; l++) {
                String tableName = l + "_" + d;
                String query = "CREATE TABLE IF NOT EXISTS PUBLIC.\"" + tableName + "\" " +
                        "(\n ";
                for (int j = 1; j <= l; j++) {
                    query += "    ID_" + j + " \"char\" NOT NULL,\n ";
                }
                query += "    DATA_ bit VARYING(1048576),\n " +
                        "    CONSTRAINT \"" + tableName + "_pkey\" PRIMARY KEY (";
                for (int j = 1; j <= l; j++) {
                    query += "ID_" + j + ",";
                }
                query = query.substring(0, query.length() - 1);
                query += ")\n ";
                query += ");\n " +
                        "ALTER TABLE IF EXISTS PUBLIC.\"" + tableName + "\" " +
                        " OWNER TO POSTGRES;\n\n";
                stmts.add(query);
            }
        }
        return stmts;
    }

}
