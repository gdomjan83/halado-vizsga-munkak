CREATE TABLE athletes
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    athlete_name VARCHAR(255) NULL,
    athlete_sex  VARCHAR(255) NULL,
    CONSTRAINT pk_athletes PRIMARY KEY (id)
);

