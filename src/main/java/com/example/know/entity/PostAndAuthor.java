package com.example.know.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author bookWorm
 */
@Data
public class PostAndAuthor {
    private int postId;
    private String postTitle;
    private int plateId;
    private char postType;
    private char status;
    private int degreeOfHeat;
    private int createBy;
    private LocalDateTime createTime;
    private LocalDateTime auditTime;
    private String nickName;
}
