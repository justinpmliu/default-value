DROP TABLE IF EXISTS default_value;

CREATE TABLE default_value (
    id INT NOT NULL AUTO_INCREMENT,
    service VARCHAR(10) NOT NULL,
    clazz VARCHAR(200) NOT NULL,
    field VARCHAR(100) NOT NULL,
    value VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO default_value (service, clazz, field, value)
VALUES ('s1', 'user', 'id', '99999');

INSERT INTO default_value (service, clazz, field, value)
VALUES ('s1', 'user.address', 'room', '999');

INSERT INTO default_value (service, clazz, field, value)
VALUES ('s1', 'user.address', 'primary', 'false');

INSERT INTO default_value (service, clazz, field, value)
VALUES ('s2', 'user', 'id', '10000');