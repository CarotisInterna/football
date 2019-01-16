-- город
CREATE TABLE city (
  id   SERIAL      NOT NULL  PRIMARY KEY,
  name VARCHAR(30) NOT NULL
);
-- стадион
CREATE TABLE stadium (
  id       SERIAL      NOT NULL  PRIMARY KEY,
  city_id  INT      NOT NULL,
  name     VARCHAR(40) NOT NULL,
  capacity BIGINT      NOT NULL,
  FOREIGN KEY (city_id) REFERENCES city (id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- роль в команде
CREATE TABLE role (
  id   SERIAL      NOT NULL PRIMARY KEY,
  name VARCHAR(30) NOT NULL
);

-- команда
CREATE TABLE team (
  id         SERIAL PRIMARY KEY,
  name       VARCHAR(30) NOT NULL,
  city_id    INT      NOT NULL REFERENCES city (id) ON DELETE CASCADE ON UPDATE CASCADE,
  prev_place INT
);
-- игрок
CREATE TABLE player (
  id      SERIAL                      NOT NULL PRIMARY KEY,
  city_id INT                      NOT NULL REFERENCES city (id) ON DELETE CASCADE ON UPDATE CASCADE,
  name    VARCHAR(30)                 NOT NULL,
  surname VARCHAR(40)                 NOT NULL,
  age     INT                         NOT NULL,
  role_id INT                     NOT NULL REFERENCES role (id) ON DELETE CASCADE ON UPDATE CASCADE,
  team_id INT REFERENCES team (id) ON DELETE CASCADE
);


CREATE TABLE player_team_history (
  player_id   SERIAL NOT NULL REFERENCES player (id) ON DELETE CASCADE ON UPDATE CASCADE,
  team_id     SERIAL  REFERENCES team (id) ON DELETE CASCADE ON UPDATE CASCADE,
  action_type SERIAL,
  action_date DATE
);

-- CREATE TABLE player_team (
--   player_id Serial,
--   team_id   Serial,
--   PRIMARY KEY (player_id, team_id),
--   FOREIGN KEY (player_id) REFERENCES player (id) ON DELETE CASCADE ON UPDATE CASCADE,
--   FOREIGN KEY (team_id) REFERENCES team (id) ON DELETE CASCADE ON UPDATE CASCADE
-- );

-- матч
CREATE TABLE match (
  id         SERIAL NOT NULL PRIMARY KEY,
  stadium_id INT NOT NULL REFERENCES stadium (id) ON DELETE CASCADE ON UPDATE CASCADE,
  team_1_id  INT NOT NULL REFERENCES team (id) ON DELETE CASCADE ON UPDATE CASCADE,
  team_2_id  INT NOT NULL REFERENCES team (id) ON DELETE CASCADE ON UPDATE CASCADE,
  date       DATE,
  team1_res  INT,
  team2_res  INT
);

-- билет на матч
CREATE TABLE ticket (
  id       SERIAL           NOT NULL PRIMARY KEY,
  match_id SERIAL           NOT NULL REFERENCES match (id) ON DELETE CASCADE ON UPDATE CASCADE,
  price    DOUBLE PRECISION
);

-- тренер
CREATE TABLE coach (
  id      SERIAL      NOT NULL PRIMARY KEY,
  name    VARCHAR(30) NOT NULL,
  surname VARCHAR(40) NOT NULL,
  team_id INT REFERENCES team (id) NULL
);

CREATE TABLE coach_team_history (
  coach_id    SERIAL NOT NULL REFERENCES coach (id) ON DELETE CASCADE ON UPDATE CASCADE,
  team_id     SERIAL REFERENCES team (id),
  action_type SERIAL,
  action_date DATE
);

-- -- связь тренера с командой
-- CREATE TABLE coach_team (
--   coach_id SERIAL NOT NULL REFERENCES coach (id) ON DELETE CASCADE ON UPDATE CASCADE,
--   team_id  SERIAL NOT NULL REFERENCES team (id) ON DELETE CASCADE ON UPDATE CASCADE,
--   PRIMARY KEY (coach_id, team_id)
-- );


INSERT INTO role (id, name) VALUES
  (0, 'Защитник'),
  (1, 'Нападающий'),
  (2, 'Вратарь'),
  (3, 'Полузащитник');
