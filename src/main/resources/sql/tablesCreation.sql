DROP TABLE IF EXISTS public.courses CASCADE;
CREATE TABLE public.courses
(
  id SERIAL PRIMARY KEY ,
  name VARCHAR (64),
  description character(100)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.courses
  OWNER TO javauser;
GRANT ALL ON TABLE public.courses TO javauser;

-- -------------------------------------------------

DROP TABLE IF EXISTS public.students CASCADE;
CREATE TABLE public.students
(
  id SERIAL PRIMARY KEY ,
  group_id integer,
  first_name VARCHAR (64),
  last_name VARCHAR (64),
  CONSTRAINT "fk_group" FOREIGN KEY ("group_id") REFERENCES "public"."groups" ("id") ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT "order_group_unique" UNIQUE ("id", "group_id")
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.students
  OWNER TO javauser;
GRANT ALL ON TABLE public.students TO javauser;

DROP INDEX IF EXISTS public.group_index;
CREATE INDEX group_index ON public.students(group_id);

-- ------------------------------------------------

DROP TABLE IF EXISTS public.groups CASCADE;
CREATE TABLE public.groups
(
  id SERIAL PRIMARY KEY ,
  name VARCHAR (64)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.groups
  OWNER TO javauser;
GRANT ALL ON TABLE public.groups TO javauser;

-- ------------------------------------------------
DROP TABLE IF EXISTS public.students_courses CASCADE;
CREATE TABLE public.students_courses
(
  course_id integer NOT NULL,
  student_id integer NOT NULL,
  CONSTRAINT pk_students_courses PRIMARY KEY (course_id, student_id),
  CONSTRAINT fk_course FOREIGN KEY (course_id)
      REFERENCES public.courses (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_student FOREIGN KEY (student_id)
      REFERENCES public.students (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.students_courses
  OWNER TO javauser;
GRANT ALL ON TABLE public.students_courses TO javauser;