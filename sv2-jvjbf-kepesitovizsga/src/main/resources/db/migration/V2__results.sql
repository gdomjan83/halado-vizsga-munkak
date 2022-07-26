CREATE TABLE results
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    result_place  VARCHAR(255) NULL,
    result_date   date NULL,
    sport_type    VARCHAR(255) NULL,
    measure_value DOUBLE NOT NULL,
    measure_unit  CHAR   NOT NULL,
    athlete_id    BIGINT NULL,
    CONSTRAINT pk_results PRIMARY KEY (id)
);

ALTER TABLE results
    ADD CONSTRAINT FK_RESULTS_ON_ATHLETE FOREIGN KEY (athlete_id) REFERENCES athletes (id);