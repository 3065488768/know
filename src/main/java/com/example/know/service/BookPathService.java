package com.example.know.service;

import com.example.know.entity.BookPath;

import java.util.List;

/**
 * @author bookWorm
 */
public interface BookPathService {

    /**
     * 有效路径
     *
     * @param bookId 书籍id
     * @return
     */
    public String getPathOfValid(int bookId);

    /**
     * 插入书籍路径
     */
    public int insert(BookPath bookPath);

    public List<BookPath> getPath(int bookId);

    public int updatePath(int bookId, String path);
}
