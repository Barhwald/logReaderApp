CREATE TABLE logs
(
    id             SERIAL PRIMARY KEY,
    log_timestamp      DATETIME NOT NULL,
    severity       VARCHAR(10),
    source_library VARCHAR(256),
    content        VARCHAR(768)
);