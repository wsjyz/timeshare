package com.timeshare.controller.upload;

/**
 * Created by user on 2016/6/24.
 */
public class ProgressEntity {

    private long pBytesRead = 0L;
    private long pContentLength = 0L;
    private int pItems;
    public long getpBytesRead() {
        return pBytesRead;
    }
    public void setpBytesRead(long pBytesRead) {
        this.pBytesRead = pBytesRead;
    }
    public long getpContentLength() {
        return pContentLength;
    }
    public void setpContentLength(long pContentLength) {
        this.pContentLength = pContentLength;
    }
    public int getpItems() {
        return pItems;
    }
    public void setpItems(int pItems) {
        this.pItems = pItems;
    }
    @Override
    public String toString() {
        return "ProgressEntity [pBytesRead=" + pBytesRead + ", pContentLength="
                + pContentLength + ", pItems=" + pItems + "]";
    }

}
