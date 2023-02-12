-- drop database academic_mangement;
-- create database academic_management;
-- TODO: check if pre_req is also a course or not. [course catalog].
-- TODO: refractor code to maybe separate different modules or even triggers and functions.
-- TODO: Is it better to go with individual table for each student or same table for all students, for different semester_AY table.
-- TODO: add semester and AY
\c academic_management
--!-----------------------------------------------* course_catalog starts *----------------------------------------------------------------
drop table course_catalog;
create table course_catalog(
    course_code varchar(255) not null,
    L integer not null,
    T integer not null,
    P numeric(10,2) not null,
    C numeric(10,2) not null,
    ay text not null,
    sem integer not null,
    pre_req text [] default null,
    primary key (course_code,ay,sem)
    
);

CREATE OR REPLACE FUNCTION add_credit()
  RETURNS TRIGGER 
  LANGUAGE PLPGSQL
  AS
$$
declare
credits numeric(10,2);
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

--!-----------------------------------------------* User authentication starts *----------------------------------------------------------------
drop table user_auth;
create table user_auth (
    name varchar(255) not null,
    id varchar(255) not null,
    pwd varchar(255) not null,
    roles varchar(255) not null,
    primary key (id)
);
CREATE OR REPLACE FUNCTION add_student_record()
  RETURNS TRIGGER 
  LANGUAGE PLPGSQL
  AS
$$
declare
  table_name text;
  
BEGIN
  If new.roles = 's' then 
    table_name := 's' || substring(new.id,1,11);
    EXECUTE 'create table if not exists '
      || quote_ident(table_name)
      || ' (
        sem integer,
        AY text,
        course text not null,
        grade text
      )';
    
  end if;
  RETURN new;
END;
$$;

create trigger create_student_record 
after insert 
on user_auth
for each row 
execute procedure add_student_record();
--------------------------------------------------* User Authentication ends *----------------------------------------------------------------

--!-----------------------------------------------* course_offerings starts *----------------------------------------------------------------
drop table course_offerings;
create table course_offerings(
    course_code varchar(255) not null,
    instructor_id varchar(255) not null,
    cg_constraint numeric(10,3) default 0,
    primary key (instructor_id,course_code),
    constraint check_inst foreign key (instructor_id) references user_auth(id)
);
--------------------------------------------------* course_offerings ends *----------------------------------------------------------------

CREATE OR REPLACE FUNCTION login_check(username text, password text)
RETURNS text AS $$
DECLARE
    result text;
BEGIN
    SELECT INTO result
        CASE
            WHEN (SELECT count(*) FROM user_auth WHERE id = login_check.username AND pwd = login_check.password and roles = 's') > 0 THEN 's'
            WHEN (SELECT count(*) FROM user_auth WHERE id = login_check.username AND pwd = login_check.password and roles = 'p') > 0 THEN 'p'
            WHEN (SELECT count(*) FROM user_auth WHERE id = login_check.username AND pwd = login_check.password and roles = 'a') > 0 THEN 'a'
            ELSE 'f'
        END;
    RETURN result;
END;
$$ LANGUAGE plpgsql;



--!--------------------------------------------------------------* login_log starts *----------------------------------------------------------------
create table login_log(
  id serial,
  email varchar(255) not null,
  status varchar(255) not null,
  time_stamp timestamp default now(),
  primary key (id),
  constraint check_user foreign key (email) references user_auth(id)
);
----------------------------------------------------------------* login_log ends *----------------------------------------------------------------


--!--------------------------------------------------------------* current_session starts *----------------------------------------------------------------
create table current_session(
  ay varchar(255) not null,
  sem integer not null,
  primary key (ay,sem)
);
----------------------------------------------------------------* current_session starts *----------------------------------------------------------------


