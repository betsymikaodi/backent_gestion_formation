--
-- PostgreSQL database dump
--

\restrict ByaHdaDkb7eWXnDwrvkNGlMkEqcN94XyaCCV6cIiAJia5Z5XdnUoRXeemTyhANe

-- Dumped from database version 16.10 (Ubuntu 16.10-0ubuntu0.24.04.1)
-- Dumped by pg_dump version 16.10 (Ubuntu 16.10-0ubuntu0.24.04.1)

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
-- Name: apprenant; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.apprenant (
    id_apprenant bigint NOT NULL,
    nom character varying(50) NOT NULL,
    prenom character varying(50) NOT NULL,
    email character varying(100) NOT NULL,
    telephone character varying(20),
    adresse text,
    date_naissance date,
    cin character varying(20) NOT NULL
);


ALTER TABLE public.apprenant OWNER TO postgres;

--
-- Name: apprenant_id_apprenant_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.apprenant_id_apprenant_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.apprenant_id_apprenant_seq OWNER TO postgres;

--
-- Name: apprenant_id_apprenant_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.apprenant_id_apprenant_seq OWNED BY public.apprenant.id_apprenant;


--
-- Name: formation; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.formation (
    id_formation bigint NOT NULL,
    nom character varying(100) NOT NULL,
    description text,
    duree integer NOT NULL,
    frais numeric(10,2) NOT NULL
);


ALTER TABLE public.formation OWNER TO postgres;

--
-- Name: formation_id_formation_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.formation_id_formation_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.formation_id_formation_seq OWNER TO postgres;

--
-- Name: formation_id_formation_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.formation_id_formation_seq OWNED BY public.formation.id_formation;


--
-- Name: inscription; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.inscription (
    id_inscription bigint NOT NULL,
    id_apprenant bigint NOT NULL,
    id_formation bigint NOT NULL,
    date_inscription date DEFAULT CURRENT_DATE,
    statut character varying(20) DEFAULT 'En attente'::character varying,
    droit_inscription numeric(10,2) DEFAULT 0
);


ALTER TABLE public.inscription OWNER TO postgres;

--
-- Name: inscription_id_inscription_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.inscription_id_inscription_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.inscription_id_inscription_seq OWNER TO postgres;

--
-- Name: inscription_id_inscription_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.inscription_id_inscription_seq OWNED BY public.inscription.id_inscription;


--
-- Name: paiement; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.paiement (
    id_paiement bigint NOT NULL,
    id_inscription bigint NOT NULL,
    date_paiement date DEFAULT CURRENT_DATE,
    montant numeric(10,2) NOT NULL,
    mode_paiement character varying(20) DEFAULT 'Espèce'::character varying,
    module character varying(20) DEFAULT 'Module 1'::character varying
);


ALTER TABLE public.paiement OWNER TO postgres;

--
-- Name: paiement_id_paiement_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.paiement_id_paiement_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.paiement_id_paiement_seq OWNER TO postgres;

--
-- Name: paiement_id_paiement_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.paiement_id_paiement_seq OWNED BY public.paiement.id_paiement;


--
-- Name: apprenant id_apprenant; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.apprenant ALTER COLUMN id_apprenant SET DEFAULT nextval('public.apprenant_id_apprenant_seq'::regclass);


--
-- Name: formation id_formation; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.formation ALTER COLUMN id_formation SET DEFAULT nextval('public.formation_id_formation_seq'::regclass);


--
-- Name: inscription id_inscription; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.inscription ALTER COLUMN id_inscription SET DEFAULT nextval('public.inscription_id_inscription_seq'::regclass);


--
-- Name: paiement id_paiement; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.paiement ALTER COLUMN id_paiement SET DEFAULT nextval('public.paiement_id_paiement_seq'::regclass);


--
-- Data for Name: apprenant; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.apprenant (id_apprenant, nom, prenom, email, telephone, adresse, date_naissance, cin) FROM stdin;
1	RAKOTO	Jean	jean.rakoto@email.com	+261 34 12 345 67	Lot ABC Antananarivo	1995-05-15	123456789012
2	RABE	Marie	marie.rabe@email.com	+261 33 11 222 33	Antsirabe	1998-03-20	234567890123
3	ANDRY	Paul	paul.andry@email.com	+261 32 44 555 66	Fianarantsoa	1992-11-08	345678901234
4	HERY	Sophie	sophie.hery@email.com	+261 34 77 888 99	Mahajanga	1996-07-12	456789012345
5	RINA	David	david.rina@email.com	+261 33 66 777 88	Toamasina	1994-09-25	567890123456
6	NOFY	Elson	elsonnofy@gmail.com	+261 34 00 11 22	Ambodirano	2003-01-01	201032120421
8	RAZAFY	Mialy	mialy.razafy@test.com	+261 34 99 88 77	Antananarivo Madagascar	2001-06-15	301061523456
9	dieu donne	fandresena	rafandresenadieudonne@gmail.com	0388243781	IG006/3204 Igaga	2004-06-17	325222222442
\.


