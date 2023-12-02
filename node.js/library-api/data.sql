--
-- PostgreSQL database dump
--

-- Dumped from database version 14.9 (Ubuntu 14.9-0ubuntu0.22.04.1)
-- Dumped by pg_dump version 14.9 (Ubuntu 14.9-0ubuntu0.22.04.1)

-- Started on 2023-11-30 22:21:01 CET

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 210 (class 1259 OID 27598)
-- Name: books; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.books (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    title text NOT NULL,
    author text NOT NULL,
    category text NOT NULL,
    borrower uuid,
    borrowing_date date,
    return_date date
);


ALTER TABLE public.books OWNER TO postgres;

--
-- TOC entry 209 (class 1259 OID 27590)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    first_name character varying(255) NOT NULL,
    last_name character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    phone character varying(30) NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 3481 (class 0 OID 27598)
-- Dependencies: 210
-- Data for Name: books; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.books VALUES ('389fb0e4-be53-4367-9af0-2ebc6f75c243', 'Jane Eyre', 'Charlotte Brontë', 'romance', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('4d0df240-7293-4aa6-888d-e81c5dce7014', 'North and South', 'Elizabeth Gaskell', 'romance', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('f9b56fca-1482-4307-a1fb-ba9750d3cc98', 'Pride and Prejudice', 'Jane Austen', 'romance', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('879b34eb-99e1-4937-a281-7ff1a9b8ee23', 'Sense and Sensibility', 'Jane Austen', 'romance', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('a66f127e-78a4-45ce-8f96-77e66b44b4c0', 'The Library at Mount Char', 'Scott Hawkins', 'fantasy', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('2f5e1bdf-f41a-46e7-874b-348e441ae95f', 'A Hero Born', 'Jin Yong', 'fantasy', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('d1d120a7-7bd1-430e-9a08-069fcfcb5a6d', 'Get in Trouble', 'Kelly Link', 'fantasy', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('9522309f-d85a-42ef-8383-47795980d9be', 'The Fifth Season', 'N.K. Jemisin', 'fantasy', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('f38c0eee-87da-4935-bc6c-d0fbcc98da81', 'Crime and Punishment', 'Fyodor Dostoevsky', 'thriller', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('a70df310-a052-44e8-bb7c-94d2a8ebcb04', 'The Spy Who Came in From the Cold', 'John le Carré', 'thriller', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('9791b669-7c01-4bf6-bdfa-3aa640977f74', 'Where Are the Children?', 'Mary Higgins Clark', 'thriller', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('f58f09fd-0a79-46ca-ba53-fe55541ff995', 'The Shining', 'Stephen King', 'thriller', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('68a1f537-e006-47d0-bdc4-3bce81ebdd04', 'The Alchemist', 'Paulo Coelho', 'self-help', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('c8d27c59-864a-45ae-a1fd-69fe70affc63', 'Girl, Wash Your Face', 'Rachel Hollis', 'self-help', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('e4bdd862-7b65-4292-a4cf-a767012f4bb0', '12 Rules for Life', 'Jordan Peterson', 'self-help', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('f46b0a47-87b4-46c5-b6c2-2c68c4ac325d', 'A Confederacy of Dunces', 'John Kennedy Toole', 'humor', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('a0aba148-2bdb-48cc-bc23-40ed47907302', 'My Year of Rest and Relaxation', 'Ottessa Moshfegh', 'humor', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('a3bb0690-c453-4f81-950b-64ac653b68be', 'A Hero Born', 'Jin Yong', 'fantasy', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('6ed0626f-3d08-4ef2-bb10-cbd5d94df587', 'Black Leopard, Red Wolf', 'Marlon James', 'fantasy', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('3c3c7600-e9cb-4eeb-af5a-c077bbf58d42', 'Get in Trouble', 'Kelly Link', 'fantasy', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('d5ab7254-377c-4f93-9cb5-02debbd78f09', 'The Spy Who Came in From the Cold', 'John le Carré', 'thriller', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('222278ed-adea-4cca-822e-72f9dbb5fbfa', 'Where Are the Children?', 'Mary Higgins Clark', 'thriller', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('35913c3c-7d33-4e73-a12c-bda9f3ca104a', 'The Shining', 'Stephen King', 'thriller', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('6deb03ae-70c4-44ab-86b0-1682a8219a6b', 'The Alchemist', 'Paulo Coelho', 'self-help', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('3cfe90ca-2688-4552-9bf5-65b9be60ba76', 'Big Magic', 'Elizabeth Gilbert', 'self-help', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('72d04248-c8a6-4d95-8b9a-a4b0dfd02768', 'A Confederacy of Dunces', 'John Kennedy Toole', 'humor', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('8a0a85eb-da0a-43bf-b9f4-539513a713e9', 'My Year of Rest and Relaxation', 'Ottessa Moshfegh', 'humor', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('cb7c09e6-fe94-489b-ae4b-58b27dd6371f', 'Jane Eyre', 'Charlotte Brontë', 'romance', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('69226919-85d4-465e-bb60-ff0d649ccf11', 'North and South', 'Elizabeth Gaskell', 'romance', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('c48a9547-3d4c-4a4e-aa40-ee0941aa9fff', 'A Hero Born', 'Jin Yong', 'fantasy', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('e79ff3df-e1c2-4fd2-8920-cb21afb36a25', 'Black Leopard, Red Wolf', 'Marlon James', 'fantasy', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('946de783-1d28-4a22-8504-64e93231eec3', 'Get in Trouble', 'Kelly Link', 'fantasy', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('8755f383-ffd5-4998-980d-f9f0cd4cd0c8', 'The Fifth Season', 'N.K. Jemisin', 'fantasy', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('9a78eaef-58d2-4313-aa5d-1da057d6768d', 'The Spy Who Came in From the Cold', 'John le Carré', 'thriller', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('f847a96b-4f37-4a22-9cd0-bb6a3d3263e8', 'Where Are the Children?', 'Mary Higgins Clark', 'thriller', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('e2ceb333-18d9-41ba-bdb0-d6bc9d65e049', 'The Shining', 'Stephen King', 'thriller', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('219d3fe2-51c9-408e-94c9-c63c6eb9dcf6', 'The Alchemist', 'Paulo Coelho', 'self-help', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('6c0f725e-0a19-4603-a7b8-2965d38c3e9c', 'Big Magic', 'Elizabeth Gilbert', 'self-help', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('a806b6b3-dae7-4e45-869c-bbe8ddd0c02e', 'A Confederacy of Dunces', 'John Kennedy Toole', 'humor', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('6f422a35-f094-4910-a733-ffa71f595bbb', 'Shit, Actually: The Definitive, 100% Objective Guide to Modern Cinema', 'Lindy West', 'humor', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('464bde42-5296-4364-8d83-63ca94e38922', 'This Is Really Happening', 'Erin Chack', 'humor', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('f561cd6c-8a57-4039-a4ef-7488ae978701', 'My Year of Rest and Relaxation', 'Ottessa Moshfegh', 'humor', NULL, NULL, NULL);
INSERT INTO public.books VALUES ('1e081a02-4ffa-40e1-9b54-1c342ffa7f0b', 'This Is Really Happening', 'Erin Chack', 'humor', '40454603-7607-4349-8e85-57fd86a05801', '2023-11-19', NULL);
INSERT INTO public.books VALUES ('25700310-d388-4f72-8459-120972405870', 'Shit, Actually: The Definitive, 100% Objective Guide to Modern Cinema', 'Lindy West', 'humor', '40454603-7607-4349-8e85-57fd86a05801', '2023-11-19', NULL);
INSERT INTO public.books VALUES ('2eb60195-e1ff-4b72-8a80-d0903467b8cd', 'Big Magic', 'Elizabeth Gilbert', 'self-help', '40454603-7607-4349-8e85-57fd86a05801', '2023-11-19', NULL);
INSERT INTO public.books VALUES ('37f2f602-cae4-4d42-95c5-ac6245ef5e33', 'Black Leopard, Red Wolf', 'Marlon James', 'fantasy', '40454603-7607-4349-8e85-57fd86a05801', '2023-11-19', NULL);
INSERT INTO public.books VALUES ('39c7cbae-c099-483c-9cc7-814ee944177f', 'The Woman in White', 'Wilkie Collins', 'thriller', '40454603-7607-4349-8e85-57fd86a05801', '2023-11-19', NULL);
INSERT INTO public.books VALUES ('19735b3e-463d-485a-971c-9217d94a8a7f', 'The Fifth Season', 'N.K. Jemisin', 'fantasy', NULL, NULL, NULL);


--
-- TOC entry 3480 (class 0 OID 27590)
-- Dependencies: 209
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.users VALUES ('40454603-7607-4349-8e85-57fd86a05801', 'John', 'Doe', 'john.doe@yahoo.com', '+632022233333');


--
-- TOC entry 3339 (class 2606 OID 27605)
-- Name: books books_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.books
    ADD CONSTRAINT books_pkey PRIMARY KEY (id);


--
-- TOC entry 3337 (class 2606 OID 27597)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 3340 (class 2606 OID 27611)
-- Name: books borrower_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.books
    ADD CONSTRAINT borrower_fk FOREIGN KEY (borrower) REFERENCES public.users(id) ON DELETE SET NULL;


-- Completed on 2023-11-30 22:21:01 CET

--
-- PostgreSQL database dump complete
--