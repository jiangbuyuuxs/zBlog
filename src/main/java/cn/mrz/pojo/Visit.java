package cn.mrz.pojo;


import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/15.
 */

@TableName("visit")
public class Visit implements Serializable{

    private long blogid;
    private long num;

    public long getBlogid() {
        return blogid;
    }

    public void setBlogid(long blogid) {
        this.blogid = blogid;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

}
