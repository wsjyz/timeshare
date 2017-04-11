package com.timeshare.service.impl.assembly;

import com.timeshare.dao.ImageObjDAO;
import com.timeshare.dao.assembly.CommentDAO;
import com.timeshare.dao.assembly.FeeDAO;
import com.timeshare.domain.ImageObj;
import com.timeshare.domain.UserInfo;
import com.timeshare.domain.assembly.Comment;
import com.timeshare.domain.assembly.Fee;
import com.timeshare.service.UserService;
import com.timeshare.service.assembly.CommentService;
import com.timeshare.service.assembly.FeeService;
import com.timeshare.utils.Contants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/9/23.
 */
@Service("CommentService")
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentDAO commentDAO;
    @Autowired
    UserService userService;
    @Autowired
    ImageObjDAO imageObjDAO;

    @Override
    public String saveComment(Comment Comment) {
        return commentDAO.saveComment(Comment);
    }

    @Override
    public String modifyComment(Comment Comment) {
        return commentDAO.modifyComment(Comment);
    }

    @Override
    public Comment findCommentById(String CommentId) {
        return commentDAO.findCommentById(CommentId);
    }

    @Override
    public List<Comment> findCommentList(Comment Comment) {
        return commentDAO.findCommentList(Comment);
    }

    @Override
    public int findCommentCount(Comment Comment) {
        return commentDAO.findCommentCount(Comment);
    }

    @Override
    public List<Comment> findCommentByObjId(String objId) {
        List<Comment> commentList= commentDAO.findCommentByObjId(objId);
        if (!CollectionUtils.isEmpty(commentList)){
            for (Comment comment:commentList){
                UserInfo userInfo=userService.findUserByUserId(comment.getUserId());
                comment.setUserName(userInfo.getNickName());
                ImageObj userImg = userService.findUserImg(comment.getUserId(), Contants.IMAGE_TYPE.USER_HEAD.toString());
                if(userImg!=null && StringUtils.isNotEmpty(userImg.getImageId())){
                    if(userImg!=null && StringUtils.isNotEmpty(userImg.getImageId())){
                        String headImg = userImg.getImageUrl();
                        if(headImg.indexOf("http") == -1){//修改过头像
                            headImg = "/time"+headImg+"_320x240.jpg";
                        }
                        comment.setUserImg(headImg);
                    }
                }
                List<ImageObj> imageObjList = imageObjDAO.findImgByObjIds("('"+comment.getCommentId()+"')");
                if(!CollectionUtils.isEmpty(imageObjList)){
                    List<String> commentImgList=new ArrayList<String>();
                    for (ImageObj imageObj:imageObjList){
                         commentImgList.add("/time"+imageObj.getImageUrl()+".jpg");
                    }
                    comment.setCommentImgList(commentImgList);
                }
                if (StringUtils.isNotEmpty(comment.getReplyContent())){
                    String[] replyContent=comment.getReplyContent().split("$#");
                    List<String> replyContentList=new ArrayList<String>();
                    for (int i=0;i<replyContent.length;i++){
                        replyContentList.add(replyContent[i]);
                    }
                    comment.setReplyContentList(replyContentList);
                }
            }
        }
        return commentList;
    }
}
