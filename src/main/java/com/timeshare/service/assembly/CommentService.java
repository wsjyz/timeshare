package com.timeshare.service.assembly;

import com.timeshare.domain.assembly.Comment;
import com.timeshare.domain.assembly.Fee;

import java.util.List;

/**
 * Created by user on 2016/9/23.
 */
public interface CommentService {


    String saveComment(Comment Comment);

    String modifyComment(Comment Comment);

    Comment findCommentById(String CommentId);

    List<Comment> findCommentList(Comment Comment);
    int findCommentCount(Comment Comment);
    List<Comment> findCommentByObjId(String objId);

}
