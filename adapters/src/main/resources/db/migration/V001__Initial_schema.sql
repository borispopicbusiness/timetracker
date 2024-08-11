CREATE TABLE public.user
(
    id         UUID         NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE public.leave
(
    id               UUID         NOT NULL,
    user_id          UUID         NOT NULL,
    start_time       TIMESTAMP    NOT NULL,
    end_time         TIMESTAMP    NOT NULL,
    description      VARCHAR(2048),
    leave_type       VARCHAR(128) NOT NULL,
    leave_status     VARCHAR(128) NOT NULL,
    request_date     TIMESTAMP    NOT NULL,
    response_comment VARCHAR(2048),
    responder_id     UUID,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES public.user (id),
    FOREIGN KEY (responder_id) REFERENCES public.user (id)
);

CREATE TABLE public.worklog
(
    id            UUID         NOT NULL,
    user_id       UUID         NOT NULL,
    task_name     VARCHAR(255) NOT NULL,
    description   VARCHAR(2048),
    start_time    TIMESTAMP    NOT NULL,
    end_time      TIMESTAMP    NOT NULL,
    total_time    TIMESTAMP    NOT NULL,
    creation_time TIMESTAMP    NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES public.user (id)
);