drop table if exists likes;
drop table if exists film_genre;
drop table if exists friends;
drop table if exists users;
drop table if exists films;
drop table if exists genres;
drop table if exists mpa;

CREATE TABLE IF NOT exists mpa (
id int not null primary key,
name varchar(5) not null
);

CREATE TABLE IF NOT EXISTS genres (
id int not null primary key,
name varchar(20) not null
);

CREATE TABLE IF NOT EXISTS users (
id bigint not null GENERATED ALWAYS AS IDENTITY primary key,
name varchar(50) not null,
login varchar (50) not NULL,
email varchar (50) NOT NULL,
birthday date NOT NULL
);

CREATE TABLE IF NOT EXISTS films (
id bigint not null GENERATED ALWAYS AS IDENTITY primary key,
name varchar(100) not null,
description varchar (200) not NULL,
release_date date NOT NULL,
duration int NOT NULL,
mpa_id int NOT NULL REFERENCES mpa(id)
);

CREATE TABLE IF NOT EXISTS film_genre (
film_id bigint NOT NULL REFERENCES films(id),
genre_id int NOT NULL REFERENCES genres(id)
);

CREATE TABLE IF NOT EXISTS likes (
film_id bigint NOT NULL REFERENCES films(id),
user_id bigint NOT NULL REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS friends (
user_id bigint not null REFERENCES users(id),
user_friend_id bigint not null REFERENCES users(id)
);