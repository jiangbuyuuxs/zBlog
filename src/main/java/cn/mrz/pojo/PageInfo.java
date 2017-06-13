package cn.mrz.pojo;

/**
 * Created by Administrator on 2017/6/5.
 */
public class PageInfo {
    public int start;
    public int num;
    public String sort;

    public PageInfo(int start, int num, String sort) {
        this.start = start;
        this.num = num;
        this.sort = sort;
    }

    public PageInfo() {
    }
}
