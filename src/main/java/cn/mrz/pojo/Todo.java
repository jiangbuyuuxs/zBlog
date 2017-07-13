package cn.mrz.pojo;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * Created by Administrator on 2017/7/13.
 */
public class Todo {
    private Long id;
    private String title;
    private String remark;
    @JSONField(format="yyyy-MM-dd")
    private Date createDate;
    private Integer state;
    private Long userId;

    public Todo() {
    }

    public Todo(String title, String remark, Date createDate, Integer state, Long userId) {
        this.title = title;
        this.remark = remark;
        this.createDate = createDate;
        this.state = state;
        this.userId = userId;
    }

    public Todo(Long id, String title, String remark, Date createDate, Integer state, Long userId) {
        this.id = id;
        this.title = title;
        this.remark = remark;
        this.createDate = createDate;
        this.state = state;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
