package com.timeshare.domain;



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
