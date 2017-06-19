package cn.mrz.pojo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by Administrator on 2017/6/10.
 */
public class Item implements Serializable{

    @TableId
    private Long id;
    private String itemId;
    private String title;
    private String imageUrl;
    private String detailUrl;
    private String itemClass;
    private String tbkUrl;
    private Float price;
    private Float myMoney;
    private String shopName;
    //TAOBAO TMALL
    private String shopType;
    private Date startDate;
    private Date endDate;
    private String shortUrl;
    private String taoUrl;
    private Long salesVolume;
    @TableField(exist = false)
    private Favourable favourable;

    public Item(Long id, String itemId, String title, String imageUrl, String detailUrl, String itemClass, String tbkUrl, Float price, Float myMoney, String shopName, String shopType, Date startDate, Date endDate, String shortUrl, String taoUrl, Long salesVolume, Favourable favourable) {
        this.id = id;
        this.itemId = itemId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.detailUrl = detailUrl;
        this.itemClass = itemClass;
        this.tbkUrl = tbkUrl;
        this.price = price;
        this.myMoney = myMoney;
        this.shopName = shopName;
        this.shopType = shopType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.shortUrl = shortUrl;
        this.taoUrl = taoUrl;
        this.salesVolume = salesVolume;
        this.favourable = favourable;
    }

    public Item() {
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Favourable getFavourable() {
        return favourable;
    }

    public void setFavourable(Favourable favourable) {
        this.favourable = favourable;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getItemClass() {
        return itemClass;
    }

    public void setItemClass(String itemClass) {
        this.itemClass = itemClass;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Float getMyMoney() {
        return myMoney;
    }

    public void setMyMoney(Float myMoney) {
        this.myMoney = myMoney;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Long getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(Long salesVolume) {
        this.salesVolume = salesVolume;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
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
}
