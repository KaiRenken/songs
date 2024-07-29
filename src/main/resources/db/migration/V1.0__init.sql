create table song
(
    id               uuid PRIMARY KEY,
    name             varchar(500) not null,
    artist           varchar(500) not null,
    lyrics           varchar(20000) not null
);