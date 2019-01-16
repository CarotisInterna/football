CREATE TABLE user_role (
  id   INT         NOT NULL PRIMARY KEY,
  name VARCHAR(30) NOT NULL
);


INSERT INTO user_role VALUES
  (0, 'Пользователь'),
  (1, 'Организатор'),
  (2, 'Администратор');

CREATE TABLE app_user (
  id       SERIAL       NOT NULL PRIMARY KEY,
  username VARCHAR(30)  NOT NULL,
  password VARCHAR(255) NOT NULL,
  role_id  INT REFERENCES user_role (id)
);

INSERT INTO app_user (id, username, password, role_id) VALUES
  (1, 'admin', 'd033e22ae348aeb5660fc2140aec35850c4da997', 2),
  (2, 'user', '12dea96fec20593566ab75692c9949596833adc9', 0),
  (3, 'manager', '1a8565a9dc72048ba03b4156be3e569f22771f23', 1);

ALTER SEQUENCE app_user_id_seq RESTART WITH 4;