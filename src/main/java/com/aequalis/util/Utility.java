package com.aequalis.util;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.impl.StatementImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by anand on 23/01/2017.
 */
public class Utility {



    public static List<com.aequalis.util.Statement> getDBStatements(String strTableName, String strOntoClassName, Map<String, String> propertyMapping){

        Connection connection = null;
        java.sql.Statement statement = null;
        ResultSet resultSet = null;
        String GRAPH_NAME = "http://www.innovation.com/schemas/virtrdf#";

        List<com.aequalis.util.Statement> allStatements = new ArrayList<com.aequalis.util.Statement>();

        try {
            connection = ConnectionConfiguration.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * from "+strTableName);

            while (resultSet.next()){

                for (Map.Entry entry : propertyMapping.entrySet()){

                    String associatedProperty = entry.getValue().toString();

                    com.aequalis.util.Statement newStatement = new com.aequalis.util.Statement();
                    newStatement.setSubject(GRAPH_NAME + strOntoClassName);
                    newStatement.setPredicate(GRAPH_NAME + associatedProperty);
                    newStatement.setObject(resultSet.getObject(entry.getKey().toString()).toString());

                    allStatements.add(newStatement);

                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return allStatements;

    }
}
