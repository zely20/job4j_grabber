package ru.job4j.quartz;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class SqlRuParser implements Parse {

    public static void main(String[] args) {
        SqlRuParser t = new SqlRuParser();
        try {
            t.detail("https://www.sql.ru/forum/1329768/stazher-razrabotchik-abap-vnutrenniy-yazyk-sap");
            t.list("https://www.sql.ru/forum/job-offers").stream()
                    .forEach(s -> System.out.println(s.getLink() + "  " + s.getName() + "  " + s.getDateCreated()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> list(String link) throws IOException {
        ParseDate parseDate = new ParseDate();
        List<Post> result = new LinkedList<>();
        Document doc = Jsoup.connect(link).get();
        Post post = null;
        Elements row = doc.select(".postslisttopic");
        for (Element td : row) {
            post = new Post();
            Element href = td.child(0);
            post.setLink(href.attr("href"));
            post.setName(href.text());
            Elements d = doc.select(".altCol");
            for (Element date : d) {
                Elements innerElements = date.children();
                if (innerElements.isEmpty()) {
                    post.setDateCreated(parseDate.parseDate(date.text()));

                }
            }
            result.add(post);
        }
        return result;
    }

    @Override
    public Post detail(String link) throws IOException {
        Post post = new Post();
        ParseDate parseDate = new ParseDate();
        Document doc = Jsoup.connect(link).get();
        Element elements = doc.selectFirst("table.msgTable.msBody");
        System.out.println(elements);
   /*    for (Element el : elements){
           Elements innerElements = el.children();

           System.out.println(innerElements);
           if (!innerElements.isEmpty()) {
               System.out.println(el);;
           }
            System.out.println(el);
        }*/
        //Element bodyElement = elements.text();
       /* System.out.println(bodyElement);
        System.out.println(bodyElement.text());
            post.setLink(bodyElement.text());*/
           // post.setName(href.text());
        System.out.println(post.getText());
       /* Elements d = doc.select(".altCol");
        for (Element td : d) {
            Elements innerElements = td.children();
            if (innerElements.isEmpty()) {
                post.setDateCreated(parseDate.parseDate(td.text()));
            }
        }*/
        return post;
    }

   /* public static void main(String[] args) throws Exception {
        Document doc = null;
        ParseDate parseDate = new ParseDate();


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
        Elements d = doc.select(".altCol");
        for (Element td : d) {
            Elements innerElements = td.children();
            if (innerElements.isEmpty()) {
                System.out.println(td.text() + " = " + parseDate.parseDate(td.text()));
            }
        }
    }*/
}