package com.example.know.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class CommentTree {

    private CommentAndAuthor comment;

    private List<CommentTree> children = new ArrayList<>();
}
