package com.aequalis.controller;


import com.aequalis.util.Utility;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import virtuoso.jdbc4.VirtuosoConnectionPoolDataSource;
import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtModel;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by anand on 09/01/2017.
 */
public class TestMe {

    public static void main(String[] args){


        ThreadPoolExecutor executor;

        String GRAPH_NAME = "http://www.innovation.com/schemas/virtrdf#";

//      virtGraph = new VirtGraph("jdbc:virtuoso://localhost:1111", "dba", "dba");
        VirtuosoConnectionPoolDataSource ds = new VirtuosoConnectionPoolDataSource();
        ds.setUser("dba");
        ds.setPwdClear("dba");
        ds.setPortNumber(1111);
        ds.setServerName("localhost");
        int threads = 4;
        try {
            ds.setMaxPoolSize(4);
        } catch (final SQLException e) {
            throw new RuntimeException("Unable to configure virtuoso connection pool size", e);
        }

        VirtGraph virtGraph = new VirtGraph(ds);
//        virtGraph.clear();

        try {


            Model m = null;
            try {
                m = new VirtModel(virtGraph);
                Map<String, String> propertyMapping = new HashMap<>();
                /*List<com.aequalis.util.Statement> allStatements = Utility.getDBStatements("products", "Products", propertyMapping);


                for (com.aequalis.util.Statement statement : allStatements){

                    final Resource subject = m.createResource(statement.getSubject());
                    final Property predicate = m.createProperty(statement.getPredicate());
                    final Resource object = m.createResource(statement.getObject());
                    m.add(subject, predicate, object);
                }
*/

                Query sparql = QueryFactory.create("SELECT * WHERE { GRAPH ?graph { ?s ?p ?o } } ");
                VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, virtGraph);
                ResultSet results = vqe.execSelect();
                while (results.hasNext()) {
                    QuerySolution result = results.nextSolution();
                    RDFNode graph = result.get("graph");
                    RDFNode s = result.get("s");
                    RDFNode p = result.get("p");
                    RDFNode o = result.get("o");
                    System.out.println(graph + " { " + s + " " + p + " " + o + " . }");
                }



            } finally {
                if (null != m) {
                    m.close();
                }
            }
        } finally {
            if (null != virtGraph) {
                virtGraph.close();
            }
        }



    }


}
