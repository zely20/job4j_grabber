package ru.job4j.quartz;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlRuParser implements Parse {

    private Pattern patternOne = Pattern.compile("\\d+\\s\\D{3}\\s\\d{2},\\s\\d{2}:\\d{2}");
    private Pattern patternTwo = Pattern.compile("\\D+,\\s\\d{2}:\\d{2}");

    private String getDateFromString(String dateString) throws Exception {
        if (dateString.contains("сегодня") || dateString.contains("вчера")) {
            Matcher matcher = patternTwo.matcher(dateString);
            if (matcher.find()) {
                return matcher.group();
            }
        } else {
            Matcher matcher = patternOne.matcher(dateString);
            if (matcher.find()) {
                return matcher.group();
            }
        }
        throw new Exception("String date is not find");
    }

    @Override
    public List<Post> list(String link) throws IOException {
        List<Post> result = new LinkedList<>();
        Document doc = Jsoup.connect(link).get();
        Post post = null;
        Elements row = doc.select(".postslisttopic");
        for (Element td : row) {
            Element href = td.child(0);
            try {
                post = detail(href.attr("href"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            result.add(post);
        }
        return result;
    }

    @Override
    public Post detail(String link) throws Exception {
        Post post = new Post();
        ParseDate parseDate = new ParseDate();
        Document doc = Jsoup.connect(link).get();
        Elements elementsText = doc.select(".msgBody");
        Elements elementsName = doc.select(".messageHeader");
        Elements elementsDate = doc.select(".msgFooter");
        String stringDate = getDateFromString(elementsDate.get(0).text());
        post.setLink(link);
        post.setName(elementsName.get(0).text());
        post.setText(elementsText.get(1).text());

        post.setDateCreated(parseDate.parseDate(stringDate));
        System.out.println(stringDate);
        System.out.println(parseDate.parseDate(stringDate));
        return post;
    }
}
