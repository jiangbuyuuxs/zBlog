package cn.mrz.pojo;

import com.baomidou.mybatisplus.annotations.TableName;

import java.util.Date;

/**
 * Created by Administrator on 2016/12/1.
 */
@TableName("blogs")
public class Blog {

    private long id;
    private String title;
    private Date cdate;
    private Date edate;
    private String texts;
    private long imgid;
    private int classtype;

    public long getId() {
        return id;
    }

    public int getClasstype() {
        return classtype;
    }

    public void setClasstype(int classtype) {
        this.classtype = classtype;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCdate() {
        return cdate;
    }

    public void setCdate(Date cdate) {
        this.cdate = cdate;
    }

    public Date getEdate() {
        return edate;
    }

    public void setEdate(Date edate) {
        this.edate = edate;
    }

    public String getTexts() {
        return texts;
    }

    public void setTexts(String texts) {
        this.texts = texts;
    }

    public long getImgid() {
        return imgid;
    }

    public void setImgid(long imgid) {
        this.imgid = imgid;
    }
}
