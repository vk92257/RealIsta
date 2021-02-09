package com.example.myapplication.pojo;

import java.io.Serializable;

public class SelectItem implements Serializable {
    private String dataname;
    private Boolean active;

    public SelectItem(String dataname) {
        this.dataname = dataname;
        active = true;
    }

    public SelectItem(String dataname, Boolean active) {
        this.dataname = dataname;
        this.active = active;
    }

    public String getDataname() {
        return dataname;
    }

    public void setDataname(String dataname) {
        this.dataname = dataname;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
    @Override
    public String toString() {
        return this.dataname;
    }
}
