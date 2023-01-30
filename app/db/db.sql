-- drop database academic_mangement;
-- create database academic_management;
-- TODO: check if pre_req is also a course or not. [course catalog].
-- TODO: refractor code to maybe separate different modules or even triggers and functions.


--!-----------------------------------------------* course_catalog starts *----------------------------------------------------------------
drop table course_catalog;
create table course_catalog(
    course_code varchar(255) not null,
    L integer not null,
    T integer not null,
    P integer not null,
    C integer not null,
    pre_req text [],
    primary key (course_code)
    
);

CREATE OR REPLACE FUNCTION add_credit()
  RETURNS TRIGGER 
  LANGUAGE PLPGSQL
  AS
$$
declare
credits integer;
BEGIN
	credits := (new.L + (new.P)/2);
    new.C = credits;
	RETURN new;
END;
$$;

create trigger calculate_credits 
before insert 
on course_catalog
for each row 
execute procedure add_credit();
--------------------------------------------------* course_catalog ends *----------------------------------------------------------------

--!-----------------------------------------------* Student authentication starts *----------------------------------------------------------------
drop table student_auth;
create table student_auth (
    name varchar(255) not null,
    id varchar(255) not null,
    pwd varchar(255) not null,
    primary key (id)
);


--------------------------------------------------* Student Authentication ends *----------------------------------------------------------------

--!-----------------------------------------------* Prof authentication starts *----------------------------------------------------------------
drop table prof_auth;
create table prof_auth (
    id varchar(255) not null,
    name varchar(255) not null,
    pwd varchar(255) not null,
    primary key (id)
);
--------------------------------------------------* Prof Authentication ends *----------------------------------------------------------------

--!-----------------------------------------------* course_offerings starts *----------------------------------------------------------------
drop table course_offerings;
create table course_offerings(
    id serial,
    course_code varchar(255) not null,
    instructor_id varchar(255) not null,
    cg_constraint numeric(10,3) default 0,
    primary key (id),
    constraint check_course foreign key (course_code) references course_catalog(course_code),
    constraint check_inst foreign key (instructor_id) references prof_auth(id)
);
--------------------------------------------------* course_offerings ends *----------------------------------------------------------------





insert into course_catalog(course_code, L, T,P) values('CS301',3,1,2);
insert into course_catalog(course_code, L, T,P,pre_req) values('CS302',3,1,0, Array ['CS301','CS305']); 
insert into course_catalog(course_code, L, T,P) values('CS303',3,1,2);
insert into course_catalog(course_code, L, T,P,pre_req) values('CS304',3,1,2, array ['CS303']);
insert into course_catalog(course_code, L, T,P) values('CS305',3,0,2);
insert into student_auth(id,name,pwd) values('2020csb1070@iitrpr.ac.in','Amit Kumar','X123');
insert into student_auth(id,name,pwd) values('2020csb1072@iitrpr.ac.in','Ankit Sharma','X123');
insert into student_auth(id,name,pwd) values('2020csb1074@iitrpr.ac.in','Arshdeep Singh','X123');
insert into prof_auth(id,name,pwd) values('apurva@iitrpr.ac.in','Apurva Mudgal','X123');
insert into prof_auth(id,name,pwd) values('gunturi@iitrpr.ac.in','V. Gunturi','X123');
insert into prof_auth(id,name,pwd) values('balwinder@iitrpr.ac.in','Balwinder Sodhi','X123');

insert into course_offerings(course_code, instructor_id) values('CS302','apurva@iitrpr.ac.in');
insert into course_offerings(course_code, instructor_id) values('CS301','gunturi@iitrpr.ac.in');
insert into course_offerings(course_code, instructor_id,cg_constraint) values('CS305','balwinder@iitrpr.ac.in',7.5);