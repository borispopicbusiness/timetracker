ALTER TABLE public.worklog
    DROP COLUMN total_time;
ALTER TABLE public.worklog
    ADD total_time BIGINT;