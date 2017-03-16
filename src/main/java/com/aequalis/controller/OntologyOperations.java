package com.aequalis.controller;

import com.aequalis.bean.AppRequest;
import com.aequalis.util.Statement;
import com.aequalis.util.Utility;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import io.swagger.annotations.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import virtuoso.jdbc4.VirtuosoConnectionPoolDataSource;
import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtModel;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;

import javax.sql.ConnectionPoolDataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by anand on 09/01/2017.
 */
@Controller
@RequestMapping("/rdf/*")
public class OntologyOperations {

    @ApiOperation(value = "importToRDF", nickname = "importToRDF")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})

    @RequestMapping(value = "import2RDF",method = RequestMethod.POST)
    public @ResponseBody String importToRDF(@RequestBody List<AppRequest> appRequests){

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

        VirtGraph virtGraph = new VirtGraph("http://www.innovation.com/schemas/virtrdf#", ds);

        for (AppRequest appRequest : appRequests){

            List<Statement> allStatements = Utility.getDBStatements(appRequest.getTableName(), appRequest.getOntologyClassName(), appRequest.getPropertyMapping());

            try {
                Model m = null;
                try {

                    m = new VirtModel(virtGraph);
                    for (com.aequalis.util.Statement statement : allStatements){


                        final Resource subject = m.createResource(statement.getSubject());
                        final Property predicate = m.createProperty(statement.getPredicate());
                        final Resource object = m.createResource(statement.getObject());
                        m.add(subject, predicate, object);


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
        return "Successfully Imported!";
    }


}
