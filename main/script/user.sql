create table user (
  id bigint primary key auto_increment,
  name varchar(1000),
  login varchar(1000),
  pwdhash varchar(1000)
);