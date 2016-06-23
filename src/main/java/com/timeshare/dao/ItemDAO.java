package com.timeshare.dao;

import com.timeshare.domain.OpenPage;
import com.timeshare.domain.Item;

import java.util.List;

/**
 * Created by adam on 2016/6/11.
 */
public interface ItemDAO {

    String saveItem(Item info);

    String modifyItem(Item Item);

    Item findItemByItemId(String ItemId);

    String deleteById(String itemId);

    List<Item> findItemPage(Item item, int startIndex, int loadSize);

}
