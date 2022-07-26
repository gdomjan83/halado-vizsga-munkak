CREATE TABLE schools
(
    school_id    BIGINT AUTO_INCREMENT NOT NULL,
    school_name  VARCHAR(255) NOT NULL,
    postal_code  VARCHAR(255) NULL,
    city         VARCHAR(255) NULL,
    street       VARCHAR(255) NULL,
    house_number INT NULL,
    CONSTRAINT pk_schools PRIMARY KEY (school_id)
);

