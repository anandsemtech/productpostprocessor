package com.aequalis.bean;

import java.util.HashMap;

/**
 * Created by anand on 07/02/2017.
 */
public class AppRequest {

    private String tableName;
    private String ontologyClassName;
    private HashMap propertyMapping;


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getOntologyClassName() {
        return ontologyClassName;
    }

    public void setOntologyClassName(String ontologyClassName) {
        this.ontologyClassName = ontologyClassName;
    }

    public HashMap getPropertyMapping() {
        return propertyMapping;
    }

    public void setPropertyMapping(HashMap propertyMapping) {
        this.propertyMapping = propertyMapping;
    }
}
