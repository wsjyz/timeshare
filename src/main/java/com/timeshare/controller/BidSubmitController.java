package com.timeshare.controller;

import com.timeshare.utils.WxUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by user on 2016/9/27.
 */
@Controller
@RequestMapping(value = "/bidsubmit")
public class BidSubmitController {

    @RequestMapping(value = "/to-add")
    public String toAdd(Model model, HttpServletRequest request) {

        //微信jssdk相关代码
        String url = WxUtils.getUrl(request);
        Map<String,String> parmsMap = WxUtils.sign(url);
        model.addAttribute("parmsMap",parmsMap);
        return "bid/bidsubmit";
    }
}
