package ru.job4j.quartz;

import java.util.List;

public interface Store {
    void save(Post post);
    Post findById(String id);
    List<Post> getAll();
}
