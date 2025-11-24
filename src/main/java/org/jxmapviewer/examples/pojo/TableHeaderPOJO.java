package org.jxmapviewer.examples.pojo;

public class TableHeaderPOJO {

    private final String name;
    private final Class cls;

    public TableHeaderPOJO(String name, Class cls) {
        this.name = name;
        this.cls = cls;
    }

    public String getName() {
        return name;
    }

    public Class getCls() {
        return cls;
    }
}
