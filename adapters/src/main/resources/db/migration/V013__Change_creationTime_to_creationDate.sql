ALTER TABLE public.worklog
    RENAME COLUMN creation_time TO creation_date;
ALTER TABLE public.worklog
    ALTER COLUMN creation_date TYPE DATE;