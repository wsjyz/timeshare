package com.timeshare.controller.manage;


import com.timeshare.controller.ItemDTO;
import com.timeshare.domain.*;
import com.timeshare.service.ItemService;
import com.timeshare.service.UserService;
import com.timeshare.utils.Contants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/7/12.
 */
@Controller
@RequestMapping(value = "/manager/item")
public class ItemManageController {

    @Autowired
    ItemService itemService;
    @Autowired
    UserService userService;

    @RequestMapping(value = "/list")
    @ResponseBody
    public List<ItemDTO> findItemList(@RequestParam String itemStatus, @RequestParam int startIndex, @RequestParam int loadSize) {


        Item parms = new Item();
        if(StringUtils.isNotBlank(itemStatus) && !itemStatus.equals("all")){
            parms.setItemStatus(itemStatus);
        }
        //parms.setItemStatus(Contants.ITEM_STATUS.apply_for_online.toString());
        List<ItemDTO> itemList = itemService.findItemManagerList(parms,startIndex,loadSize);
        return itemList;
    }
    @RequestMapping(value = "/modify")
    @ResponseBody
    public String modify(Item item) {
        if(StringUtils.isNotBlank(item.getItemStatus())){
            Item dbItem = itemService.findItemByItemId(item.getItemId());
            UserInfo userInfo = userService.findUserByUserId(dbItem.getUserId());
            int sellCount = userInfo.getSellCounts();
            if(item.getItemStatus().equals(Contants.ITEM_STATUS.for_sale.toString())){

                userInfo.setSellCounts(sellCount + 1);
                //userService.modifyUser(userInfo);
            }else if(item.getItemStatus().equals(Contants.ITEM_STATUS.undercarriage.toString())){
                if(sellCount > 0){
                    userInfo.setSellCounts(sellCount - 1);
                }

            }
        }

        String result = itemService.modifyItem(item);
        return result;
    }

    @RequestMapping(value = "/to-upload-img")
    public String toUploadImg(HttpServletRequest request, Model model) {
        String itemId = request.getParameter("itemId");
        Item item = itemService.findItemByItemId(itemId);
        String userId = item.getUserId();
        ImageObj imageObj = userService.findUserImg(userId, Contants.ITEM_SHOW_IMG);
        String objId = "";
        String imgType = "";
        String imageId = "";
        if(imageObj == null){
            objId = userId;
            imgType = Contants.ITEM_SHOW_IMG;
        }else{
            objId = imageObj.getObjId();
            imgType = imageObj.getImageType();
            imageId = imageObj.getImageId();
            model.addAttribute("imgPath",request.getContextPath()+imageObj.getImageUrl()+".jpg");
        }
        model.addAttribute("objId",objId);
        model.addAttribute("imageType",imgType);
        model.addAttribute("imageId",imageId);
        return "uploadimg";
    }
    @RequestMapping(value = "/to-item-manage")
    public String toUserManage(Model model){
        return "manager/item";
    }
    @RequestMapping(value = "/to-yue-manage")
    public String toYueList(Model model){
        return "manager/yue";
    }

    @ResponseBody
    @RequestMapping(value = "/find-item-count")
    public int findItemCount(Item item){
        int count = itemService.findItemCount(item);
        return count;
    }

    @RequestMapping(value = "/get-item")
    @ResponseBody
    public Item getItemById(@RequestParam String itemId) {
        Item item = itemService.findItemByItemId(itemId);
        if(item == null){
            item = new Item();
        }
        return item;
    }
}
