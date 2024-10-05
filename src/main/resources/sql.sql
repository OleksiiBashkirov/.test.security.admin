create table person
(
    id        int generated by default as identity primary key,
    name      varchar        not null,
    lastname  varchar        not null,
    email     varchar unique not null,
    username  varchar        not null,
    password  varchar        not null,
    role      varchar        not null,
    is_enable boolean        not null
);

select *
from person;

create table activation
(
    id    int generated by default as identity primary key,
    key   varchar not null,
    email varchar not null
);

select * from activation;

delete from activation;

delete from person;