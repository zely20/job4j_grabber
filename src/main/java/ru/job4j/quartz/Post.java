package ru.job4j.quartz;

import java.util.Date;
import java.util.Objects;

public class Post {

    private Integer id;
    private String name;
    private String text;
    private String link;
    private Date dateCreated;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id) &&
                Objects.equals(name, post.name) &&
                Objects.equals(text, post.text) &&
                Objects.equals(link, post.link) &&
                Objects.equals(dateCreated, post.dateCreated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, text, link, dateCreated);
    }


}
