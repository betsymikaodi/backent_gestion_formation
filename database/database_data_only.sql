--
-- PostgreSQL database dump
--

\restrict NKzXHFiQvtStra3CaWbFxjctq477x6Q02aXg4OEUtNmB7GOacNRAkBZRD2K2hI4

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

--
-- Data for Name: apprenant; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.apprenant VALUES (1, 'RAKOTO', 'Jean', 'jean.rakoto@email.com', '+261 34 12 345 67', 'Lot ABC Antananarivo', '1995-05-15', '123456789012');
INSERT INTO public.apprenant VALUES (2, 'RABE', 'Marie', 'marie.rabe@email.com', '+261 33 11 222 33', 'Antsirabe', '1998-03-20', '234567890123');
INSERT INTO public.apprenant VALUES (3, 'ANDRY', 'Paul', 'paul.andry@email.com', '+261 32 44 555 66', 'Fianarantsoa', '1992-11-08', '345678901234');
INSERT INTO public.apprenant VALUES (4, 'HERY', 'Sophie', 'sophie.hery@email.com', '+261 34 77 888 99', 'Mahajanga', '1996-07-12', '456789012345');
INSERT INTO public.apprenant VALUES (5, 'RINA', 'David', 'david.rina@email.com', '+261 33 66 777 88', 'Toamasina', '1994-09-25', '567890123456');
INSERT INTO public.apprenant VALUES (6, 'NOFY', 'Elson', 'elsonnofy@gmail.com', '+261 34 00 11 22', 'Ambodirano', '2003-01-01', '201032120421');
INSERT INTO public.apprenant VALUES (8, 'RAZAFY', 'Mialy', 'mialy.razafy@test.com', '+261 34 99 88 77', 'Antananarivo Madagascar', '2001-06-15', '301061523456');
INSERT INTO public.apprenant VALUES (9, 'dieu donne', 'fandresena', 'rafandresenadieudonne@gmail.com', '0388243781', 'IG006/3204 Igaga', '2004-06-17', '325222222442');


--
-- Data for Name: formation; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.formation VALUES (1, 'Développement Web Full Stack', 'Formation complète en développement web avec React et Spring Boot', 120, 500000.00);
INSERT INTO public.formation VALUES (2, 'Python pour Data Science', 'Analyse de données et machine learning avec Python', 80, 350000.00);
INSERT INTO public.formation VALUES (3, 'Marketing Digital', 'Stratégies de marketing en ligne et réseaux sociaux', 60, 250000.00);
INSERT INTO public.formation VALUES (4, 'Comptabilité et Gestion', 'Bases de la comptabilité et gestion d''entreprise', 100, 300000.00);
INSERT INTO public.formation VALUES (5, 'Anglais Commercial', 'Anglais professionnel pour le monde des affaires', 40, 150000.00);


--
-- Data for Name: inscription; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.inscription VALUES (1, 1, 1, '2024-01-15', 'Confirmé', 25000.00);
INSERT INTO public.inscription VALUES (2, 2, 1, '2024-01-18', 'Confirmé', 25000.00);
INSERT INTO public.inscription VALUES (3, 3, 2, '2024-01-20', 'Confirmé', 20000.00);
INSERT INTO public.inscription VALUES (4, 4, 3, '2024-01-25', 'En attente', 15000.00);
INSERT INTO public.inscription VALUES (5, 5, 4, '2024-02-01', 'Confirmé', 20000.00);
INSERT INTO public.inscription VALUES (6, 1, 5, '2024-02-05', 'Confirmé', 10000.00);


--
-- Data for Name: paiement; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.paiement VALUES (1, 1, '2024-01-16', 125000.00, 'Virement', 'Module 1');
INSERT INTO public.paiement VALUES (2, 1, '2024-02-15', 125000.00, 'Virement', 'Module 2');
INSERT INTO public.paiement VALUES (3, 1, '2024-03-15', 125000.00, 'Virement', 'Module 3');
INSERT INTO public.paiement VALUES (4, 2, '2024-01-20', 150000.00, 'Mobile Money', 'Module 1');
INSERT INTO public.paiement VALUES (5, 2, '2024-02-20', 150000.00, 'Mobile Money', 'Module 2');
INSERT INTO public.paiement VALUES (6, 3, '2024-01-22', 100000.00, 'Espèce', 'Module 1');
INSERT INTO public.paiement VALUES (7, 3, '2024-02-22', 100000.00, 'Espèce', 'Module 2');
INSERT INTO public.paiement VALUES (8, 5, '2024-02-03', 100000.00, 'Virement', 'Module 1');
INSERT INTO public.paiement VALUES (9, 5, '2024-03-03', 100000.00, 'Virement', 'Module 2');
INSERT INTO public.paiement VALUES (10, 6, '2024-02-07', 50000.00, 'Mobile Money', 'Module 1');
INSERT INTO public.paiement VALUES (11, 6, '2024-03-07', 50000.00, 'Mobile Money', 'Module 2');


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
-- PostgreSQL database dump complete
--

\unrestrict NKzXHFiQvtStra3CaWbFxjctq477x6Q02aXg4OEUtNmB7GOacNRAkBZRD2K2hI4

