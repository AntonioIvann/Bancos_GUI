--
-- PostgreSQL database dump
--

\restrict Z5QKWNRqzCAIlqmOjfQWUwpkLmD1ykWIQuf0yrZIOsGYh4gT3dEMyece46hF7mS

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
-- Name: bancos; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.bancos (
    idb integer NOT NULL,
    nombre character varying(100) NOT NULL
);


ALTER TABLE public.bancos OWNER TO admin;

--
-- Name: bancos_idb_seq; Type: SEQUENCE; Schema: public; Owner: admin
--

CREATE SEQUENCE public.bancos_idb_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.bancos_idb_seq OWNER TO admin;

--
-- Name: bancos_idb_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: admin
--

ALTER SEQUENCE public.bancos_idb_seq OWNED BY public.bancos.idb;


--
-- Name: clientes; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.clientes (
    idc integer NOT NULL,
    apellido_paterno character varying(100) NOT NULL,
    apellido_materno character varying(100) NOT NULL,
    nombre character varying(100) NOT NULL
);


ALTER TABLE public.clientes OWNER TO admin;

--
-- Name: clientes_idc_seq; Type: SEQUENCE; Schema: public; Owner: admin
--

CREATE SEQUENCE public.clientes_idc_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.clientes_idc_seq OWNER TO admin;

--
-- Name: clientes_idc_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: admin
--

ALTER SEQUENCE public.clientes_idc_seq OWNED BY public.clientes.idc;


--
-- Name: movimientos; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.movimientos (
    idm integer NOT NULL,
    idt integer NOT NULL,
    cantidad numeric(15,2) NOT NULL,
    tipo character varying(10) NOT NULL,
    descripcion character varying(255) NOT NULL,
    fecha_movimiento timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT movimientos_cantidad_check CHECK ((cantidad > (0)::numeric)),
    CONSTRAINT movimientos_tipo_check CHECK (((tipo)::text = ANY ((ARRAY['CREDITO'::character varying, 'DEBITO'::character varying])::text[])))
);


ALTER TABLE public.movimientos OWNER TO admin;

--
-- Name: movimientos_idm_seq; Type: SEQUENCE; Schema: public; Owner: admin
--

CREATE SEQUENCE public.movimientos_idm_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.movimientos_idm_seq OWNER TO admin;

--
-- Name: movimientos_idm_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: admin
--

ALTER SEQUENCE public.movimientos_idm_seq OWNED BY public.movimientos.idm;


--
-- Name: tarjetas; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.tarjetas (
    idt integer NOT NULL,
    idc integer NOT NULL,
    idb integer NOT NULL,
    numero_tarjeta character varying(16) NOT NULL,
    saldo numeric(15,2) DEFAULT 0.00 NOT NULL,
    fecha_creacion timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    activa boolean DEFAULT true,
    CONSTRAINT tarjetas_saldo_check CHECK ((saldo >= (0)::numeric))
);


ALTER TABLE public.tarjetas OWNER TO admin;

--
-- Name: tarjetas_idt_seq; Type: SEQUENCE; Schema: public; Owner: admin
--

CREATE SEQUENCE public.tarjetas_idt_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.tarjetas_idt_seq OWNER TO admin;

--
-- Name: tarjetas_idt_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: admin
--

ALTER SEQUENCE public.tarjetas_idt_seq OWNED BY public.tarjetas.idt;


