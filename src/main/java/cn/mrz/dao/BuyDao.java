package cn.mrz.dao;

import java.util.List;

/**
 * Created by Administrator on 2017/8/3.
 */
public interface BuyDao {

    void addBuyFile(String key,String buyFile);
    void removeBuyFile(String key,String buyFile);
    List<String> listBuyFile(String key);
}
