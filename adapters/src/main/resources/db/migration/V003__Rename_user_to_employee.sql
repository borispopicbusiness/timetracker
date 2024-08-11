ALTER TABLE public.user
    RENAME TO employee;

ALTER TABLE public.leave
    RENAME COLUMN user_id TO employee_id;

ALTER TABLE public.worklog
    RENAME COLUMN user_id TO employee_id;