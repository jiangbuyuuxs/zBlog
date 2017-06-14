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
    private Date createDate;
    private Date editDate;
    private String texts;
    private long imageId;
    private int classType;

    public long getId() {
        return id;
    }

    public int getClassType() {
        return classType;
    }

    public void setClassType(int classType) {
        this.classType = classType;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getEditDate() {
        return editDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }

    public String getTexts() {
        return texts;
    }

    public void setTexts(String texts) {
        this.texts = texts;
    }

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }
}
