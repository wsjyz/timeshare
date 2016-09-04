package com.timeshare.service.impl;

import com.timeshare.dao.FeedbackDAO;
import com.timeshare.domain.Feedback;
import com.timeshare.domain.OpenPage;
import com.timeshare.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by user on 2016/7/20.
 */
@Service("FeedbackService")
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    FeedbackDAO feedbackDAO;

    @Override
    public String saveFeedback(Feedback info) {
        return feedbackDAO.saveFeedback(info);
    }

    @Override
    public String modifyFeedback(Feedback itemFeedback) {
        return null;
    }

    @Override
    public Feedback findFeedbackByFeedbackId(String FeedbackId) {
        return null;
    }

    @Override
    public String deleteById(String FeedbackId) {
        return null;
    }

    @Override
    public OpenPage<Feedback> findFeedbackPage(String mobile, String nickName, OpenPage page) {
        return null;
    }

    @Override
    public List<Feedback> findFeedbackListByToUserId(String toUserId) {
        return feedbackDAO.findFeedbackListByToUserId(toUserId);
    }

    @Override
    public List<Feedback> findFeedbackListByItemId(String itemId) {
        return feedbackDAO.findFeedbackListByItemId(itemId);
    }

    @Override
    public Feedback findFeedBackByCreateUserId(String createUserId,String itemId) {
        return feedbackDAO.findFeedBackByCreateUserId(createUserId,itemId);
    }

    @Override
    public Feedback findFeedBackByOrderId(String createUserId, String orderId) {
        return feedbackDAO.findFeedBackByOrderId(createUserId,orderId);
    }

    @Override
    public int findItemTotalScore(String itemId,String itemCreateUserId) {
        return feedbackDAO.findItemTotalScore(itemId,itemCreateUserId);
    }
}
