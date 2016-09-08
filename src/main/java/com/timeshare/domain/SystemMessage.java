package com.timeshare.domain;

/**
 * Created by user on 2016/6/22.
 */
public class SystemMessage {

    private String messageType;
    private String content;
    private String objId;//传值用，例如保存的时候可以用来传递id

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
