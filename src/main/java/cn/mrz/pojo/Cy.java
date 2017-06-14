package cn.mrz.pojo;

/**
 * Created by Administrator on 2017/4/20.
 */

public class Cy {
    private long id;
    private String cy;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCy() {
        return cy;
    }

    public void setCy(String cy) {
        this.cy = cy;
    }

    public String getPyFirst() {
        return pyFirst;
    }

    public void setPyFirst(String pyFirst) {
        this.pyFirst = pyFirst;
    }

    public String getPyEnd() {
        return pyEnd;
    }

    public void setPyEnd(String pyEnd) {
        this.pyEnd = pyEnd;
    }

    private String pyFirst;
    private String pyEnd;
}
