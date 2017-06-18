package cn.mrz.pojo;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/9.
 */
public class Permission implements Serializable {
    private long id;
    private String permission;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
