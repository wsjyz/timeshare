package com.timeshare.domain.assembly;


import com.timeshare.domain.BaseDomain;

import java.util.List;

/**
 * Created by user on 2017/3/24.
 * 评论
 */
public class Comment extends BaseDomain {

    private String commentId;
    //内容
    private String content;
    //打分
    private int rating;
    //赞内容以&符号分开，例如$张三$李四
    private String zanContent;
    //评论内容以&#符号分开，例如$#张三：很好$#李四：非常棒
    private String replyContent;
    //评论类型
    private String objType;
    //评论所属对象id
    private String objId;
    private String userId;
    private String createTime;
    private String userImg;
    private String userName;
    private String acountType;
    private List<String> commentImgList;
    private List<String> replyContentList;

    public void setReplyContentList(List<String> replyContentList) {
        this.replyContentList = replyContentList;
    }

    public List<String> getReplyContentList() {
        return replyContentList;
    }

    public void setCommentImgList(List<String> commentImgList) {
        this.commentImgList = commentImgList;
    }

    public List<String> getCommentImgList() {
        return commentImgList;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAcountType() {
        return acountType;
    }

    public void setAcountType(String acountType) {
        this.acountType = acountType;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getObjType() {
        return objType;
    }

    public void setObjType(String objType) {
        this.objType = objType;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getZanContent() {
        return zanContent;
    }

    public void setZanContent(String zanContent) {
        this.zanContent = zanContent;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }
}
