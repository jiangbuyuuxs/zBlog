package cn.mrz.pojo;


import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/15.
 */

@TableName("visit")
public class Visit implements Serializable{

    private long blogId;
    private long num;
    public Visit(){}

    public Visit(long blogId, long num) {
        this.blogId = blogId;
        this.num = num;
    }

    public long getBlogId() {
        return blogId;
    }

    public void setBlogId(long blogId) {
        this.blogId = blogId;
    }




    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

}
