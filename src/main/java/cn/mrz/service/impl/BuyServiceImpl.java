package cn.mrz.service.impl;

import cn.mrz.dao.ItemDao;
import cn.mrz.exception.BuyFileExistException;
import cn.mrz.mapper.FavourableMapper;
import cn.mrz.mapper.ItemMapper;
import cn.mrz.pojo.Favourable;
import cn.mrz.pojo.Item;
import cn.mrz.service.BuyService;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.File;

/**
 * Created by Administrator on 2017/6/19.
 */
@Service
public class BuyServiceImpl implements BuyService {
    @Autowired
    ItemMapper itemMapper;
    @Autowired
    ItemDao itemDao;
    @Autowired
    FavourableMapper favourableDao;
    final String fileDictionary = "C://attach//";

    @Override
    public List<String> getBuyFileList() {
        List<String> buyFileList = new ArrayList<String>();
        File file = new File(fileDictionary);
        if (!file.exists()) {
            file.mkdir();
            return buyFileList;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File buyFile : files) {
                if (buyFile.isFile()) {
                    buyFileList.add(buyFile.getName());
                }
            }
        }
        return buyFileList;
    }

    @Override
    public boolean saveBuyFile(MultipartFile uploadFile) {
        File file = new File(fileDictionary);
        if (!file.exists()) {
            file.mkdir();
        }
        String originalFilename = uploadFile.getOriginalFilename();
        File buyFile = new File(file, originalFilename);
        if (buyFile.exists()) {
            throw new BuyFileExistException();
        }
        try {
            uploadFile.transferTo(buyFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean parseBuyFile(String buyFilePath) {
        int insertNum = 0;
        try {
            List<Item> items = parseData(buyFilePath);
            int batchSize = 100;
            int flag = 0;
            List<Item> itemBatch = new ArrayList<Item>();
            List<Favourable> favourableBatch = new ArrayList<Favourable>();
            long queryItemExist = 0;
            final Map<String,String[]> itemCLass = new HashMap<String,String[]>();
            for (Item item : items) {
                long start = System.currentTimeMillis();
                //页面返回事件45秒,这里使用35秒,在item_id上创建索引后这里使用3秒,页面响应时间12秒
                Item findItems = itemMapper.selectByItemId(item.getItemId());
                queryItemExist += System.currentTimeMillis() - start;
                if (findItems == null) {
                    flag++;
                    itemBatch.add(item);
                    insertNum++;
                    Favourable favourable = item.getFavourable();
                    if (favourable != null) {
                        favourableBatch.add(favourable);
                    }
                    itemCLass.put(item.getItemId(), item.getItemClass().split("/"));
                }
                if (flag == batchSize) {
                    itemMapper.insertItemList(itemBatch);
                    favourableDao.insertFavourableList(favourableBatch);
                    itemBatch = new ArrayList<Item>();
                    favourableBatch = new ArrayList<Favourable>();
                    flag = 0;
                }
            }
            System.out.println("查询商品是否存在使用了:" + queryItemExist + "ms");
            itemMapper.insertItemList(itemBatch);
            favourableDao.insertFavourableList(favourableBatch);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    new BuyServiceImpl().setItemClass(itemCLass, itemDao);
                }
            }
            ).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return insertNum != 0;
    }

    private void setItemClass(Map<String, String[]> itemClassMap, ItemDao itemDao) {
        Iterator<Map.Entry<String, String[]>> iterator = itemClassMap.entrySet().iterator();
        Map<Integer, List<String>> data = new HashMap<Integer, List<String>>();
        while (iterator.hasNext()) {
            Map.Entry<String, String[]> next = iterator.next();
            String itemId = next.getKey();
            String[] itemClasses = next.getValue();
            for (String itemClass : itemClasses) {
                int hashCode = itemClass.hashCode();
                if (!data.containsKey(hashCode)) {
                    ArrayList<String> itemIdList = new ArrayList<String>();
                    itemIdList.add(itemId);
                    data.put(hashCode, itemIdList);
                    itemDao.addItemClass(itemClass);
                } else {
                    List<String> itemIdList = data.get(hashCode);
                    itemIdList.add(itemId);
                    data.put(hashCode, itemIdList);
                }
            }
        }
        Iterator<Map.Entry<Integer, List<String>>> dataIterator = data.entrySet().iterator();
        while (dataIterator.hasNext()) {
            Map.Entry<Integer, List<String>> next = dataIterator.next();
            itemDao.setItemClass(next.getKey(), next.getValue());
        }
    }


    @Override
    public Page<Item> getItem(Page<Item> page) {
        page.setRecords(itemMapper.selectItem(page));
        return page;
    }

    @Override
    public Set<String> getItemClass() {
        return itemDao.getItemClassList();
    }

    @Override
    public List<String> getItemIdByClass(int itemClassHashCode) {
        return itemDao.getItemIdByClassHashcode(itemClassHashCode);
    }

    @Override
    public List<Item> getItemByItemIdList(List<String> itemIdList) {
        List<Item> items = new ArrayList<Item>();
        for(String itemId:itemIdList){
            items.add(itemMapper.selectByItemId(itemId));
        }
        return items;
    }

    private List<Item> parseData(String filePath) throws Exception {
        File file = new File(fileDictionary, filePath);
        InputStream is = new FileInputStream(file);
        try {
            POIFSFileSystem fs = new POIFSFileSystem(is);
            Workbook wb = new HSSFWorkbook(fs);
            Sheet sheet = wb.getSheetAt(0);
            int lastRowNum = sheet.getLastRowNum();
            List<Item> items = new ArrayList<Item>();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            List<String> itemIdList = new ArrayList<String>();
            for (int i = 1; i <= lastRowNum; i++) {
                Row row = sheet.getRow(i);
                String itemId = row.getCell(0).getStringCellValue();
                if (itemIdList.contains(itemId))
                    continue;
                itemIdList.add(itemId);

                String countStr = row.getCell(15).getStringCellValue();
                String surplusStr = row.getCell(16).getStringCellValue();
                String favourableTitle = row.getCell(17).getStringCellValue();
                String favourableStartDateStr = row.getCell(18).getStringCellValue();
                String favourableEndDateStr = row.getCell(19).getStringCellValue();
                String favourableTbkUrl = row.getCell(21).getStringCellValue();
                Long countNum = Long.parseLong(countStr);
                Long surplus = Long.parseLong(surplusStr);
                Date favourableStartDate = dateFormat.parse(favourableStartDateStr);
                Date favourableEndDate = dateFormat.parse(favourableEndDateStr);


                String title = row.getCell(1).getStringCellValue();
                String imageUrl = row.getCell(2).getStringCellValue();
                String detailUrl = row.getCell(3).getStringCellValue();
                String itemClass = row.getCell(4).getStringCellValue();
                //String tbkUrl = row.getCell(5).getStringCellValue();
                String priceStr = row.getCell(6).getStringCellValue();
                String salesVolumeStr = row.getCell(7).getStringCellValue();
                String myMoneyStr = row.getCell(9).getStringCellValue();
                String shopName = row.getCell(12).getStringCellValue();
                String shopType = row.getCell(13).getStringCellValue();//平台类型
                Float price = Float.parseFloat(priceStr);
                Float myMoney = Float.parseFloat(myMoneyStr);
                Long salesVolume = Long.parseLong(salesVolumeStr);

                Favourable favourable = new Favourable(countNum, new java.sql.Date(favourableEndDate.getTime()), itemId, null, new java.sql.Date(favourableStartDate.getTime()), surplus, null, favourableTbkUrl, favourableTitle);
                Item item = new Item(null, itemId, title, imageUrl, detailUrl, itemClass, favourableTbkUrl, price, myMoney, shopName, shopType, null, null, null, null, salesVolume, favourable);
                items.add(item);
            }
            return items;
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }
}
