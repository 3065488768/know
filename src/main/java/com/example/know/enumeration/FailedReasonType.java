package com.example.know.enumeration;

import lombok.Data;

/**
 * 帖子审核失败原因
 *
 * @author bookWorm
 */

public enum FailedReasonType {
    POLITICALREASONS(0, "政治原因"),
    PORNOGRAPHIC(1, "色情"),
    ILLEGAL(2, "违法"),
    VULGAR(3, "低俗"),
    GAMBLING(4, "赌博"),
    RUMOR(5, "谣言");

    private int type;
    private String content;

    FailedReasonType(int type, String content) {
        this.type = type;
        this.content = content;
    }

    public static String getContentByType(int type) {
        switch (type) {
            case 0:
                return POLITICALREASONS.getContent();
            case 1:
                return PORNOGRAPHIC.getContent();
            case 2:
                return ILLEGAL.getContent();
            case 3:
                return VULGAR.getContent();
            case 4:
                return GAMBLING.getContent();
            case 5:
                return RUMOR.getContent();
            default:
                return "其他";
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
