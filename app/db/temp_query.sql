select * from s2020csb1072 ,course_catalog where s2020csb1072.course = course_catalog.course_code and s2020csb1072.ay = course_catalog.ay and s2020csb1072.sem = course_catalog.sem and s2020csb1072.grade != 'F'