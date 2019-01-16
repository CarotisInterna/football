INSERT INTO city (id, name) VALUES
  (1, 'Москва'),
  (2, 'Воронеж'),
  (3, 'Владивосток'),
  (4, 'Казань'),
  (5, 'Сочи');

ALTER SEQUENCE city_id_seq RESTART WITH 6;

INSERT INTO team (id, name, city_id, prev_place) VALUES
  (1, 'Хищные бобры', 2, 3),
  (2, 'Динамо', 1, 1),
  (3, 'Рубин', 4, 13),
  (4, 'Спартак', 1, 2);


INSERT INTO player (id, name, surname, age, role_id, city_id, team_id) VALUES
  (1, 'Иван', 'Иванов', '22', 0, 1, 2),
  (2, 'Николай', 'Некрасов', '23', 1, 1, 2),
  (3, 'Михаил', 'Михайлов', '24', 2, 1, 2),
  (4, 'Василий', 'Васин', '25', 3, 1, 2),
  (5, 'Дмитрий', 'Дмитриев', '26', 0, 1, 2),
  (6, 'Иван', 'Попов', '22', 0, 1, 1),
  (7, 'Николай', 'Иванов', '23', 1, 1, 1),
  (8, 'Михаил', 'Иванов', '24', 3, 1, 1),
  (9, 'Василий', 'Иванов', '25', 3, 1, 1),
  (10, 'Дмитрий', 'Иванов', '26', 2, 1, 1),
  (11, 'Иван', 'Петров', '22', 1, 1, 3),
  (12, 'Николай', 'Петров', '23', 1, 1, 3),
  (13, 'Михаил', 'Петров', '24', 3, 1, 3),
  (14, 'Василий', 'Петров', '25', 3, 1, 3),
  (15, 'Дмитрий', 'Петров', '26', 2, 1, 3);


ALTER SEQUENCE player_id_seq RESTART WITH 16;

INSERT INTO coach (id, name, surname, team_id) VALUES
  (1, 'Петр', 'Петров', 1),
  (2, 'Семен', 'Семенов', 2),
  (3, 'Григорий', 'Гришин', 3),
  (4, 'Игорь', 'Гришин', NULL),
  (5, 'Николай', 'Гришин', NULL);

ALTER SEQUENCE coach_id_seq RESTART WITH 6;

INSERT INTO stadium (id, name, city_id, capacity) VALUES
  (1, 'Фишт', 5, 10000),
  (2, 'Казань Арена', 4, 5000),
  (3, 'Труд', 2, 1000);

ALTER SEQUENCE stadium_id_seq RESTART WITH 4;


ALTER SEQUENCE team_id_seq RESTART WITH 5;

INSERT INTO match (id, stadium_id, team_1_id, team_2_id, date, team1_res, team2_res) VALUES
  (1, 1, 1, 2, '2018-10-01', 1, 0),
  (2, 1, 1, 2, '2018-09-01', 1, 0),
  (3, 1, 1, 2, '2018-08-01', 1, 0),
  (4, 1, 1, 2, '2018-07-01', 1, 0),
  (5, 1, 1, 2, '2018-06-01', 1, 0);

ALTER SEQUENCE match_id_seq RESTART WITH 6;

INSERT INTO ticket (id, match_id, price) VALUES
  (1, 1, 12000.0),
  (2, 2, 12000.0),
  (3, 3, 12000.0),
  (4, 4, 12000.0),
  (5, 5, 12000.0);

ALTER SEQUENCE ticket_id_seq RESTART WITH 6;

-- INSERT INTO player_team (player_id, team_id) VALUES
--   (1,2),
--   (2,2),
--   (3,2),
--   (4,2),
--   (5,2),
--   (6,1),
--   (7,1),
--   (8,1),
--   (9,1),
--   (10,1),
--   (11,3),
--   (12,3),
--   (13,3),
--   (14,3),
--   (15,3)
--
-- ;

-- INSERT INTO coach_team (coach_id, team_id) VALUES (
--   1,1
-- );
