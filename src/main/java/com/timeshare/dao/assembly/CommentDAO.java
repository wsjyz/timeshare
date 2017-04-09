package com.timeshare.dao.assembly;

import com.timeshare.domain.assembly.Comment;

import java.util.List;


public interface CommentDAO {

     String saveComment(Comment Comment);

    String modifyComment(Comment Comment);

    Comment findCommentById(String CommentId);

    List<Comment> findCommentList(Comment Comment);
    int findCommentCount(Comment Comment);
    List<Comment> findCommentByObjId(String objId);

}
