package com.stony.reactor.jersey;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>reactor-netty-ext
 * <p>com.stony.reactor.jersey
 *
 * @author stony
 * @version 上午10:06
 * @since 2018/3/1
 */
public class MimeType {
    String contentType;
    String suffix;
    private String fileName;

    public MimeType(String contentType, String suffix) {
        this.contentType = contentType;
        this.suffix = suffix;
    }

    public String getContentType() {
        return contentType;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public String toString() {
        return "MimeType{" +
                "contentType='" + contentType + '\'' +
                ", suffix='" + suffix + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }



}
