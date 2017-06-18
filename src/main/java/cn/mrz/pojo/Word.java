package cn.mrz.pojo;


import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

/**
 * 每一篇文章对应一些词汇,分开统计这些词汇,方便编辑时重新获取这些词汇.
 */
@TableName("words")
public class Word implements Serializable {
    private long id;
    private long blogId;
    private String remark;
    private String hashcode;
    private long num;
    private String wordType;

    public Word() {
    }

    public Word(String remark,long num,String hashcode) {
        this.remark = remark;
        this.num = num;
        this.hashcode = hashcode;
    }

    public long getBlogId() {
        return blogId;
    }

    public void setBlogId(long blogId) {
        this.blogId = blogId;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getHashcode() {
        return hashcode;
    }

    public void setHashcode(String hashcode) {
        this.hashcode = hashcode;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

    public String getWordType() {
        return wordType;
    }

    public void setWordType(String wordType) {
        this.wordType = wordType;
    }
}