--!--------------------------------------------------------------* config starts *----------------------------------------------------------------
create table config(
  id serial primary key,
  sem_start boolean default true,
  course_float_start boolean default false,
  course_float_end boolean default false,
  student_enroll_start boolean default false,
  student_enroll_end boolean default false,
  grade_submission_start boolean default false,
  grade_submission_end boolean default false,
  validation_end boolean default false,
  semester_end boolean default false
);
create table config_number(
  id integer not  null default 1
);
----------------------------------------------------------------* config ends *----------------------------------------------------------------

--!--------------------------------------------------------------* report_validator starts *----------------------------------------------------------------
create table report_validator(
  course_code varchar(255) not null,
  student_id varchar(255) not null,
  primary key(course_code, student_id)
);
----------------------------------------------------------------* report_validator starts *----------------------------------------------------------------




insert into course_catalog(course_code, L, T,P,ay,sem) values('CS301',3,1,2,'2023-24',1);
insert into course_catalog(course_code, L, T,P,ay,sem) values('CS304',3,1,2,'2022-23',1);
insert into course_catalog(course_code, L, T,P,ay,sem) values('CS301',3,1,2,'2020-21',1);
insert into course_catalog(course_code, L, T,P,ay,sem) values('CS201',3,1,2,'2023-24',1);
insert into course_catalog(course_code, L, T,P,ay,sem) values('CS202',3,1,2,'2023-24',1);
insert into course_catalog(course_code, L, T,P,ay,sem) values('CS204',3,1,2,'2023-24',1);
insert into course_catalog(course_code, L, T,P,ay,sem) values('CS205',3,1,2,'2023-24',1);
insert into course_catalog(course_code, L, T,P,pre_req,ay,sem) values('CS302',3,1,0, Array ['CS301','CS305'],'2023-24',1); 
insert into course_catalog(course_code, L, T,P,pre_req,ay,sem) values('CS302',3,1,0, Array ['CS301','CS305'],'2021-22',2); 
insert into course_catalog(course_code, L, T,P,pre_req,ay,sem) values('CS303',3,1,2,Array ['CS302'],'2023-24',1);
insert into course_catalog(course_code, L, T,P,pre_req,ay,sem) values('CS303',3,1,2,Array ['CS302'],'2022-23',1);
insert into course_catalog(course_code, L, T,P,pre_req,ay,sem) values('CS303',3,1,2,Array ['CS302'],'2022-23',2);
insert into course_catalog(course_code, L, T,P,pre_req,ay,sem) values('CS304',3,1,2, array ['CS303'],'2023-24',1);
insert into course_catalog(course_code, L, T,P,ay,sem) values('CS305',3,0,2,'2023-24',1);
insert into course_catalog(course_code, L, T,P,ay,sem) values('CS305',3,0,2,'2021-22',1);
insert into course_catalog(course_code, L, T,P,ay,sem) values('CS306',3,0,2,'2023-24',1);
insert into course_catalog(course_code, L, T,P,ay,sem) values('CS306',3,0,2,'2021-22',1);
insert into course_catalog(course_code, L, T,P,ay,sem) values('CS539',3,0,2,'2023-24',1);
insert into course_catalog(course_code, L, T,P,ay,sem) values('CS539',3,0,2,'2020-21',2);

insert into user_auth(id,name,pwd,roles) values('2020csb1070@iitrpr.ac.in','Amit Kumar','X123','s');
insert into user_auth(id,name,pwd,roles) values('2020csb1072@iitrpr.ac.in','Ankit Sharma','X123','s');
insert into user_auth(id,name,pwd,roles) values('2020csb1074@iitrpr.ac.in','Arshdeep Singh','X123','s');
insert into user_auth(id,name,pwd,roles) values('apurva@iitrpr.ac.in','Apurva Mudgal','X123','p');
insert into user_auth(id,name,pwd,roles) values('gunturi@iitrpr.ac.in','V. Gunturi','X123','p');
insert into user_auth(id,name,pwd,roles) values('balwinder@iitrpr.ac.in','Balwinder Sodhi','X123','p');
insert into user_auth(id,name,pwd,roles) values('admin@iitrpr.ac.in','Admin','X123','a');

