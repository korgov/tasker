

drop table task;
drop table course;
drop table task_type;
create table task_type (
  id bigint primary key auto_increment,
  description varchar(4000),
  view_info varchar(4000) not null,
  solve_info varchar(4000) not null
);

drop table course;

create table course (
  id bigint primary key auto_increment,
  title varchar(900),
  description varchar(4000)
);

create table task_creator (
  id bigint primary key auto_increment,
  course_id bigint not null,
  description varchar(4000),
  info varchar(4000),

  constraint foreign key (course_id) references course(id)

);

drop table task;
create table task (
  id bigint primary key auto_increment,
  type_id bigint not null,
  course_id bigint not null,
  info varchar(4000),
  title varchar(900),

  constraint foreign key (type_id) references task_type(id),
  constraint foreign key (course_id) references course(id)
);