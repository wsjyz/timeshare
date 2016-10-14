package com.timeshare.controller;

import com.timeshare.domain.Bid;
import com.timeshare.domain.BidSubmit;
import com.timeshare.domain.SystemMessage;
import com.timeshare.domain.UserInfo;
import com.timeshare.service.BidService;
import com.timeshare.service.BidSubmitService;
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

    @RequestMapping(value = "/to-submit/{bidId}")
    public String toAdd(@PathVariable String bidId, Model model, HttpServletRequest request,
                        @CookieValue(value="time_sid", defaultValue="c9f7da60747f4cf49505123d15d29ac4") String currentUserId) {
        String bidCreatorUserId = "";
        if(StringUtils.isNotBlank(bidId)){
            Bid bid = bidService.findBidById(bidId);
            model.addAttribute("bid",bid);
            bidCreatorUserId = bid.getUserId();
        }
        //应标人的id
        String bidUserId = request.getParameter("bidUserId");
        if(StringUtils.isBlank(bidUserId)){
            bidUserId = currentUserId;
        }
        model.addAttribute("bidUserId",bidUserId);

        String audit = request.getParameter("audit");
        model.addAttribute("audit",audit);
        //用户信息
        UserInfo bidCreator = getCurrentUser(bidCreatorUserId);
        UserInfo currentUser = getCurrentUser(currentUserId);
        model.addAttribute("bidCreator",bidCreator);
        model.addAttribute("currentUser",currentUser);
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
    public List<BidSubmit> findSubmitList(String bidId,String bidUserId,@CookieValue(value="time_sid", defaultValue="c9f7da60747f4cf49505123d15d29ac4") String currentUserId) {

        BidSubmit bidSubmit = new BidSubmit();
        bidSubmit.setBidId(bidId);
        if(StringUtils.isBlank(bidUserId)){
            bidSubmit.setUserId(currentUserId);
        }else{
            bidSubmit.setUserId(bidUserId);
        }
        List<BidSubmit> bidSubmitList = bidSubmitService.findSubmitList(bidSubmit,0,0);
        return bidSubmitList;
    }
}
