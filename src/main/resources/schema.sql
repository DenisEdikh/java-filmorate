drop table if exists likes;
drop table if exists film_genre;
drop table if exists friends;
drop table if exists users;
drop table if exists films;
drop table if exists genres;
drop table if exists mpa;

CREATE TABLE IF NOT EXISTS mpa (
id int NOT NULL PRIMARY KEY,
name varchar(5) NOT NULL
);

CREATE TABLE IF NOT EXISTS genres (
id int NOT NULL PRIMARY KEY,
name varchar(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
id bigint NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
name varchar(50) NOT NULL,
login varchar (50) NOT NULL UNIQUE,
email varchar (50) NOT NULL UNIQUE,
birthday date NOT NULL
);

CREATE TABLE IF NOT EXISTS films (
id bigint NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
name varchar(100) NOT NULL,
description varchar (200) NOT NULL,
release_date date NOT NULL,
duration int NOT NULL,
mpa_id int NOT NULL REFERENCES mpa(id)
);

CREATE TABLE IF NOT EXISTS film_genre (
film_id bigint NOT NULL REFERENCES films(id),
genre_id int NOT NULL REFERENCES genres(id),
PRIMARY KEY(film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS likes (
film_id bigint NOT NULL REFERENCES films(id),
user_id bigint NOT NULL REFERENCES users(id),
PRIMARY KEY(film_id, user_id)
);

CREATE TABLE IF NOT EXISTS friends (
user_id bigint not null REFERENCES users(id),
user_friend_id bigint not null REFERENCES users(id),
PRIMARY KEY(user_id, user_friend_id)
);