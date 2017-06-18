package cn.mrz.pojo;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/9.
 */
public class Role implements Serializable {
    private long id;
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
