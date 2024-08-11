INSERT INTO public.employee (id, first_name, last_name, email, password, principal_id)
VALUES ('b136ec22-b109-11ed-afa1-0242ac120002', 'Nikola', 'Tesla', 'nikola.tesla@semiramide.com', 'admin',
        '82aee0b0-68eb-442d-a94e-cdb403cfd66f');
INSERT INTO public.employee (id, first_name, last_name, email, password, principal_id)
VALUES ('4cc21b96-b126-11ed-afa1-0242ac120002', 'Filip', 'Visnjic', 'filip.visnjic@semiramide.com', 'admin',
        '641d2a88-d6d3-481a-bddf-575644d5c059');
INSERT INTO public.employee (id, first_name, last_name, email, password, principal_id)
VALUES ('68d036ec-b126-11ed-afa1-0242ac120002', 'Josif', 'Pancic', 'josif.pancic@semiramide.com', 'admin',
        '05fb9a71-e239-451f-906c-aaadb6ebe330');
INSERT INTO public.employee (id, first_name, last_name, email, password, principal_id)
VALUES ('80487ef6-b126-11ed-afa1-0242ac120002', 'Mihailo', 'Pupin', 'mihajlo.pupin@semiramide.com', 'admin',
        '20b8115f-cd1a-4a6e-b397-110ba5e7d58d');
INSERT INTO public.employee (id, first_name, last_name, email, password, principal_id)
VALUES ('99b9bda0-b126-11ed-afa1-0242ac120002', 'Mileva', 'Maric', 'mileva.maric@semiramide.com', 'admin',
        '4e805a3d-aa07-47aa-8249-0b7c08a74bb9');
INSERT INTO public.employee (id, first_name, last_name, email, password, principal_id)
VALUES ('b05d847e-b126-11ed-afa1-0242ac120002', 'Nadezda', 'Petrovic', 'nadezda.petrovic@semiramide.com', 'admin',
        'b1d9379b-7ce4-4ecc-b916-7ff0c42e9821');
INSERT INTO public.employee (id, first_name, last_name, email, password, principal_id)
VALUES ('c98edbe6-b126-11ed-afa1-0242ac120002', 'Rudjer', 'Boskovic', 'rudjer.boskovic@semiramide.com', 'admin',
        'fbdbe1cc-d9d2-4836-8cbd-0609db503ed1');

INSERT INTO public.employee_hierarchy (id, parent_id, child_id)
VALUES ('117727ba-b127-11ed-afa1-0242ac120002', 'b136ec22-b109-11ed-afa1-0242ac120002',
        '80487ef6-b126-11ed-afa1-0242ac120002');
INSERT INTO public.employee_hierarchy (id, parent_id, child_id)
VALUES ('23f151e0-b127-11ed-afa1-0242ac120002', 'b136ec22-b109-11ed-afa1-0242ac120002',
        'c98edbe6-b126-11ed-afa1-0242ac120002');
INSERT INTO public.employee_hierarchy (id, parent_id, child_id)
VALUES ('314b9224-b127-11ed-afa1-0242ac120002', '80487ef6-b126-11ed-afa1-0242ac120002',
        '4cc21b96-b126-11ed-afa1-0242ac120002');
INSERT INTO public.employee_hierarchy (id, parent_id, child_id)
VALUES ('514b65b8-b127-11ed-afa1-0242ac120002', '80487ef6-b126-11ed-afa1-0242ac120002',
        '68d036ec-b126-11ed-afa1-0242ac120002');
INSERT INTO public.employee_hierarchy (id, parent_id, child_id)
VALUES ('5c97c1a0-b127-11ed-afa1-0242ac120002', 'c98edbe6-b126-11ed-afa1-0242ac120002',
        '68d036ec-b126-11ed-afa1-0242ac120002');
INSERT INTO public.employee_hierarchy (id, parent_id, child_id)
VALUES ('78b48576-b127-11ed-afa1-0242ac120002', 'c98edbe6-b126-11ed-afa1-0242ac120002',
        '99b9bda0-b126-11ed-afa1-0242ac120002');
INSERT INTO public.employee_hierarchy (id, parent_id, child_id)
VALUES ('89613e78-b127-11ed-afa1-0242ac120002', '68d036ec-b126-11ed-afa1-0242ac120002',
        'b05d847e-b126-11ed-afa1-0242ac120002');

INSERT INTO public.project (id, name, description)
VALUES ('99037468-b127-11ed-afa1-0242ac120002', 'Build a spaceship',
        'Build a spaceship for exploration beyond the Solar System.');

INSERT INTO public.project_employees (id, projects_id, employees_id)
VALUES ('01cb93c2-b128-11ed-afa1-0242ac120002', '99037468-b127-11ed-afa1-0242ac120002',
        'b136ec22-b109-11ed-afa1-0242ac120002');
INSERT INTO public.project_employees (id, projects_id, employees_id)
VALUES ('fbdfb4ca-b127-11ed-afa1-0242ac120002', '99037468-b127-11ed-afa1-0242ac120002',
        '4cc21b96-b126-11ed-afa1-0242ac120002');
INSERT INTO public.project_employees (id, projects_id, employees_id)
VALUES ('f866ee62-b127-11ed-afa1-0242ac120002', '99037468-b127-11ed-afa1-0242ac120002',
        '68d036ec-b126-11ed-afa1-0242ac120002');
INSERT INTO public.project_employees (id, projects_id, employees_id)
VALUES ('f4a4a562-b127-11ed-afa1-0242ac120002', '99037468-b127-11ed-afa1-0242ac120002',
        '80487ef6-b126-11ed-afa1-0242ac120002');
INSERT INTO public.project_employees (id, projects_id, employees_id)
VALUES ('f0d7018c-b127-11ed-afa1-0242ac120002', '99037468-b127-11ed-afa1-0242ac120002',
        '99b9bda0-b126-11ed-afa1-0242ac120002');
INSERT INTO public.project_employees (id, projects_id, employees_id)
VALUES ('ed2bc36a-b127-11ed-afa1-0242ac120002', '99037468-b127-11ed-afa1-0242ac120002',
        'b05d847e-b126-11ed-afa1-0242ac120002');
INSERT INTO public.project_employees (id, projects_id, employees_id)
VALUES ('99037468-b127-11ed-afa1-0242ac120002', '99037468-b127-11ed-afa1-0242ac120002',
        'c98edbe6-b126-11ed-afa1-0242ac120002');
