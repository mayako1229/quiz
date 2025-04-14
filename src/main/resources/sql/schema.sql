CREATE SEQUENCE IF NOT EXISTS quiz_id_seq START 1;

DROP TABLE IF EXISTS public.quizzes;

CREATE TABLE public.quizzes (
    id bigint NOT NULL DEFAULT nextval('quiz_id_seq'::regclass),
    question character varying(255) COLLATE pg_catalog."default",
    choice1 character varying(255) COLLATE pg_catalog."default",
    choice2 character varying(255) COLLATE pg_catalog."default",
    choice3 character varying(255) COLLATE pg_catalog."default",
    choice4 character varying(255) COLLATE pg_catalog."default",
    answer integer,
    description character varying(255) COLLATE pg_catalog."default",
    created_at timestamp without time zone DEFAULT now(),
    updated_at timestamp without time zone,
    CONSTRAINT quiz_pkey PRIMARY KEY (id)
);
--シーケンスの調整
SELECT setval('quiz_id_seq', COALESCE((SELECT MAX(id) FROM quizzes), 1), false);
