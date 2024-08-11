CREATE TABLE public.employee_hierarchy
(
    id        UUID NOT NULL,
    parent_id UUID NOT NULL,
    child_id  UUID NOT NULL,
    PRIMARY KEY (id)
);