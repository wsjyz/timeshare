package com.timeshare.controller.manage;

import com.timeshare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by user on 2016/9/12.
 */
@Controller
@RequestMapping(value = "/manager/user")
public class UserManageController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/to-user-manage")
    public String toUserManage(Model model){
        int totalCount = userService.findUserCount(null);
        int hasMobileCount = userService.findUserHasMobileCount();
        model.addAttribute("totalCount",totalCount);
        model.addAttribute("hasMobileCount",hasMobileCount);
        return "manager/user";
    }
}