insert into course_offerings(course_code, instructor_id) values('CS302','apurva@iitrpr.ac.in');
insert into course_offerings(course_code, instructor_id) values('CS301','gunturi@iitrpr.ac.in');
insert into course_offerings(course_code, instructor_id) values('CS201','gunturi@iitrpr.ac.in');
insert into course_offerings(course_code, instructor_id) values('CS202','gunturi@iitrpr.ac.in');
insert into course_offerings(course_code, instructor_id) values('CS204','gunturi@iitrpr.ac.in');
insert into course_offerings(course_code, instructor_id) values('CS205','gunturi@iitrpr.ac.in');
insert into course_offerings(course_code, instructor_id,cg_constraint) values('CS305','balwinder@iitrpr.ac.in',7.5);
insert into course_offerings(course_code, instructor_id,cg_constraint) values('CS539','balwinder@iitrpr.ac.in',0.0);
insert into course_offerings(course_code, instructor_id) values('CS304','balwinder@iitrpr.ac.in');

insert into current_session(ay,sem) values('2023-24',1);

insert into s2020csb1072(sem,ay,course,grade) values (1,'2020-21','CS301','A');
insert into s2020csb1072(sem,ay,course,grade) values (2,'2020-21','CS539','A-');
insert into s2020csb1072(sem,ay,course,grade) values (1,'2021-22','CS305','B');
insert into s2020csb1072(sem,ay,course,grade) values (1,'2021-22','CS306','A');
insert into s2020csb1072(sem,ay,course,grade) values (2,'2021-22','CS302','A');
insert into s2020csb1072(sem,ay,course,grade) values (1,'2022-23','CS303','F');
insert into s2020csb1072(sem,ay,course,grade) values (2,'2022-23','CS303','A');


insert into config(sem_start,course_float_start,course_float_end,student_enroll_start,student_enroll_end,grade_submission_start,grade_submission_end,validation_end,semester_end) values(true,false,false,false,false,false,false,false,false);
insert into config(sem_start,course_float_start,course_float_end,student_enroll_start,student_enroll_end,grade_submission_start,grade_submission_end,validation_end,semester_end) values(true,true,false,false,false,false,false,false,false);
insert into config(sem_start,course_float_start,course_float_end,student_enroll_start,student_enroll_end,grade_submission_start,grade_submission_end,validation_end,semester_end) values(true,false,true,false,false,false,false,false,false);
insert into config(sem_start,course_float_start,course_float_end,student_enroll_start,student_enroll_end,grade_submission_start,grade_submission_end,validation_end,semester_end) values(true,false,true,true,false,false,false,false,false);
insert into config(sem_start,course_float_start,course_float_end,student_enroll_start,student_enroll_end,grade_submission_start,grade_submission_end,validation_end,semester_end) values(true,false,true,false,true,false,false,false,false);
insert into config(sem_start,course_float_start,course_float_end,student_enroll_start,student_enroll_end,grade_submission_start,grade_submission_end,validation_end,semester_end) values(true,false,true,false,true,true,false,false,false);
insert into config(sem_start,course_float_start,course_float_end,student_enroll_start,student_enroll_end,grade_submission_start,grade_submission_end,validation_end,semester_end) values(true,false,true,false,true,false,true,false,false);
insert into config(sem_start,course_float_start,course_float_end,student_enroll_start,student_enroll_end,grade_submission_start,grade_submission_end,validation_end,semester_end) values(true,false,true,false,true,false,true,true,false);
insert into config(sem_start,course_float_start,course_float_end,student_enroll_start,student_enroll_end,grade_submission_start,grade_submission_end,validation_end,semester_end) values(true,false,true,false,true,false,true,false,true);

insert into config_number(id) values(4);