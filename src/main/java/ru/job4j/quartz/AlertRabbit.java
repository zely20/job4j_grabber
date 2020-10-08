package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class AlertRabbit {

    public static Connection connection(Properties config) {
        try {
            Class.forName(config.getProperty("driver-class-name"));
            return DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")
            );
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        try (InputStream in = AlertRabbit.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            List<Long> store = new ArrayList<>();
            Properties config = new Properties();
            config.load(in);
            try (Connection connection = connection(config)) {
                Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
                scheduler.start();
                JobDataMap data = new JobDataMap();
                data.put("store", store);
                data.put("connection", connection);
                JobDetail job = newJob(Rabbit.class).build();
                SimpleScheduleBuilder times = simpleSchedule()
                        .withIntervalInSeconds(Integer.parseInt(config.getProperty("rabbit.interval")))
                        .repeatForever();
                Trigger trigger = newTrigger()
                        .startNow()
                        .withSchedule(times)
                        .build();
                scheduler.scheduleJob(job, trigger);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SchedulerException se) {
            se.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


        public static class Rabbit implements Job {

            public Rabbit() {
                System.out.println(hashCode());

            }
            @Override
            public void execute(JobExecutionContext context) throws JobExecutionException {
                System.out.println("Rabbit runs here ...");
                List<Long> store = (List<Long>) context.getJobDetail().getJobDataMap().get("store");
                Connection connection = (Connection) context.getJobDetail().getJobDataMap().get("connection");
                try (PreparedStatement ps = connection.prepareStatement("INSERT INTO rabbit (created) VALUES (?);")) {
                    ps.setLong(1, System.currentTimeMillis());
                    ps.execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
