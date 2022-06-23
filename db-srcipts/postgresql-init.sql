-- -----------------------------------------------------
-- Create Schema provide_and_order_services
-- -----------------------------------------------------
DROP DATABASE IF EXISTS provide_and_order_services;
CREATE DATABASE provide_and_order_services
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'English_United States.1251'
    LC_CTYPE = 'English_United States.1251'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

-- -----------------------------------------------------
-- Create Table role
-- -----------------------------------------------------
DROP TABLE IF EXISTS public.role;
CREATE TABLE IF NOT EXISTS public.role
(
    id integer NOT NULL,
    name character varying(45) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT role_pkey PRIMARY KEY (id)
)
    TABLESPACE pg_default;
ALTER TABLE IF EXISTS public.role
    OWNER to postgres;

-- -----------------------------------------------------
-- Create Table user
-- -----------------------------------------------------
DROP TABLE IF EXISTS public."user";
CREATE TABLE IF NOT EXISTS public."user"
(
    id bigint NOT NULL,
    first_name character varying(16) COLLATE pg_catalog."default" NOT NULL,
    last_name character varying(16) COLLATE pg_catalog."default" NOT NULL,
    email character varying(255) COLLATE pg_catalog."default" NOT NULL,
    phone_number character varying(15) COLLATE pg_catalog."default",
    password character varying(255) COLLATE pg_catalog."default" NOT NULL,
    create_time time without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    role_id integer NOT NULL DEFAULT 0,
    CONSTRAINT user_pkey PRIMARY KEY (id),
    CONSTRAINT fk_user_role FOREIGN KEY (role_id)
        REFERENCES public.role (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
    TABLESPACE pg_default;
ALTER TABLE IF EXISTS public."user"
    OWNER to postgres;

-- -----------------------------------------------------
-- Create Table account_transaction
-- -----------------------------------------------------
DROP TABLE IF EXISTS public.account_transaction;
CREATE TABLE IF NOT EXISTS public.account_transaction
(
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    amount double precision NOT NULL,
    create_time time without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT account_transaction_pkey PRIMARY KEY (id),
    CONSTRAINT fk_account_transaction_user FOREIGN KEY (user_id)
        REFERENCES public."user" (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
    TABLESPACE pg_default;
ALTER TABLE IF EXISTS public.account_transaction
    OWNER to postgres;

-- -----------------------------------------------------
-- Create Table service_order_status
-- -----------------------------------------------------
DROP TABLE IF EXISTS public.service_order_status;
CREATE TABLE IF NOT EXISTS public.service_order_status
(
    id integer NOT NULL,
    name character varying(45) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT service_order_status_pkey PRIMARY KEY (id)
)
    TABLESPACE pg_default;
ALTER TABLE IF EXISTS public.service_order_status
    OWNER to postgres;

-- -----------------------------------------------------
-- Create Table work_category
-- -----------------------------------------------------
DROP TABLE IF EXISTS public.work_category;
CREATE TABLE IF NOT EXISTS public.work_category
(
    id bigint NOT NULL,
    parent_id bigint,
    name character varying(64) COLLATE pg_catalog."default" NOT NULL,
    icon character varying(64) COLLATE pg_catalog."default",
    CONSTRAINT work_category_pkey PRIMARY KEY (id)
)
    TABLESPACE pg_default;
ALTER TABLE IF EXISTS public.work_category
    OWNER to postgres;

-- -----------------------------------------------------
-- Create Table specialist_work_categories
-- -----------------------------------------------------
DROP TABLE IF EXISTS public.specialist_work_categories;
CREATE TABLE IF NOT EXISTS public.specialist_work_categories
(
    specialist_id bigint NOT NULL,
    work_category_id bigint NOT NULL,
    CONSTRAINT specialist_work_categories_pkey PRIMARY KEY (specialist_id, work_category_id),
    CONSTRAINT fk_specialist_work_categories_specialist FOREIGN KEY (specialist_id)
        REFERENCES public."user" (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_specialist_work_categories_work_category FOREIGN KEY (work_category_id)
        REFERENCES public.work_category (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
    TABLESPACE pg_default;
ALTER TABLE IF EXISTS public.specialist_work_categories
    OWNER to postgres;

-- -----------------------------------------------------
-- Create Table service_order
-- -----------------------------------------------------
DROP TABLE IF EXISTS public.service_order;
CREATE TABLE IF NOT EXISTS public.service_order
(
    id bigint NOT NULL,
    title character varying(128) COLLATE pg_catalog."default",
    description character varying(512) COLLATE pg_catalog."default",
    create_time timestamp without time zone NOT NULL,
    cost double precision,
    customer_id bigint NOT NULL,
    specialist_id bigint,
    work_category_id bigint NOT NULL,
    status_id integer NOT NULL,
    CONSTRAINT service_order_pkey PRIMARY KEY (id),
    CONSTRAINT fk_service_order_customer FOREIGN KEY (customer_id)
        REFERENCES public."user" (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_service_order_specialist FOREIGN KEY (specialist_id)
        REFERENCES public."user" (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_service_order_status FOREIGN KEY (status_id)
        REFERENCES public.service_order_status (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_service_order_work_category FOREIGN KEY (work_category_id)
        REFERENCES public.work_category (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
    TABLESPACE pg_default;
ALTER TABLE IF EXISTS public.service_order
    OWNER to postgres;

-- -----------------------------------------------------
-- Create Table feedback
-- -----------------------------------------------------
DROP TABLE IF EXISTS public.feedback;
CREATE TABLE IF NOT EXISTS public.feedback
(
    id bigint NOT NULL,
    text character varying(512) COLLATE pg_catalog."default" NOT NULL,
    rating integer NOT NULL,
    hidden boolean NOT NULL DEFAULT false,
    user_id bigint NOT NULL,
    service_order_id bigint NOT NULL,
    create_time timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT feedback_pkey PRIMARY KEY (id),
    CONSTRAINT fk_feedback_service_order FOREIGN KEY (service_order_id)
        REFERENCES public.service_order (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_feedback_user FOREIGN KEY (user_id)
        REFERENCES public."user" (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
    TABLESPACE pg_default;
ALTER TABLE IF EXISTS public.feedback
    OWNER to postgres;

-- -----------------------------------------------------
-- Create Table offer_service_order
-- -----------------------------------------------------
DROP TABLE IF EXISTS public.offer_service_order;
CREATE TABLE IF NOT EXISTS public.offer_service_order
(
    id bigint NOT NULL,
    service_order_id bigint NOT NULL,
    specialist_id bigint NOT NULL,
    specialist_answer boolean,
    CONSTRAINT offer_service_order_pkey PRIMARY KEY (id),
    CONSTRAINT fk_offer_service_order_service_order FOREIGN KEY (service_order_id)
        REFERENCES public.service_order (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_offer_service_order_service_specialist FOREIGN KEY (specialist_id)
        REFERENCES public."user" (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
    TABLESPACE pg_default;
ALTER TABLE IF EXISTS public.offer_service_order
    OWNER to postgres;

-- -----------------------------------------------------
-- Populate Table role
-- -----------------------------------------------------
INSERT INTO public.role(id, name)
VALUES (0, 'ROLE_CUSTOMER'),
       (1, 'ROLE_SPECIALIST'),
       (2, 'ROLE_ADMIN');

-- -----------------------------------------------------
-- Populate Table user
-- -----------------------------------------------------
INSERT INTO public."user"(id, first_name, last_name, email, password, role_id)
VALUES (1, 'ADMIN', 'ADMIN', 'admin@mail.com', '$2a$12$ZZzIf3sow0ximoO0yRWvqey2DsVZl1CsZfjozPF0/5y3GgHA/9w2.', 2);

-- -----------------------------------------------------
-- Populate Table service_order_status
-- -----------------------------------------------------
INSERT INTO public.service_order_status(id, name)
VALUES (0, 'CREATED'),
       (1, 'CANCELLED'),
       (2, 'IN_WORK'),
       (3, 'WAIT_FOR_CUSTOMER_APPROVE'),
       (4, 'WAIT_FOR_PAYMENT'),
       (5, 'PAID'),
       (6, 'COMPLETED');
