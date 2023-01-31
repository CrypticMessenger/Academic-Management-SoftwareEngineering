-- drop database academic_mangement;
-- create database academic_management;
-- TODO: check if pre_req is also a course or not. [course catalog].
-- TODO: refractor code to maybe separate different modules or even triggers and functions.
-- TODO: Is it better to go with individual table for each student or same table for all students, for different semester_AY table.
-- TODO: add semester and AY
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
sem integer;
BEGIN
    If new.roles = 's' then 
    EXECUTE 'create table if not exists '
      || quote_ident('s' || substring(new.id,1,11))
      || ' (
        sem integer,
        AY text,
        courses text [],
        grades text [],
        cgpa numeric(10,2),
        credits_earned integer default 0
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
    id serial,
    course_code varchar(255) not null,
    instructor_id varchar(255) not null,
    cg_constraint numeric(10,3) default 0,
    primary key (id),
    constraint check_course foreign key (course_code) references course_catalog(course_code),
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














insert into course_catalog(course_code, L, T,P) values('CS301',3,1,2);
insert into course_catalog(course_code, L, T,P,pre_req) values('CS302',3,1,0, Array ['CS301','CS305']); 
insert into course_catalog(course_code, L, T,P) values('CS303',3,1,2);
insert into course_catalog(course_code, L, T,P,pre_req) values('CS304',3,1,2, array ['CS303']);
insert into course_catalog(course_code, L, T,P) values('CS305',3,0,2);
insert into user_auth(id,name,pwd,roles) values('2020csb1070@iitrpr.ac.in','Amit Kumar','X123','s');
insert into user_auth(id,name,pwd,roles) values('2020csb1072@iitrpr.ac.in','Ankit Sharma','X123','s');
insert into user_auth(id,name,pwd,roles) values('2020csb1074@iitrpr.ac.in','Arshdeep Singh','X123','s');
insert into s2020csb1070(sem,ay) values(2,'2020-2021');
insert into s2020csb1070(sem,ay) values(1,'2021-2022');
insert into s2020csb1070(sem,ay) values(2,'2021-2022');
insert into s2020csb1070(sem,ay) values(1,'2022-2023');
insert into s2020csb1070(sem,ay) values(2,'2022-2023');
insert into s2020csb1070(sem,ay) values(1,'2023-2024');

insert into s2020csb1072(sem,ay) values(2,'2020-2021');
insert into s2020csb1072(sem,ay) values(1,'2021-2022');
insert into s2020csb1072(sem,ay) values(2,'2021-2022');
insert into s2020csb1072(sem,ay) values(1,'2022-2023');
insert into s2020csb1072(sem,ay) values(2,'2022-2023');
insert into s2020csb1072(sem,ay) values(1,'2023-2024');

insert into user_auth(id,name,pwd,roles) values('apurva@iitrpr.ac.in','Apurva Mudgal','X123','p');
insert into user_auth(id,name,pwd,roles) values('gunturi@iitrpr.ac.in','V. Gunturi','X123','p');
insert into user_auth(id,name,pwd,roles) values('balwinder@iitrpr.ac.in','Balwinder Sodhi','X123','p');
insert into user_auth(id,name,pwd,roles) values('admin@iitrpr.ac.in','Admin','X123','a');
insert into course_offerings(course_code, instructor_id) values('CS302','apurva@iitrpr.ac.in');
insert into course_offerings(course_code, instructor_id) values('CS301','gunturi@iitrpr.ac.in');
insert into course_offerings(course_code, instructor_id,cg_constraint) values('CS305','balwinder@iitrpr.ac.in',7.5);
