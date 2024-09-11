drop table if exists likes cascade;
drop table if exists film_genre cascade;
drop table if exists friends cascade;
drop table if exists users cascade;
drop table if exists films cascade;
drop table if exists genres cascade;
drop table if exists mpa cascade;
drop table if exists directors cascade;
drop table if exists film_director cascade;
drop table if exists reviews cascade;
drop table if exists useful cascade;
drop table if exists events cascade;

CREATE TABLE IF NOT EXISTS mpa
(
    id   int        NOT NULL PRIMARY KEY,
    name varchar(5) NOT NULL
);

CREATE TABLE IF NOT EXISTS genres
(
    id   int         NOT NULL PRIMARY KEY,
    name varchar(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS users
(
    id       bigint      NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name     varchar(50) NOT NULL,
    login    varchar(50) NOT NULL UNIQUE,
    email    varchar(50) NOT NULL UNIQUE,
    birthday date        NOT NULL
);

CREATE TABLE IF NOT EXISTS films
(
    id           bigint       NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name         varchar(100) NOT NULL,
    description  varchar(200) NOT NULL,
    release_date date         NOT NULL,
    duration     int          NOT NULL,
    mpa_id       int          NOT NULL REFERENCES mpa (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS directors
(
    id   bigint      NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS film_genre
(
    film_id  bigint NOT NULL REFERENCES films (id) ON DELETE CASCADE,
    genre_id int    NOT NULL REFERENCES genres (id) ON DELETE CASCADE,
    PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS likes
(
    film_id bigint NOT NULL REFERENCES films (id) ON DELETE CASCADE,
    user_id bigint NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    PRIMARY KEY (film_id, user_id)
);

CREATE TABLE IF NOT EXISTS friends
(
    user_id        bigint NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    user_friend_id bigint NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, user_friend_id)
);

CREATE TABLE IF NOT EXISTS film_director
(
    film_id     bigint NOT NULL REFERENCES films (id) ON DELETE CASCADE,
    director_id bigint NOT NULL REFERENCES directors (id) ON DELETE CASCADE,
    PRIMARY KEY (film_id, director_id)
);

CREATE TABLE IF NOT EXISTS reviews
(
    id          bigint  NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    content     varchar NOT NULL,
    is_positive bool    NOT NULL,
    user_id     bigint  NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    film_id     bigint  NOT NULL REFERENCES films (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS useful
(
    review_id bigint NOT NULL REFERENCES reviews (id) ON DELETE CASCADE,
    user_id   bigint NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    l_d       int    NOT NULL,
    PRIMARY KEY (review_id, user_id)
);

CREATE TABLE IF NOT EXISTS events
(
    timestamp  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    user_id    bigint    NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    event_type int       NOT NULL,
    operation  int       NOT NULL,
    id         bigint    NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    entity_id  bigint    NOT NULL
);
