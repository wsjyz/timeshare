package com.timeshare.service.impl;

import com.timeshare.controller.ItemDTO;
import com.timeshare.dao.ItemDAO;
import com.timeshare.domain.Item;
import com.timeshare.domain.OpenPage;
import com.timeshare.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by user on 2016/6/21.
 */
@Service("ItemService")
public class ItemServiceImpl implements ItemService {

    @Autowired
    ItemDAO itemDAO;
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
        return itemDAO.findSellItemListByCondition(condition,startIndex,loadSize);
    }

    @Override
    public List<Item> findItemList(Item item, int startIndex, int loadSize) {
        return itemDAO.findItemList(item,startIndex,loadSize);
    }

    @Override
    public int findItemCount(Item item) {
        return itemDAO.findItemCount(item);
    }
}
