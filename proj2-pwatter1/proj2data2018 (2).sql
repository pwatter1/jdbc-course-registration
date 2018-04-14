drop table logs;
drop table prerequisites;
drop table enrollments;
drop table classes;
drop table courses;
drop table students;

create table students (sid char(4) primary key check (sid like 'B%'),
firstname varchar2(15) not null, lastname varchar2(15) not null, status varchar2(10) 
check (status in ('freshman', 'sophomore', 'junior', 'senior', 'graduate')), 
gpa number(3,2) check (gpa between 0 and 4.0), email varchar2(20) unique);

create table courses (dept_code varchar2(4) not null, course_no number(3) not null
check (course_no between 100 and 799), title varchar2(20) not null,
primary key (dept_code, course_no));

create table prerequisites (dept_code varchar2(4) not null,
course_no number(3) not null, pre_dept_code varchar2(4) not null,
pre_course_no number(3) not null,
primary key (dept_code, course_no, pre_dept_code, pre_course_no),
foreign key (dept_code, course_no) references courses on delete cascade,
foreign key (pre_dept_code, pre_course_no) references courses
on delete cascade);

create table classes (classid char(5) primary key check (classid like 'c%'), 
dept_code varchar2(4) not null, course_no number(3) not null, 
sect_no number(2), year number(4), semester varchar2(6) 
check (semester in ('Spring', 'Fall', 'Summer')), limit number(3), 
class_size number(3), foreign key (dept_code, course_no) references courses
on delete cascade, unique(dept_code, course_no, sect_no, year, semester),
check (class_size <= limit));

create table enrollments (sid char(4) references students, classid char(5) references classes, 
lgrade char check (lgrade in ('A', 'B', 'C', 'D', 'F', 'I', null)), primary key (sid, classid));

create table logs (logid number(4) primary key, who varchar2(10) not null, time date not null, 
table_name varchar2(20) not null, operation varchar2(6) not null, key_value varchar2(14));

insert into students values ('B001', 'Anne', 'Broder', 'junior', 3.17, 'broder@bu.edu');
insert into students values ('B002', 'Terry', 'Buttler', 'senior', 3.0, 'buttler@bu.edu');
insert into students values ('B003', 'Tracy', 'Wang', 'senior', 4.0, 'wang@bu.edu');
insert into students values ('B004', 'Barbara', 'Callan', 'junior', 2.5, 'callan@bu.edu');
insert into students values ('B005', 'Jack', 'Smith', 'graduate', 3.0, 'smith@bu.edu');
insert into students values ('B006', 'Terry', 'Zillman', 'graduate', 4.0, 'zillman@bu.edu');
insert into students values ('B007', 'Becky', 'Lee', 'senior', 4.0, 'lee@bu.edu');
insert into students values ('B008', 'Tom', 'Baker', 'freshman', null, 'baker@bu.edu');

insert into courses values ('CS', 432, 'database systems');
insert into courses values ('Math', 314, 'discrete math');
insert into courses values ('CS', 240, 'data structure');
insert into courses values ('Math', 221, 'calculus I');
insert into courses values ('CS', 532, 'database systems');
insert into courses values ('CS', 552, 'operating systems');
insert into courses values ('BIOL', 425, 'molecular biology');

insert into prerequisites values ('CS', 432, 'CS', 240);
insert into prerequisites values ('CS', 532, 'CS', 432);
insert into prerequisites values ('Math', 314, 'Math', 221);
insert into prerequisites values ('CS', 432, 'Math', 314);
insert into prerequisites values ('CS', 552, 'CS', 240);

insert into classes values  ('c0001', 'CS', 432, 1, 2018, 'Spring', 3, 1);
insert into classes values  ('c0002', 'Math', 314, 1, 2017, 'Fall', 2, 2);
insert into classes values  ('c0003', 'Math', 314, 2, 2017, 'Fall', 3, 2);
insert into classes values  ('c0004', 'CS', 432, 1, 2017, 'Spring', 2, 1);
insert into classes values  ('c0005', 'CS', 240, 1, 2018, 'Spring', 5, 5);
insert into classes values  ('c0006', 'CS', 532, 1, 2018, 'Spring', 2, 1);
insert into classes values  ('c0007', 'Math', 221, 1, 2018, 'Spring', 6, 5);

insert into enrollments values  ('B001', 'c0001', 'A');
insert into enrollments values  ('B002', 'c0002', 'B');
insert into enrollments values  ('B006', 'c0007', 'A');
insert into enrollments values  ('B004', 'c0005', 'C');
insert into enrollments values  ('B005', 'c0005', 'B');
insert into enrollments values  ('B005', 'c0007', 'B');
insert into enrollments values  ('B006', 'c0003', 'A');
insert into enrollments values  ('B001', 'c0002', 'C');
insert into enrollments values  ('B003', 'c0005', null);
insert into enrollments values  ('B002', 'c0007', 'A');
insert into enrollments values  ('B001', 'c0007', 'B');
insert into enrollments values  ('B001', 'c0006', 'B');
insert into enrollments values  ('B001', 'c0005', 'A');
insert into enrollments values  ('B005', 'c0003', 'B');
insert into enrollments values  ('B005', 'c0004', 'D');
insert into enrollments values  ('B007', 'c0005', 'B');
insert into enrollments values  ('B008', 'c0007', 'A');

