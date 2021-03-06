package cn.mrz.pojo;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/8/17.
 */
public class Reply implements Serializable {
    private Long id;
    private Long cId;
    private Long uId;
    private String username;
    private Date cTime;
    private Long device;
    private String content;
    private List<Reply> reply;
    private Long up;
    private Long down;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public Long getUp() {
        return up;
    }

    public void setUp(Long up) {
        this.up = up;
    }

    public Long getDown() {
        return down;
    }

    public void setDown(Long down) {
        this.down = down;
    }
    public List<Reply> getReply() {
        return reply;
    }

    public void setReply(List<Reply> reply) {
        this.reply = reply;
    }

    public Long getuId() {
        return uId;
    }

    public void setuId(Long uId) {
        this.uId = uId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getcId() {
        return cId;
    }

    public void setcId(Long cId) {
        this.cId = cId;
    }

    public Date getcTime() {
        return cTime;
    }

    public void setcTime(Date cTime) {
        this.cTime = cTime;
    }

    public Long getDevice() {
        return device;
    }

    public void setDevice(Long device) {
        this.device = device;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
