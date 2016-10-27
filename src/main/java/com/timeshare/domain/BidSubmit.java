package com.timeshare.domain;



/**
 * Created by user on 2016/9/22.
 */
public class BidSubmit extends BaseDomain {

    private String bidSubmitId;
    private String bidSubmitText;
    private String bidSubmitType;
    private String bidId;
    private String wxServerId;
    private String serverPath;
    private String otherUserId;

    //传值用
    private String bidCreateUser;

    public String getOtherUserId() {
        return otherUserId;
    }

    public void setOtherUserId(String otherUserId) {
        this.otherUserId = otherUserId;
    }

    public String getBidCreateUser() {
        return bidCreateUser;
    }

    public void setBidCreateUser(String bidCreateUser) {
        this.bidCreateUser = bidCreateUser;
    }

    public String getBidSubmitId() {
        return bidSubmitId;
    }

    public void setBidSubmitId(String bidSubmitId) {
        this.bidSubmitId = bidSubmitId;
    }

    public String getBidSubmitText() {
        return bidSubmitText;
    }

    public void setBidSubmitText(String bidSubmitText) {
        this.bidSubmitText = bidSubmitText;
    }

    public String getBidSubmitType() {
        return bidSubmitType;
    }

    public void setBidSubmitType(String bidSubmitType) {
        this.bidSubmitType = bidSubmitType;
    }

    public String getBidId() {
        return bidId;
    }

    public void setBidId(String bidId) {
        this.bidId = bidId;
    }

    public String getWxServerId() {
        return wxServerId;
    }

    public void setWxServerId(String wxServerId) {
        this.wxServerId = wxServerId;
    }

    public String getServerPath() {
        return serverPath;
    }

    public void setServerPath(String serverPath) {
        this.serverPath = serverPath;
    }
}
