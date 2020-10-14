package ru.job4j.quartz;

import java.io.InputStream;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {

/*    public static void main(String[] args) {

        try (InputStream in = PsqlStore.class.getClassLoader().getResourceAsStream("rabbit.properties")){
            Properties config = new Properties();
            config.load(in);
            PsqlStore psqlStore = new PsqlStore(config);
            SqlRuParser postFromSql = new SqlRuParser();
            for (Post post : postFromSql.list("https://www.sql.ru/forum/job-offers/")) {
                psqlStore.save(post);
            }

            psqlStore.getAll().forEach(System.out::println);
            System.out.println("find from id 1   " + psqlStore.findById("2"));

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }*/

    private static final String ADD_POST = "INSERT INTO posts (name, topic_text, link, date_created) VALUES (?, ?, ?, ?);";
    private static final String FIND_ALL_POST = "SELECT * FROM posts";
    private static final String FIND_BY_ID_POST = "SELECT * FROM posts WHERE id = ?";
    private Connection cnn;

    public PsqlStore(Properties cfg) {
        try {
            Class.forName(cfg.getProperty("driver-class-name"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            cnn = DriverManager.getConnection(
                    cfg.getProperty("url"),
                    cfg.getProperty("username"),
                    cfg.getProperty("password"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement pr = cnn.prepareStatement(ADD_POST)) {
            pr.setString(1, post.getName());
            pr.setString(2, post.getText());
            pr.setString(3, post.getLink());
            pr.setDate(4, new java.sql.Date(post.getDateCreated().getTime()));
            pr.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
       List<Post> result = new LinkedList<>();
        try (PreparedStatement pr = cnn.prepareStatement(FIND_ALL_POST)) {
            ResultSet resultSet = pr.executeQuery();
            while (resultSet.next()) {
                result.add(new Post(resultSet.getInt(1), resultSet.getString(2),
                        resultSet.getString(3), resultSet.getString(4), resultSet.getDate(5)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Post findById(String id) {
        Post result = null;
        try (PreparedStatement pr = cnn.prepareStatement(FIND_BY_ID_POST)) {
            pr.setInt(1, Integer.parseInt(id));
            ResultSet resultSet = pr.executeQuery();
            while (resultSet.next()) {
                result = new Post(resultSet.getInt(1), resultSet.getString(2),
                        resultSet.getString(3), resultSet.getString(4), resultSet.getDate(5));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }
}
