package com.stoney.reactor.jerysey;

import com.stony.reactor.jersey.MimeType;
import com.stony.reactor.jersey.MimeTypeUtil;
import org.junit.Test;
import reactor.ipc.netty.http.server.SimpleHttpPredicate;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.*;
import java.util.stream.Collectors;

/**
 * <p>reactor-netty-ext
 * <p>com.stoney.reactor.jerysey
 *
 * @author stony
 * @version 上午9:59
 * @since 2018/3/1
 */
public class MimeTypesTest {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get(MimeTypeUtil.class.getResource("/mime.types").getPath());
        System.out.println("path里包含的路径数量：" + path.getNameCount());
        System.out.println("path的根路径： "+path.getRoot());
        //path的绝对路径
        //对比传统java.io, 取绝对路径 file.getAbsoluteFile()
        Path absolutePath = path.toAbsolutePath();
        System.out.println("path的绝对路径：");
        System.out.println(absolutePath);
        System.out.println("absolutePath的根路径： "+absolutePath.getRoot());
        System.out.println("absolutePath里包含的路径数量：" + absolutePath.getNameCount());
        System.out.println(absolutePath.getName(3));
        //以多个String来构建path
        Path path2 = Paths.get("g:", "publish" , "codes");
        System.out.println(path2);
        System.out.println(".......................");
        List<MimeType> types = Files.lines(path, StandardCharsets.UTF_8)
                .filter(s -> !s.startsWith("#"))
                .map(s -> s.replace(";", ""))
                .map(s -> {
                    String[] a = s.split(" ");
                    return a;
                })
                .filter(a -> a.length >= 2)
                .collect(
                        () -> new ArrayList<MimeType>(128),
                        (mimeTypes, strings) -> {
                            if(strings.length == 2) {
                                mimeTypes.add(new MimeType(strings[0],strings[1]));
                            } else {
                                String content = strings[0];
                                for (int i = 1, len = strings.length; i < len; i++) {
                                    mimeTypes.add(new MimeType(content, strings[i]));
                                }
                            }
                        }, ArrayList::addAll);

        System.out.println("size = " + types.size());
        System.out.println(types);

        Map<String, MimeType> suffixMap = types.stream().collect(Collectors.toMap(MimeType::getSuffix, mimeType -> mimeType));

        System.out.println("map = \n" + suffixMap.keySet());
        String url = "http://java.sun.com/test/test.css?a=1&b=2";
        URI uri = URI.create(url);
        System.out.println(uri.getPath());
        System.out.println(uri.getHost());
        System.out.println(uri.getQuery());

        System.out.println(">> " + uri.getPath().substring(uri.getPath().lastIndexOf("/")+1));
        System.out.println("---file----");
        System.out.println(MimeTypeUtil.getFileName(uri.getPath()));
        System.out.println(MimeTypeUtil.getFilePath(uri.getPath()));
        System.out.println("file sufffix");
        System.out.println(MimeTypeUtil.getFileSuffix(MimeTypeUtil.getFileName(uri.getPath())));
        System.out.println(MimeTypeUtil.getInstance().getMimeTypeByPath(uri.getPath()));

        System.out.println(path.getParent().resolve("mime.types").toAbsolutePath());

        System.out.println("---------");
        String prefix = "/test.html/";
        if (prefix.charAt(0) == '/') {
            prefix = prefix.substring(1);
        }
        System.out.println(prefix);


        if (prefix.charAt(prefix.length()-1) == '/') {
            prefix = prefix.substring(0,prefix.length()-1);
        }
        System.out.println(prefix);
    }

    @Test
    public void test_102(){
        String url = "http://java.sun.com/test/test.css?a=1&b=2";
        URI uri = URI.create(url);
        System.out.println(uri.getPath());
        System.out.println(uri.getHost());
        System.out.println(uri.getQuery());
        System.out.println("------------------");
        url = "/test/test.css?a=1&b=2&name=痘痘";

        uri = URI.create(url);
        System.out.println(uri.getPath());
        System.out.println(uri.getHost());
        System.out.println(uri.getQuery());
        System.out.println(uri.getRawQuery());
        System.out.println(SimpleHttpPredicate.parseParameters(uri.getQuery()));
    }


}
