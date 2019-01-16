--триггер на цену - 5
CREATE OR REPLACE FUNCTION on_insert_or_update_match()
  RETURNS TRIGGER AS
$$
DECLARE
  r_1 RECORD;
  r_2 RECORD;
  p_1 DOUBLE PRECISION;
  p_2 DOUBLE PRECISION;
BEGIN
  SELECT t.*
  INTO r_1
  FROM team t
  WHERE t.id = new.team_1_id;
  SELECT t.*
  INTO r_2
  FROM team t
  WHERE t.id = new.team_2_id;
  IF r_1.prev_place IN (1, 2, 3)
  THEN
    p_1 := (4 - r_1.prev_place) * 3000;
  ELSE
    p_1 := 1000;
  END IF;
  IF r_2.prev_place IN (1, 2, 3)
  THEN
    p_2 := (4 - r_2.prev_place) * 3000;
  ELSE
    p_2 := 1000;
  END IF;
  IF TG_OP = 'INSERT'
  THEN
    INSERT INTO ticket (match_id, price) VALUES
      (new.id, p_1 + p_2);
    RETURN new;
  ELSE
    UPDATE ticket
    SET price = p_1 + p_2
    WHERE match_id = new.id;
    RETURN new;
  END IF;
END;
$$
LANGUAGE PLPGSQL;

CREATE TRIGGER on_insert_match_price
AFTER INSERT
  ON match
FOR EACH ROW EXECUTE PROCEDURE on_insert_or_update_match();

CREATE TRIGGER on_update_match_price
BEFORE UPDATE OF team_1_id, team_2_id
  ON match
FOR EACH ROW EXECUTE PROCEDURE on_insert_or_update_match();

--триггер на неполный состав команд - 6
CREATE OR REPLACE FUNCTION on_insert_or_update_team_match()
  RETURNS TRIGGER AS
$$
DECLARE
  r_p_1_Z RECORD;
  r_p_1_N RECORD;
  r_p_1_V RECORD;
  r_p_1_P RECORD;
  r_p_2_Z RECORD;
  r_p_2_N RECORD;
  r_p_2_V RECORD;
  r_p_2_P RECORD;
BEGIN
  SELECT p.*
  INTO r_p_1_Z
  FROM player p
  WHERE p.role_id = 0 AND p.team_id = new.team_1_id;
  SELECT p.*
  INTO r_p_1_N
  FROM player p
  WHERE p.role_id = 1 AND p.team_id = new.team_1_id;
  SELECT p.*
  INTO r_p_1_V
  FROM player p
  WHERE p.role_id = 2 AND p.team_id = new.team_1_id;
  SELECT p.*
  INTO r_p_1_P
  FROM player p
  WHERE p.role_id = 3 AND p.team_id = new.team_1_id;
  SELECT p.*
  INTO r_p_2_Z
  FROM player p
  WHERE p.role_id = 0 AND p.team_id = new.team_2_id;
  SELECT p.*
  INTO r_p_2_N
  FROM player p
  WHERE p.role_id = 1 AND p.team_id = new.team_2_id;
  SELECT p.*
  INTO r_p_2_V
  FROM player p
  WHERE p.role_id = 2 AND p.team_id = new.team_2_id;
  SELECT p.*
  INTO r_p_2_P
  FROM player p
  WHERE p.role_id = 3 AND p.team_id = new.team_2_id;
  IF r_p_1_N IS NULL OR r_p_1_Z IS NULL OR r_p_1_V IS NULL OR r_p_1_P IS NULL
     OR r_p_2_N IS NULL OR r_p_2_Z IS NULL OR r_p_2_V IS NULL OR r_p_2_P IS NULL
  THEN
    RAISE EXCEPTION 'Команды не укомплектованы полностью';
    RETURN NULL;
  ELSE RETURN new;
  END IF;
END;
$$
LANGUAGE PLPGSQL;

CREATE TRIGGER on_insert_team_match
AFTER INSERT
  ON match
FOR EACH ROW EXECUTE PROCEDURE on_insert_or_update_team_match();

CREATE TRIGGER on_update_team_match
BEFORE UPDATE OF team_1_id, team_2_id
  ON match
FOR EACH ROW EXECUTE PROCEDURE on_insert_or_update_team_match();


--тригер на добавление команды, которая выиграла 5 матчей - 7
CREATE OR REPLACE FUNCTION on_insert_match()
  RETURNS TRIGGER AS
$$
DECLARE
  r_1 RECORD;
BEGIN
  WITH results AS (
    SELECT
      team_1_id AS team_id,
      1         AS won
    FROM match
    WHERE team1_res > team2_res
    UNION ALL
    SELECT
      team_2_id AS team_id,
      1         AS won
    FROM match
    WHERE team1_res < team2_res
  ), totals AS (
      SELECT
        team_id,
        SUM(won) AS num_won
      FROM results
      GROUP BY team_id
  )
  SELECT t.*
  INTO r_1
  FROM totals t
  WHERE (t.team_id = new.team_1_id
         AND t.num_won > 4)
        OR (t.team_id = new.team_2_id
            AND t.num_won > 4);
  IF r_1 IS NOT NULL
  THEN
    IF (SELECT s.capacity
        FROM stadium s
        WHERE new.stadium_id = s.id) < 5000
    THEN
      RAISE EXCEPTION 'Команда, которая выигрывала 5 раз, не может играть на таком маленьком стадионе';
      RETURN NULL;
    ELSE
      RETURN new;
    END IF;
  ELSE RETURN new;
  END IF;

END;
$$
LANGUAGE PLPGSQL;

CREATE TRIGGER on_insert_match
AFTER INSERT
  ON match
FOR EACH ROW EXECUTE PROCEDURE on_insert_match();

--тригер на вставку или обновление стадиона - 8
CREATE OR REPLACE FUNCTION on_insert_or_update_stadium()
  RETURNS TRIGGER AS
$$
DECLARE
  r_1 RECORD;
BEGIN
  SELECT m.*
  INTO r_1
  FROM match m
  WHERE m.stadium_id = new.stadium_id
        AND m.id <> new.id
        AND (m.date = new.date
             OR m.date = (new.date - INTEGER '1'));
  IF r_1 IS NOT NULL
  THEN
    RAISE EXCEPTION 'Этот стадион не может принять матч, так как в указанный день или за день до него он уже принимает матч';
    RETURN NULL;
  ELSE
    RETURN new;
  END IF;
END;
$$
LANGUAGE PLPGSQL;

CREATE TRIGGER on_insert_or_update_stadium
BEFORE INSERT OR UPDATE OF stadium_id, date
  ON match
FOR EACH ROW EXECUTE PROCEDURE on_insert_or_update_stadium();
