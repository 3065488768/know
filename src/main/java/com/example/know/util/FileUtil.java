package com.example.know.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author bookWorm
 */
public class FileUtil {
    /**
     * 原始名字
     */
    private String fileName;
    /**
     * 后缀名
     */
    private String suffixName;
    /**
     * 保存路径
     */
    private String filePath;


    public String getReFileName() {
        return reFileName;
    }

    public void setReFileName(String reFileName) {
        this.reFileName = filePath + UUID.randomUUID() + fileName;
    }

    /**
     * 重命名后的名字
     */
    private String reFileName;
    private MultipartFile file;

    public FileUtil(MultipartFile file) {
        file = this.file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = file.getOriginalFilename();
    }

    public String getSuffixName() {
        return suffixName;
    }

    public void setSuffixName(String suffixName) {
        this.suffixName = fileName.substring(fileName.lastIndexOf("."));
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = "E:/file";
    }

    public boolean saveFile() {
        File dest = new File(reFileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
