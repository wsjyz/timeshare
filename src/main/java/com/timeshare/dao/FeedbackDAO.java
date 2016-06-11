package com.timeshare.dao;

import com.timeshare.domain.Feedback;
import com.timeshare.domain.OpenPage;

/**
 * Created by adam on 2016/6/11.
 */
public interface FeedbackDAO {

    void saveFeedback(Feedback info);

    String modifyFeedback(Feedback itemFeedback);

    Feedback findFeedbackByFeedbackId(String FeedbackId);

    String deleteById(String FeedbackId);

    OpenPage<Feedback> findFeedbackPage(String mobile, String nickName, OpenPage page);
    
    
}
