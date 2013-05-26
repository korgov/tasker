

drop table task;
drop table course;
drop table task_type;
create table task_type (
  id bigint primary key auto_increment,
  description varchar(4000),
  view_info varchar(4000) not null,
  solve_info varchar(4000) not null,
  create_info varchar(4000) not null
);

drop table course;

create table course (
  id bigint primary key auto_increment,
  title varchar(900),
  description varchar(4000)
);

drop table task_creator;
create table task_creator (
  course_id bigint not null,
  task_type_id bigint not null,
  descr varchar(4000),

  constraint foreign key (course_id) references course(id),
  constraint foreign key (task_type_id) references task_type(id)

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