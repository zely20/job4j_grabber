package ru.job4j.quartz;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {

    private static final String ADD_POST = "INSERT INTO items ( name) VALUES (?);";
    private static final String FIND_ALL_POST = "SELECT * FROM items";
    private Connection cnn;

    public PsqlStore(Properties cfg) {
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
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
            pr.setString(2, post.getName());
            pr.setString(3, post.getText());
            pr.setString(4, post.getLink());
            pr.setDate(5, (Date) post.getDateCreated());
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

    public Post findById(String id) {
        return null;
    }

    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }
}