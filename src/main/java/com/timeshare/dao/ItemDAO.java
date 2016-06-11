package com.timeshare.dao;

import com.timeshare.domain.OpenPage;
import com.timeshare.domain.Item;

/**
 * Created by adam on 2016/6/11.
 */
public interface ItemDAO {

    void saveItem(Item info);

    String modifyItem(Item Item);

    Item findItemByItemId(String ItemId);

    String deleteById(String itemId);

    OpenPage<Item> findItemPage(String mobile, String nickName, OpenPage page);

}
