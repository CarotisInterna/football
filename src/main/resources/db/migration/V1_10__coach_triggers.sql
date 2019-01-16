--триггер на сохранение истории при вставке и редактировании тренера - 10
CREATE OR REPLACE FUNCTION on_insert_or_update_coach()
  RETURNS TRIGGER AS
$$
DECLARE
  r RECORD;
BEGIN
  RAISE NOTICE 'Удаление %', 2;
  IF tg_op = 'UPDATE'
  THEN
    IF old.team_id IS NULL AND new.team_id IS NOT NULL
    THEN
      INSERT INTO coach_team_history (coach_id, team_id, action_type, action_date) VALUES
        (new.id, new.team_id, 0, now());
    ELSEIF old.team_id IS NOT NULL AND new.team_id IS NOT NULL
      THEN
        INSERT INTO coach_team_history (coach_id, team_id, action_type, action_date) VALUES
          (old.id, old.team_id, 1, now());
        INSERT INTO coach_team_history (coach_id, team_id, action_type, action_date) VALUES
          (new.id, new.team_id, 0, now());
    ELSEIF old.team_id IS NOT NULL AND new.team_id IS NULL
      THEN
        INSERT INTO coach_team_history (coach_id, team_id, action_type, action_date) VALUES
          (new.id, old.team_id, 1, now());
    END IF;
  ELSEIF tg_op = 'INSERT'
    THEN
      IF new.team_id IS NOT NULL
      THEN
        INSERT INTO coach_team_history (coach_id, team_id, action_type, action_date) VALUES
          (new.id, new.team_id, 0, now());
      END IF;
  END IF;
  RETURN new;
END;
$$
LANGUAGE PLPGSQL;

CREATE TRIGGER on_update_coach
BEFORE UPDATE OF team_id
  ON coach
FOR EACH ROW EXECUTE PROCEDURE on_insert_or_update_coach();


CREATE TRIGGER on_insert_coach
AFTER INSERT
  ON coach
FOR EACH ROW EXECUTE PROCEDURE on_insert_or_update_coach();

-- --при удалении тренера и изменении команды тренера брать первого свободного, который раньше не тренировал эту команду - 11
CREATE OR REPLACE FUNCTION on_delete_coach_from_team()
  RETURNS TRIGGER AS
$$
DECLARE
  r   INT;
  rec RECORD;
BEGIN
  IF old.team_id IS NULL
  THEN RETURN new;
  END IF;
  RAISE NOTICE 'Берет первого %', 1;
  RAISE NOTICE 'какой изменяем %', old;
  RAISE NOTICE 'какой изменяем айди%', old.id;
  RAISE NOTICE 'какой изменяем команда%', old.team_id;
  SELECT c.id
  INTO r
  FROM coach c
    LEFT JOIN (
                SELECT
                  cth.coach_id,
                  array_agg(cth.team_id) array_teams
                FROM coach_team_history cth
                GROUP BY cth.coach_id
              ) AS t
      ON c.id = t.coach_id
  WHERE c.id <> old.id
        AND c.team_id IS NULL
        AND (old.team_id <> ALL (t.array_teams)
             OR t.array_teams IS NULL)
  LIMIT 1;
  RAISE NOTICE 'тот, что выбран %', r;
  IF r IS NOT NULL
  THEN
    UPDATE coach
    SET team_id = old.team_id
    WHERE id = r;
    RAISE NOTICE '%зашел', r;
  END IF;
  IF tg_op = 'DELETE'
  THEN
    RETURN old;
  END IF;
  IF tg_op = 'UPDATE'
  THEN
    RETURN new;
  END IF;
END;
$$
LANGUAGE PLPGSQL;

CREATE TRIGGER on_delete_coach_from_team
BEFORE DELETE OR UPDATE OF team_id
  ON coach
FOR EACH ROW EXECUTE PROCEDURE on_delete_coach_from_team();

SELECT *
FROM coach c
  INNER JOIN coach_team_history cth
    ON c.id = cth.coach_id
WHERE c.id <> 1
      AND c.team_id IS NULL
      AND cth.team_id <> 1
LIMIT 1;

SELECT *
FROM (
       SELECT
         a.coach_id,
         array_agg(a.team_id) vbvnmv
       FROM coach_team_history a
       GROUP BY a.coach_id) AS t
WHERE '4' <> ALL (t.vbvnmv);

SELECT *
FROM coach c
  LEFT JOIN (
              SELECT
                cth.coach_id,
                array_agg(cth.team_id) array_teams
              FROM coach_team_history cth
              GROUP BY cth.coach_id
            ) AS t
    ON c.id = t.coach_id
WHERE c.id <> 1
      AND c.team_id IS NULL
      AND ('5' <> ALL (t.array_teams) OR t.array_teams IS NULL)
LIMIT 1;

CREATE OR REPLACE FUNCTION on_insert_or_update_coach_1()
  RETURNS TRIGGER AS
$$
DECLARE
  r RECORD;
BEGIN
  SELECT s.*
  INTO r
  FROM coach s
  WHERE s.id <> new.id
        AND s.name = new.name
        AND s.surname = new.surname
        AND s.team_id = new.team_id;
  IF r IS NOT NULL
  THEN
    RAISE EXCEPTION 'Такой игрок уже существует';
    RETURN NULL;
  ELSE
    RETURN new;
  END IF;
END;
$$
LANGUAGE PLPGSQL;

CREATE TRIGGER on_insert_or_update_coach_1
BEFORE INSERT OR UPDATE
  ON coach
FOR EACH ROW EXECUTE PROCEDURE on_insert_or_update_coach_1();
