package com.timeshare.controller;

import com.alibaba.fastjson.JSON;
import com.timeshare.dao.ImageObjDAO;
import com.timeshare.domain.ImageObj;
import com.timeshare.domain.Item;
import com.timeshare.domain.SystemMessage;
import com.timeshare.domain.UserInfo;
import com.timeshare.service.ItemService;
import com.timeshare.service.UserService;
import com.timeshare.utils.Contants;
import com.timeshare.utils.WxUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2016/6/21.
 */
@Controller
@RequestMapping(value = "/item")
public class ItemController extends BaseController{

    @Autowired
    ItemService itemService;
    @Autowired
    UserService userService;
    @Autowired
    ImageObjDAO imageObjDAO;

    @RequestMapping(value = "/to-add")
    public String toAdd(String itemJson,ModelMap modelMap) {
        if(StringUtils.isNotBlank(itemJson)){
            try {
                itemJson = URLDecoder.decode(itemJson,"UTF-8");
                Item item = JSON.parseObject(itemJson,Item.class);
                modelMap.addAttribute("item", item);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return "additem";
    }

    @RequestMapping(value = "/to-list")
    public String itemList() {
        return "selltime";
    }

    @RequestMapping(value = "/to-search")
    public String toSearch(@RequestParam String keyword,Model model) {

        model.addAttribute("keyword",keyword);
        return "searchitem";
    }

    @RequestMapping(value = "/search")
    @ResponseBody
    public List<ItemDTO> search(@RequestParam String keyword ,@RequestParam int startIndex, @RequestParam int loadSize) {
        Item item = new Item();
        item.setTitle(keyword);
        item.setCreateUserName(keyword);
        item.setItemStatus(Contants.ITEM_STATUS.for_sale.toString());
        List<ItemDTO> itemList = itemService.searchItemList(item,startIndex,loadSize);
        return itemList;
    }

    @RequestMapping(value = "/to-view/{itemId}")
    public String toView(@PathVariable String itemId,Model model,@CookieValue(value="time_sid", defaultValue="") String userId,HttpServletRequest request) {
        String returnStr = "";
        Item item = itemService.findItemByItemId(itemId);
        if(item != null &&
                ( item.getItemStatus().equals(Contants.ITEM_STATUS.draft.toString())
                        || item.getItemStatus().equals(Contants.ITEM_STATUS.not_pass.toString())
                        || item.getItemStatus().equals(Contants.ITEM_STATUS.undercarriage.toString())
                )){
            model.addAttribute("item", item);
            returnStr = "additem";
        }else{
            String selfItem = "no";
            if(StringUtils.isNotBlank(userId) && userId.equals(item.getUserId())){
                selfItem = "yes";
            }
            returnStr = "iteminfo";
            model.addAttribute("itemId",itemId);
            model.addAttribute("selfItem",selfItem);
            model.addAttribute("itemStatus",item.getItemStatus());
            //微信jssdk相关代码
            String url = WxUtils.getUrl(request);
            Map<String,String> parmsMap = WxUtils.sign(url);
            model.addAttribute("parmsMap",parmsMap);
        }

        return returnStr;
    }

    @RequestMapping(value = "/get-item")
    @ResponseBody
    public ItemDTO getItem(@RequestParam String itemId, @RequestParam(required = false,defaultValue = "") String userId, HttpServletRequest request) {
        Item item = new Item();
        ItemDTO itemDTO = new ItemDTO();
        if(StringUtils.isNotBlank(itemId)){
            item = itemService.findItemByItemId(itemId);
            itemDTO.setItem(item);
        }
        ImageObj imageObj = new ImageObj();
        if(StringUtils.isBlank(userId)){
            userId = item.getUserId();
        }
        imageObj.setImageType(Contants.IMAGE_TYPE.ITEM_SHOW_IMG.toString());
        UserInfo userInfo = userService.findUserByUserId(userId,imageObj);
        ImageObj headObj = imageObjDAO.findImgByObjIdAndType(userId,Contants.IMAGE_TYPE.USER_HEAD.toString());
        String headImg = headObj.getImageUrl();
        if(headImg.indexOf("http") == -1){//修改过头像
            headImg = request.getContextPath()+headImg+"_320x240.jpg";
        }
        userInfo.setHeadImgPath(headImg);
        itemDTO.setUserInfo(userInfo);
        return itemDTO;

    }


    @RequestMapping(value = "/list")
    @ResponseBody
    public List<Item> findItemList(@RequestParam int startIndex, @RequestParam int loadSize,
                                   @CookieValue(value="time_sid", defaultValue="") String userId) {
        List<Item> itemList = new ArrayList<Item>();
        Item parms = new Item();
        parms.setUserId(userId);
        itemList = itemService.findItemPage(parms,startIndex,loadSize);
        return itemList;
    }

    @RequestMapping(value = "/list-by-condition")
    @ResponseBody
    public List<ItemDTO> listByCondition(@RequestParam String condition ,@RequestParam int startIndex, @RequestParam int loadSize) {
        List<ItemDTO> itemList = itemService.findSellItemListByCondition(condition,startIndex,loadSize);
        return itemList;
    }

    @RequestMapping(value = "/save")
    public String save(Item item,@CookieValue(value="time_sid", defaultValue="") String userId,Model model) {
        SystemMessage message = saveItem(item,userId);
        model.addAttribute("message",message);
        model.addAttribute("jumpUrl","/item/to-list");
        return "info";
    }
    @ResponseBody
    @RequestMapping(value = "/save-async")
    public SystemMessage save(Item item,@CookieValue(value="time_sid", defaultValue="") String userId) {
        SystemMessage message = saveItem(item,userId);
        return message;
    }
    private SystemMessage saveItem(Item item,String userId){
        SystemMessage message = new SystemMessage();
        if(item != null){
            UserInfo user = getCurrentUser(userId);
            item.setCreateUserName(user.getNickName());
            item.setUserId(userId);
            String result = "";
            //TODO 测试时为1分
            //item.setPrice(new BigDecimal("0.01"));
            if(StringUtils.isNotBlank(item.getItemId())){
                result = itemService.modifyItem(item);
            }else{
                if(StringUtils.isNotBlank(item.getTitle()) && item.getPrice() != null){
                    result = itemService.saveItem(item);
                }

            }

            message.setObjId(result);
            if(!result.equals(Contants.FAILED)){
                message.setContent("添加成功！");
                message.setMessageType(Contants.SUCCESS);
            }else{
                message.setContent("添加失败！");
                message.setMessageType(Contants.FAILED);
            }
        }
        return message;
    }
    @RequestMapping(value = "/modify-item-status")
    public String modifyItemStatus(@RequestParam String itemId,@RequestParam String itemStatus,Model model){

        Item item = itemService.findItemByItemId(itemId);
        item.setItemStatus(itemStatus);
        String result = itemService.modifyItem(item);
        SystemMessage message = new SystemMessage();
        if(result.equals(Contants.SUCCESS)){
            message.setContent("修改成功！");
            message.setMessageType(result);
        }
        model.addAttribute("message",message);
        model.addAttribute("jumpUrl","/item/to-list");
        return "info";
    }

    @RequestMapping(value = "/preview")
    public String preview(Item item, ModelMap modelMap,
                          @CookieValue(value="time_sid", defaultValue="admin") String userId) {
        modelMap.addAttribute("item",item);
        UserInfo userInfo = getCurrentUser(userId);
        modelMap.addAttribute("user",userInfo);
        String itemJson = JSON.toJSONString(item);
        try {
            modelMap.addAttribute("itemJson", URLEncoder.encode(itemJson,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "previewitem";
    }
}
