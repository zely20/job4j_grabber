package ru.job4j.quartz;

import java.util.List;

public interface Store {
    void save(Post post);

    List<Post> getAll();
}
