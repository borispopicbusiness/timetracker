CREATE TABLE public.project_employees
(
    id           UUID NOT NULL,
    projects_id  UUID NOT NULL,
    employees_id UUID NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (projects_id) REFERENCES public.project (id),
    FOREIGN KEY (employees_id) REFERENCES public.employee (id)
);