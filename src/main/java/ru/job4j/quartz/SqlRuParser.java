package ru.job4j.quartz;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SqlRuParser {
    public static void main(String[] args) throws Exception {
        Document doc;
        for (int i = 0; i < 6; i++) {
            if (i == 0) {
                doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
            } else {
                doc = Jsoup.connect("https://www.sql.ru/forum/job-offers/" + i).get();
            }
            Elements row = doc.select(".postslisttopic");
            for (Element td : row) {
                Element href = td.child(0);
                System.out.println(href.attr("href"));
                System.out.println(href.text());
            }
        }
/*        Elements d = doc.select(".altCol");
        for (Element td : d) {
            Elements innerElements = td.children();
            if (innerElements.isEmpty()){
                System.out.println(td.text());
            }
        }*/
    }
}
