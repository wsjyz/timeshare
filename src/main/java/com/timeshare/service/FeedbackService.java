package com.timeshare.service;

import com.timeshare.domain.Feedback;
import com.timeshare.domain.OpenPage;

import java.util.List;

/**
 * Created by adam on 2016/6/11.
 */
public interface FeedbackService {

    String saveFeedback(Feedback info);

    String modifyFeedback(Feedback itemFeedback);

    Feedback findFeedbackByFeedbackId(String FeedbackId);

    String deleteById(String FeedbackId);

    OpenPage<Feedback> findFeedbackPage(String mobile, String nickName, OpenPage page);

    List<Feedback> findFeedbackListByToUserId(String toUserId);

    List<Feedback> findFeedbackListByItemId(String itemId);

    Feedback findFeedBackByCreateUserId(String createUserId,String itemId);

    Feedback findFeedBackByOrderId(String createUserId,String order);

    /**
     * 获取项目的总分，不包括自己打的分
     * @param itemCreateUserId
     * @return
     */
    int findItemTotalScore(String itemId,String itemCreateUserId);

}
