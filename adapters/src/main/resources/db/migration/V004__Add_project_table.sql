CREATE TABLE public.project
(
    id          UUID PRIMARY KEY,
    name        VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(50)        NOT NULL
);