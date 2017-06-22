package cn.mrz.service.impl;

import cn.mrz.dao.ItemDao;
import cn.mrz.exception.BuyFileExistException;
import cn.mrz.mapper.FavourableMapper;
import cn.mrz.mapper.ItemClassMapper;
import cn.mrz.mapper.ItemMapper;
import cn.mrz.pojo.Favourable;
import cn.mrz.pojo.Item;
import cn.mrz.pojo.ItemClass;
import cn.mrz.service.BuyService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/6/19.
 */
@Service
@Transactional
public class BuyServiceImpl implements BuyService {
    @Autowired
    ItemMapper itemMapper;
    @Autowired
    ItemDao itemDao;
    @Autowired
    FavourableMapper favourableMapper;
    @Autowired
    ItemClassMapper itemClassMapper;
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
            //按最后修改时间逆序
            List<File> fileList = Arrays.asList(files);
            Collections.sort(fileList, new Comparator<File>() {
                @Override
                public int compare(File file1, File file2) {
                    return (int) (file2.lastModified() - file1.lastModified());
                }
            });
            for (File buyFile : fileList) {
                buyFileList.add(buyFile.getName());
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
    public boolean deleteBuyFile(String buyFilePath) {
        File buyFile = new File(fileDictionary, buyFilePath);
        return buyFile.delete();
    }

    @Override
    public boolean parseBuyFile(String buyFilePath) {
        try {
            List<Item> items = parseData(buyFilePath);
            int batchSize = 100;
            int flag = 0;
            List<Item> itemBatch = new ArrayList<Item>();
            List<Favourable> favourableBatch = new ArrayList<Favourable>();
            List<ItemClass> itemClassBatch = new ArrayList<ItemClass>();
            List<String> itemClassTitleList = itemClassMapper.selectAllClassTitle();

            for (Item item : items) {
                //TODO 这里可以优化一下
                int existItem = itemMapper.hasItemId(item.getItemId());
                if (existItem < 1) {
                    flag++;
                    itemBatch.add(item);
                    Favourable favourable = item.getFavourable();
                    if (favourable != null) {
                        favourableBatch.add(favourable);
                    }
                    String itemClass = item.getItemClassString();
                    if (!itemClassTitleList.contains(itemClass)) {
                        itemClassBatch.add(new ItemClass(itemClass));
                        itemClassTitleList.add(itemClass);
                    }
                    if (flag == batchSize) {
                        if (itemBatch.size() > 0) {
                            itemMapper.insertItemList(itemBatch);
                            itemBatch = new ArrayList<Item>();
                        }
                        if (favourableBatch.size() > 0) {
                            favourableMapper.insertFavourableList(favourableBatch);
                            favourableBatch = new ArrayList<Favourable>();
                        }
                        if (itemClassBatch.size() > 0) {
                            itemClassMapper.insertItemClassList(itemClassBatch);
                            itemClassBatch = new ArrayList<ItemClass>();
                        }
                        flag = 0;
                    }
                }
            }
            if (itemBatch.size() > 0)
                itemMapper.insertItemList(itemBatch);
            if (favourableBatch.size() > 0)
                favourableMapper.insertFavourableList(favourableBatch);
            if (itemClassBatch.size() > 0)
                itemClassMapper.insertItemClassList(itemClassBatch);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void setItemClass(Map<String, String> itemClassMap, ItemDao itemDao) {
        Iterator<Map.Entry<String, String>> iterator = itemClassMap.entrySet().iterator();
        Map<Integer, List<String>> data = new HashMap<Integer, List<String>>();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            String itemId = next.getKey();
            String itemClass = next.getValue();
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
    public List<ItemClass> getItemClassByParentId(Long parentId) {
        EntityWrapper<ItemClass> entityWrapper = new EntityWrapper<ItemClass>();
        entityWrapper.where("parent_id={0}", parentId);
        return itemClassMapper.selectList(entityWrapper);
    }

    @Override
    public List<String> getItemIdByClass(int itemClassHashCode) {
        return itemDao.getItemIdByClassHashcode(itemClassHashCode);
    }

    @Override
    public List<Item> getItemByItemIdList(List<String> itemIdList) {
        List<Item> items = new ArrayList<Item>();
        for (String itemId : itemIdList) {
            items.add(itemMapper.selectByItemId(itemId));
        }
        return items;
    }

    @Override
    public List<Item> getCacheIndexItemList(String key) {
        return itemDao.getList(key);
    }

    @Override
    public int cacheIndexItemList(String key, List<Item> itemList) {
        itemDao.setList(key, itemList);
        return 0;
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
                String itemClassString = row.getCell(4).getStringCellValue();
                //String tbkUrl = row.getCell(5).getStringCellValue();
                String priceStr = row.getCell(6).getStringCellValue();
                String salesVolumeStr = row.getCell(7).getStringCellValue();
                String myMoneyStr = row.getCell(9).getStringCellValue();
                String shopName = row.getCell(12).getStringCellValue();
                String shopTypeOrg = row.getCell(13).getStringCellValue();//平台类型
                String shopType = "tmall";
                if ("淘宝".equals(shopTypeOrg)) {
                    shopType = "taobao";
                }
                Float price = Float.parseFloat(priceStr);
                Float myMoney = Float.parseFloat(myMoneyStr);
                Long salesVolume = Long.parseLong(salesVolumeStr);

                Favourable favourable = new Favourable(countNum, new java.sql.Date(favourableEndDate.getTime()), itemId, null, new java.sql.Date(favourableStartDate.getTime()), surplus, null, favourableTbkUrl, favourableTitle);
                Item item = new Item(null, itemId, title, imageUrl, detailUrl, itemClassString, favourableTbkUrl, price, myMoney, shopName, shopType, null, null, null, null, salesVolume, favourable);
                items.add(item);
            }
            return items;
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }
}
