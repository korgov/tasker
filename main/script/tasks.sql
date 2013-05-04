
create table task_type (
  id bigint primary key auto_increment,
  description varchar(4000),
  view_bean varchar(900) not null,
  solve_bean varchar(900) not null
);


create table course (
  id bigint primary key auto_increment,
  description varchar(4000)
);

create table task (
  id bigint primary key auto_increment,
  type_id bigint not null,
  course_id bigint not null,
  info varchar(4000),

  constraint foreign key (type_id) references task_type(id),
  constraint foreign key (course_id) references course(id)
);