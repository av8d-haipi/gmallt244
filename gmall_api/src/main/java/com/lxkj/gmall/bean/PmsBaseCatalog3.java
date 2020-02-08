package com.lxkj.gmall.bean;

import javax.persistence.Id;
import java.io.Serializable;

public class PmsBaseCatalog3 implements Serializable {

    @Id
    private String id;
    private String name;
    private String catalog2_Id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatalog2_Id() {
        return catalog2_Id;
    }

    public void setCatalog2_Id(String catalog2_Id) {
        this.catalog2_Id = catalog2_Id;
    }
}
