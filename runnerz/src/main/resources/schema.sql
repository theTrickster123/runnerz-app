CREATE TABLE IF NOT EXISTS Run (
    id SERIAL NOT NULL,
    title VARCHAR(255) NOT NULL,
    started_on TIMESTAMP NOT NULL,
    completed_on TIMESTAMP NOT NULL,
    miles INT NOT NULL,
    location VARCHAR(10) NOT NULL,
    version INT NOT NULL ,
    PRIMARY KEY (id)
);