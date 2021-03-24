package com.example.elsearch.Utils;

import com.example.elsearch.User.Content;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: HtmlParseUtil
 * @Description: TODO
 * @author: 大佬
 * @Date: 2021/3/24 22:29
 * @Version: 1.0
 **/
@Component
public class HtmlParseUtil {
    public static void main(String[] args) throws IOException {
        parseJD("java").forEach(System.out::println);
    }

    public static  List<Content> parseJD(String keywords) throws IOException {
        //获取请求
        String url="https://search.jd.com/Search?keyword="+keywords;
        //解析网页浏览器docmet对象
        Document document = Jsoup.parse(new URL(url), 40000);
        Element element = document.getElementById("J_goodsList");
        Elements elements = element.getElementsByTag("li");
        List<Content> list =new ArrayList<>();
        for(Element el:elements){
            String img = el.getElementsByTag("img").eq(0).attr("data-lazy-img");
            String price = el.getElementsByClass("p-price").eq(0).text();
            String title = el.getElementsByClass("p-name").eq(0).text();
            Content content =new Content();
            content.setTitle(title);
            content.setImg(img);
            content.setPrice(price);
            list.add(content);
        }
        return list;
    }
}
