package com.timeshare.controller;

import com.timeshare.domain.*;
import com.timeshare.service.AuditorService;
import com.timeshare.service.BidService;
import com.timeshare.service.BidSubmitService;
import com.timeshare.service.BidUserService;
import com.timeshare.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2016/9/27.
 */
@Controller
@RequestMapping(value = "/bidsubmit")
public class BidSubmitController extends BaseController{

    @Autowired
    BidSubmitService bidSubmitService;
    @Autowired
    BidService bidService;
    @Autowired
    BidUserService bidUserService;

    /**
     * 有这几种情况
     * 点击我发的飚，查看详情，需要的参数：此飚创建人，应飚人，当前用户
     * 旁听，需要的参数：此飚创建人，中飚人
     * 我接的飚，需要的参数：此飚创建人，当前用户
     * @param bidId
     * @param model
     * @param request
     * @param currentUserId 当前用户
     * @return
     */
    @RequestMapping(value = "/to-submit/{bidId}")
    public String toAdd(@PathVariable String bidId, Model model, HttpServletRequest request,
                        @CookieValue(value="time_sid", defaultValue="c9f7da60747f4cf49505123d15d29ac4") String currentUserId) {
        String bidCreatorUserId = "";
        String otherUserId = "";
        if(StringUtils.isNotBlank(bidId)){
            Bid bid = bidService.findBidById(bidId);
            model.addAttribute("bid",bid);
            bidCreatorUserId = bid.getUserId();
        }
        //应标人的id
        String bidUserId = request.getParameter("bidUserId");
        if(StringUtils.isBlank(bidUserId)){
            otherUserId = currentUserId;//如果没有bidUserId，就说明是我接的飚
        }else{
            otherUserId = bidUserId;
        }

        String audit = request.getParameter("audit");
        if(StringUtils.isNotBlank(audit)){//中飚人
            BidUser bidUser = new BidUser();
            bidUser.setWinTheBid("1");
            List<BidUser> winUserList = bidUserService.findBidUserList(bidUser,0,1);
            if(winUserList != null){
                String winUserId = winUserList.get(0).getUserId();
                otherUserId = winUserId;
            }
        }
        model.addAttribute("audit",audit);
        //用户信息
        UserInfo bidCreator = getCurrentUser(bidCreatorUserId);
        UserInfo currentUser = getCurrentUser(otherUserId);
        model.addAttribute("bidCreator",bidCreator);
        model.addAttribute("otherUser",currentUser);
        model.addAttribute("currentUserId",currentUserId);
        //微信jssdk相关代码
        String url = WxUtils.getUrl(request);
        Map<String,String> parmsMap = WxUtils.sign(url);
        model.addAttribute("parmsMap",parmsMap);

        return "bid/bidsubmit";
    }

    @ResponseBody
    @RequestMapping(value = "/addtext")
    public SystemMessage addText(String bidId,String text,@CookieValue(value="time_sid", defaultValue="c9f7da60747f4cf49505123d15d29ac4") String userId) {

        BidSubmit bidSubmit = saveSubmitBase(userId,bidId);
        bidSubmit.setBidSubmitType(Contants.BID_SUBMIT_TYPE.TEXT.toString());
        bidSubmit.setBidSubmitText(text);

        String result = bidSubmitService.saveBidSubmit(bidSubmit);
        return getSystemMessage(result);
    }

    public BidSubmit saveSubmitBase(String userId,String bidId){
        BidSubmit bidSubmit = new BidSubmit();
        UserInfo user = getCurrentUser(userId);
        bidSubmit.setCreateUserName(user.getNickName());
        bidSubmit.setUserId(userId);
        bidSubmit.setBidId(bidId);
        return bidSubmit;
    }
    @ResponseBody
    @RequestMapping(value = "/download-file")
    public String downloadFile(String bidId,String serverId,String fileType,@CookieValue(value="time_sid", defaultValue="c9f7da60747f4cf49505123d15d29ac4") String userId){
        OkhttpClient client = new OkhttpClient();
        String accessToken = WxUtils.getAccessToken().getAccess_token();
        String fileUrl = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token="+accessToken+"&media_id="+serverId;
        String savePath = Contants.FILE_SAVE_PATH+"/bidsubmit/"+bidId+"/";
        client.asyncDownloadFile(fileUrl, savePath, new HttpClientCallback() {
            @Override
            public String call(OkhttpClient.FileBean fileBean) {
                BidSubmit bidSubmit = saveSubmitBase(userId,bidId);
                bidSubmit.setBidSubmitType(fileType);
                bidSubmit.setWxServerId(serverId);
                //bidSubmit.setServerPath(bidId +"/"+ fileBean.getFileName());
                if(fileType.equals(Contants.BID_SUBMIT_TYPE.VOICE.toString())){
                    VoiceFileUtils.amrToMp3(savePath+serverId+".amr");
                }

                bidSubmitService.saveBidSubmit(bidSubmit);
                return null;
            }
        });
        return "success";
    }

    @ResponseBody
    @RequestMapping(value = "/findsubmitlist")
    public List<BidSubmit> findSubmitList(String bidId,String otherUserId,@CookieValue(value="time_sid", defaultValue="c9f7da60747f4cf49505123d15d29ac4") String currentUserId) {

        BidSubmit bidSubmit = new BidSubmit();
        bidSubmit.setBidId(bidId);
        Bid bid = bidService.findBidById(bidId);
        bidSubmit.setBidCreateUser(bid.getUserId());
        bidSubmit.setUserId(otherUserId);
        List<BidSubmit> bidSubmitList = bidSubmitService.findSubmitList(bidSubmit,0,0);
        return bidSubmitList;
    }
}