--
-- Data for Name: formation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.formation (id_formation, nom, description, duree, frais) FROM stdin;
1	Développement Web Full Stack	Formation complète en développement web avec React et Spring Boot	120	500000.00
2	Python pour Data Science	Analyse de données et machine learning avec Python	80	350000.00
3	Marketing Digital	Stratégies de marketing en ligne et réseaux sociaux	60	250000.00
4	Comptabilité et Gestion	Bases de la comptabilité et gestion d'entreprise	100	300000.00
5	Anglais Commercial	Anglais professionnel pour le monde des affaires	40	150000.00
\.


--
-- Data for Name: inscription; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.inscription (id_inscription, id_apprenant, id_formation, date_inscription, statut, droit_inscription) FROM stdin;
1	1	1	2024-01-15	Confirmé	25000.00
2	2	1	2024-01-18	Confirmé	25000.00
3	3	2	2024-01-20	Confirmé	20000.00
4	4	3	2024-01-25	En attente	15000.00
5	5	4	2024-02-01	Confirmé	20000.00
6	1	5	2024-02-05	Confirmé	10000.00
\.


--
-- Data for Name: paiement; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.paiement (id_paiement, id_inscription, date_paiement, montant, mode_paiement, module) FROM stdin;
1	1	2024-01-16	125000.00	Virement	Module 1
2	1	2024-02-15	125000.00	Virement	Module 2
3	1	2024-03-15	125000.00	Virement	Module 3
4	2	2024-01-20	150000.00	Mobile Money	Module 1
5	2	2024-02-20	150000.00	Mobile Money	Module 2
6	3	2024-01-22	100000.00	Espèce	Module 1
7	3	2024-02-22	100000.00	Espèce	Module 2
8	5	2024-02-03	100000.00	Virement	Module 1
9	5	2024-03-03	100000.00	Virement	Module 2
10	6	2024-02-07	50000.00	Mobile Money	Module 1
11	6	2024-03-07	50000.00	Mobile Money	Module 2
\.


--
-- Name: apprenant_id_apprenant_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.apprenant_id_apprenant_seq', 9, true);


--
-- Name: formation_id_formation_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.formation_id_formation_seq', 5, true);


--
-- Name: inscription_id_inscription_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.inscription_id_inscription_seq', 6, true);


--
-- Name: paiement_id_paiement_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.paiement_id_paiement_seq', 11, true);


--
-- Name: apprenant apprenant_cin_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.apprenant
    ADD CONSTRAINT apprenant_cin_key UNIQUE (cin);


--
-- Name: apprenant apprenant_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.apprenant
    ADD CONSTRAINT apprenant_email_key UNIQUE (email);


--
-- Name: apprenant apprenant_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.apprenant
    ADD CONSTRAINT apprenant_pkey PRIMARY KEY (id_apprenant);


--
-- Name: formation formation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.formation
    ADD CONSTRAINT formation_pkey PRIMARY KEY (id_formation);


--
-- Name: inscription inscription_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.inscription
    ADD CONSTRAINT inscription_pkey PRIMARY KEY (id_inscription);


--
-- Name: paiement paiement_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.paiement
    ADD CONSTRAINT paiement_pkey PRIMARY KEY (id_paiement);


--
-- Name: idx_apprenant_cin; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_apprenant_cin ON public.apprenant USING btree (cin);


--
-- Name: idx_apprenant_email; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_apprenant_email ON public.apprenant USING btree (email);


--
-- Name: idx_formation_nom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_formation_nom ON public.formation USING btree (nom);


--
-- Name: idx_inscription_apprenant; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_inscription_apprenant ON public.inscription USING btree (id_apprenant);


--
-- Name: idx_paiement_inscription; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_paiement_inscription ON public.paiement USING btree (id_inscription);


--
-- Name: inscription inscription_id_apprenant_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.inscription
    ADD CONSTRAINT inscription_id_apprenant_fkey FOREIGN KEY (id_apprenant) REFERENCES public.apprenant(id_apprenant) ON DELETE CASCADE;


--
-- Name: inscription inscription_id_formation_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.inscription
    ADD CONSTRAINT inscription_id_formation_fkey FOREIGN KEY (id_formation) REFERENCES public.formation(id_formation) ON DELETE CASCADE;


--
-- Name: paiement paiement_id_inscription_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.paiement
    ADD CONSTRAINT paiement_id_inscription_fkey FOREIGN KEY (id_inscription) REFERENCES public.inscription(id_inscription) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

\unrestrict ByaHdaDkb7eWXnDwrvkNGlMkEqcN94XyaCCV6cIiAJia5Z5XdnUoRXeemTyhANe

