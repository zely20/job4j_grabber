create table posts (
                       id serial,
                       name text,
                       topic_text text,
                       link text UNIQUE,
                       date_created date
);