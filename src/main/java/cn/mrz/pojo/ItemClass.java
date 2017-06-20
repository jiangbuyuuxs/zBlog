package cn.mrz.pojo;

import javax.validation.constraints.NotEmpty;

/**
 * Created by Administrator on 2017/6/20.
 */
public class ItemClass {
    private Long id;
    @NotEmpty
    private String title;
    private int hashCode;
    /**
     * 0 顶级
     * -1 未设定
     */
    private int parentId;

    public ItemClass() {
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public ItemClass(String title) {
        this.title = title;
        this.hashCode = title.hashCode();
    }

    public ItemClass(String title, int parentId) {
        this.title = title;
        this.hashCode = title.hashCode();
        this.parentId = parentId;
    }

    public ItemClass(Long id, String title, int parentId) {
        this.id = id;
        this.title = title;
        this.parentId = parentId;
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

    public int getHashCode() {
        return hashCode;
    }

    public void setHashCode(int hashCode) {
        this.hashCode = hashCode;
    }
}
