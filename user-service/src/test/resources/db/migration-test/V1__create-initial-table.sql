create table user_service.profile
(
    id          bigint auto_increment
        primary key,
    description varchar(255) not null,
    name        varchar(255) not null
);

create table user_service.user
(
    id         bigint auto_increment
        primary key,
    email      varchar(255) not null,
    first_name varchar(255) not null,
    last_name  varchar(255) not null,
    password   varchar(255) not null,
    roles      varchar(255) not null,
    constraint UKob8kqyqqgmefl0aco34akdtpe
        unique (email)
);

create table user_service.user_profile
(
    id         bigint auto_increment
        primary key,
    profile_id bigint not null,
    user_id    bigint not null,
    constraint FK6kwj5lk78pnhwor4pgosvb51r
        foreign key (user_id) references user_service.user (id),
    constraint FKqfbftbxicceqbmvj87g9be2qn
        foreign key (profile_id) references user_service.profile (id)
);

