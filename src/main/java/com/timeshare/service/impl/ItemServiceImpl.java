package com.timeshare.service.impl;

import com.timeshare.controller.ItemDTO;
import com.timeshare.dao.ImageObjDAO;
import com.timeshare.dao.ItemDAO;
import com.timeshare.domain.ImageObj;
import com.timeshare.domain.Item;
import com.timeshare.domain.OpenPage;
import com.timeshare.service.ItemService;
import com.timeshare.utils.Contants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2016/6/21.
 */
@Service("ItemService")
public class ItemServiceImpl implements ItemService {

    @Autowired
    ItemDAO itemDAO;
    @Autowired
    ImageObjDAO imageObjDAO;
    @Override
    public String saveItem(Item info) {
        return itemDAO.saveItem(info);
    }

    @Override
    public String modifyItem(Item item) {
        return itemDAO.modifyItem(item);
    }

    @Override
    public Item findItemByItemId(String itemId) {
        return itemDAO.findItemByItemId(itemId);
    }

    @Override
    public String deleteById(String itemId) {
        return null;
    }

    @Override
    public List<Item> findItemPage(Item item,int startIndex,int loadSize) {
        return itemDAO.findItemPage(item,startIndex,loadSize);
    }

    @Override
    public List<ItemDTO> findSellItemListByCondition(String condition, int startIndex, int loadSize) {

        List<Item> dtoDbList = itemDAO.findSellItemListByCondition(condition,startIndex,loadSize);


        return fixImg(dtoDbList);
    }

    @Override
    public List<Item> findItemList(Item item, int startIndex, int loadSize) {
        return itemDAO.findItemList(item,startIndex,loadSize);
    }

    @Override
    public List<ItemDTO> searchItemList(Item item, int startIndex, int loadSize) {
        List<Item> dtoDbList = itemDAO.findItemList(item,startIndex,loadSize);
        return fixImg(dtoDbList);
    }

    @Override
    public int findItemCount(Item item) {
        return itemDAO.findItemCount(item);
    }

    private List<ItemDTO> fixImg(List<Item> dtoDbList){
        List<ItemDTO> dtoList = new ArrayList<>();
        if(dtoDbList != null && !dtoDbList.isEmpty()){

            StringBuilder inStr = new StringBuilder("(");
            for(Item item:dtoDbList){
                inStr.append("'"+item.getUserId()+"',");
            }
            if (inStr.lastIndexOf(",") + 1 == inStr.length()) {
                inStr.delete(inStr.lastIndexOf(","), inStr.length());
            }
            inStr.append(")");
            List<ImageObj> imageObjList = imageObjDAO.findImgByObjIds(inStr.toString());
            Map<String,String> imgMap = new HashMap<>();
            for(ImageObj obj:imageObjList){
                imgMap.put(obj.getObjId()+obj.getImageType().toString(),obj.getImageUrl());
            }
            for(Item item:dtoDbList){
                ItemDTO itemDTO = new ItemDTO();
                itemDTO.setItem(item);
                String headImg = imgMap.get(item.getUserId()+Contants.IMAGE_TYPE.USER_HEAD.toString());
                //TODO context path is required
                if(headImg.indexOf("http") == -1){//修改过头像
                    headImg = "/time"+headImg+"_320x240.jpg";
                }
                itemDTO.setHeadImgPath(headImg);
                itemDTO.setImgPath(imgMap.get(item.getUserId()+Contants.IMAGE_TYPE.ITEM_SHOW_IMG.toString()));
                dtoList.add(itemDTO);
            }
        }
        return dtoList;
    }
}
