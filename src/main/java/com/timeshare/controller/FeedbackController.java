package com.timeshare.controller;

import com.timeshare.domain.Feedback;
import com.timeshare.domain.Item;
import com.timeshare.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/7/21.
 */
@Controller
@RequestMapping(value = "/feedback")
public class FeedbackController extends BaseController {

    @Autowired
    FeedbackService feedbackService;

    @RequestMapping(value = "/list-by-item")
    @ResponseBody
    public List<Feedback> findFeedbackListByItem(@RequestParam String itemId) {
        List<Feedback> feedList = new ArrayList<Feedback>();
        feedList = feedbackService.findFeedbackListByItemId(itemId);
        return feedList;
    }

    @RequestMapping(value = "/list-by-touser")
    @ResponseBody
    public List<Feedback> findFeedbackListByToUser(@RequestParam String toUserId) {
        List<Feedback> feedList = new ArrayList<Feedback>();
        feedList = feedbackService.findFeedbackListByToUserId(toUserId);
        return feedList;
    }

    @RequestMapping(value = "/list-by-user-item")
    @ResponseBody
    public Feedback findFeedbackListByItemAndUser(@CookieValue(value="time_sid", defaultValue="") String userId,
                                                  @RequestParam String itemId) {
        Feedback feedback = feedbackService.findFeedBackByCreateUserId(userId,itemId);
        return feedback;
    }
}