--
-- Name: usuarios; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.usuarios (
    idu integer NOT NULL,
    usuario character varying(50) NOT NULL,
    "contraseña" character varying(255) NOT NULL,
    idc integer NOT NULL,
    es_admin boolean DEFAULT false,
    activo boolean DEFAULT true,
    fecha_creacion timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.usuarios OWNER TO admin;

--
-- Name: usuarios_idu_seq; Type: SEQUENCE; Schema: public; Owner: admin
--

CREATE SEQUENCE public.usuarios_idu_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.usuarios_idu_seq OWNER TO admin;

--
-- Name: usuarios_idu_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: admin
--

ALTER SEQUENCE public.usuarios_idu_seq OWNED BY public.usuarios.idu;


--
-- Name: bancos idb; Type: DEFAULT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.bancos ALTER COLUMN idb SET DEFAULT nextval('public.bancos_idb_seq'::regclass);


--
-- Name: clientes idc; Type: DEFAULT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.clientes ALTER COLUMN idc SET DEFAULT nextval('public.clientes_idc_seq'::regclass);


--
-- Name: movimientos idm; Type: DEFAULT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.movimientos ALTER COLUMN idm SET DEFAULT nextval('public.movimientos_idm_seq'::regclass);


--
-- Name: tarjetas idt; Type: DEFAULT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.tarjetas ALTER COLUMN idt SET DEFAULT nextval('public.tarjetas_idt_seq'::regclass);


--
-- Name: usuarios idu; Type: DEFAULT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.usuarios ALTER COLUMN idu SET DEFAULT nextval('public.usuarios_idu_seq'::regclass);


--
-- Data for Name: bancos; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.bancos (idb, nombre) FROM stdin;
3	BBVA
\.


--
-- Data for Name: clientes; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.clientes (idc, apellido_paterno, apellido_materno, nombre) FROM stdin;
7	reyes	perez	antonio
8	guewkuew	silhsilhec	Antonio
\.


--
-- Data for Name: movimientos; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.movimientos (idm, idt, cantidad, tipo, descripcion, fecha_movimiento) FROM stdin;
\.


--
-- Data for Name: tarjetas; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.tarjetas (idt, idc, idb, numero_tarjeta, saldo, fecha_creacion, activa) FROM stdin;
9	7	3	7906637255187755	0.00	2025-11-26 07:42:41.070061	t
\.


--
-- Data for Name: usuarios; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.usuarios (idu, usuario, "contraseña", idc, es_admin, activo, fecha_creacion) FROM stdin;
7	antonio	0111fa6cad2892e3cf03da11afb8bb7b47f96f3a2dfdaa96630f374769545a82	7	f	t	2025-11-26 07:41:37.794991
8	Antonio	b51b3c60a115bd28bde9faf3a96158d1ea6999f4fe7299d5c65d8873bcda41cd	8	t	t	2025-11-26 07:41:58.92665
\.


--
-- Name: bancos_idb_seq; Type: SEQUENCE SET; Schema: public; Owner: admin
--

SELECT pg_catalog.setval('public.bancos_idb_seq', 5, true);


--
-- Name: clientes_idc_seq; Type: SEQUENCE SET; Schema: public; Owner: admin
--

SELECT pg_catalog.setval('public.clientes_idc_seq', 8, true);


--
-- Name: movimientos_idm_seq; Type: SEQUENCE SET; Schema: public; Owner: admin
--

SELECT pg_catalog.setval('public.movimientos_idm_seq', 7, true);


--
-- Name: tarjetas_idt_seq; Type: SEQUENCE SET; Schema: public; Owner: admin
--

SELECT pg_catalog.setval('public.tarjetas_idt_seq', 9, true);


--
-- Name: usuarios_idu_seq; Type: SEQUENCE SET; Schema: public; Owner: admin
--

SELECT pg_catalog.setval('public.usuarios_idu_seq', 8, true);


--
-- Name: bancos bancos_nombre_key; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.bancos
    ADD CONSTRAINT bancos_nombre_key UNIQUE (nombre);


--
-- Name: bancos bancos_pkey; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.bancos
    ADD CONSTRAINT bancos_pkey PRIMARY KEY (idb);


--
-- Name: clientes clientes_pkey; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.clientes
    ADD CONSTRAINT clientes_pkey PRIMARY KEY (idc);


--
-- Name: movimientos movimientos_pkey; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.movimientos
    ADD CONSTRAINT movimientos_pkey PRIMARY KEY (idm);


--
-- Name: tarjetas tarjetas_numero_tarjeta_key; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.tarjetas
    ADD CONSTRAINT tarjetas_numero_tarjeta_key UNIQUE (numero_tarjeta);


--
-- Name: tarjetas tarjetas_pkey; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.tarjetas
    ADD CONSTRAINT tarjetas_pkey PRIMARY KEY (idt);


--
-- Name: usuarios usuarios_pkey; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_pkey PRIMARY KEY (idu);


--
-- Name: usuarios usuarios_usuario_key; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_usuario_key UNIQUE (usuario);


--
-- Name: idx_clientes_nombre; Type: INDEX; Schema: public; Owner: admin
--

CREATE INDEX idx_clientes_nombre ON public.clientes USING btree (nombre);


--
-- Name: idx_movimientos_fecha; Type: INDEX; Schema: public; Owner: admin
--

CREATE INDEX idx_movimientos_fecha ON public.movimientos USING btree (fecha_movimiento);


--
-- Name: idx_movimientos_tarjeta; Type: INDEX; Schema: public; Owner: admin
--

CREATE INDEX idx_movimientos_tarjeta ON public.movimientos USING btree (idt);


--
-- Name: idx_tarjetas_banco; Type: INDEX; Schema: public; Owner: admin
--

CREATE INDEX idx_tarjetas_banco ON public.tarjetas USING btree (idb);


--
-- Name: idx_tarjetas_cliente; Type: INDEX; Schema: public; Owner: admin
--

CREATE INDEX idx_tarjetas_cliente ON public.tarjetas USING btree (idc);


--
-- Name: idx_usuarios_cliente; Type: INDEX; Schema: public; Owner: admin
--

CREATE INDEX idx_usuarios_cliente ON public.usuarios USING btree (idc);


--
-- Name: idx_usuarios_usuario; Type: INDEX; Schema: public; Owner: admin
--

CREATE INDEX idx_usuarios_usuario ON public.usuarios USING btree (usuario);


--
-- Name: movimientos movimientos_idt_fkey; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.movimientos
    ADD CONSTRAINT movimientos_idt_fkey FOREIGN KEY (idt) REFERENCES public.tarjetas(idt) ON DELETE CASCADE;


--
-- Name: tarjetas tarjetas_idb_fkey; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.tarjetas
    ADD CONSTRAINT tarjetas_idb_fkey FOREIGN KEY (idb) REFERENCES public.bancos(idb) ON DELETE RESTRICT;


--
-- Name: tarjetas tarjetas_idc_fkey; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.tarjetas
    ADD CONSTRAINT tarjetas_idc_fkey FOREIGN KEY (idc) REFERENCES public.clientes(idc) ON DELETE CASCADE;


--
-- Name: usuarios usuarios_idc_fkey; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_idc_fkey FOREIGN KEY (idc) REFERENCES public.clientes(idc) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

\unrestrict Z5QKWNRqzCAIlqmOjfQWUwpkLmD1ykWIQuf0yrZIOsGYh4gT3dEMyece46hF7mS

