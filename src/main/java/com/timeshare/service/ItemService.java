package com.timeshare.service;

import com.timeshare.domain.Item;
import com.timeshare.domain.OpenPage;

/**
 * Created by adam on 2016/6/11.
 */
public interface ItemService {

    void saveItem(Item info);

    String modifyItem(Item Item);

    Item findItemByItemId(String ItemId);

    String deleteById(String itemId);

    OpenPage<Item> findItemPage(String mobile, String nickName, OpenPage page);

}
