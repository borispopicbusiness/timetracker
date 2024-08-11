ALTER TABLE public.worklog
    ALTER COLUMN start_time DROP NOT NULL;
ALTER TABLE public.worklog
    ALTER COLUMN end_time DROP NOT NULL;
ALTER TABLE public.worklog
    ALTER COLUMN total_time DROP NOT NULL;
