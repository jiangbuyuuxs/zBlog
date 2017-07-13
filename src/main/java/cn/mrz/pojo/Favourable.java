package cn.mrz.pojo;

import com.baomidou.mybatisplus.annotations.TableId;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/6/10.
 */
public class Favourable implements Serializable {
    @TableId
    private Long id;
    private String itemId;
    private String title;
    private Long countNum;//优惠劵总数
    private Long surplus;//剩余优惠劵
    private Date startDate;
    private Date endDate;
    private String tbkUrl;
    private String shortUrl;
    private String taoUrl;

    public Favourable() {
    }

    public Favourable(Long id, String itemId, String title, Long countNum, Long surplus, Date startDate, Date endDate, String tbkUrl, String shortUrl, String taoUrl) {
        this.id = id;
        this.itemId = itemId;
        this.title = title;
        this.countNum = countNum;
        this.surplus = surplus;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tbkUrl = tbkUrl;
        this.shortUrl = shortUrl;
        this.taoUrl = taoUrl;
    }

    public Long getCountNum() {
        return countNum;
    }

    public void setCountNum(Long countNum) {
        this.countNum = countNum;
    }

    public void setSurplus(Long surplus) {
        this.surplus = surplus;
    }
    public Long getSurplus() {
        return surplus;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getTaoUrl() {
        return taoUrl;
    }

    public void setTaoUrl(String taoUrl) {
        this.taoUrl = taoUrl;
    }

    public String getTbkUrl() {
        return tbkUrl;
    }

    public void setTbkUrl(String tbkUrl) {
        this.tbkUrl = tbkUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Favourable(Long countNum, Date endDate, String itemId, String shortUrl, Date startDate, Long surplus, String taoUrl, String tbkUrl, String title) {
        this.countNum = countNum;
        this.endDate = endDate;
        this.itemId = itemId;
        this.shortUrl = shortUrl;
        this.startDate = startDate;
        this.surplus = surplus;
        this.taoUrl = taoUrl;
        this.tbkUrl = tbkUrl;
        this.title = title;
    }


}
