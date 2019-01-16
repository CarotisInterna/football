--триггер на сохранение истории при обновлении и создании игрока - 1
CREATE OR REPLACE FUNCTION on_insert_or_update_player()
  RETURNS TRIGGER AS
$$
BEGIN
  IF tg_op = 'UPDATE'
  THEN
    IF old.team_id IS NULL AND new.team_id IS NOT NULL
    THEN
      INSERT INTO player_team_history (player_id, team_id, action_type, action_date) VALUES
        (new.id, new.team_id, 0, now());
    ELSEIF old.team_id IS NOT NULL AND new.team_id IS NOT NULL
      THEN
        INSERT INTO player_team_history (player_id, team_id, action_type, action_date) VALUES
          (old.id, old.team_id, 1, now());
        INSERT INTO player_team_history (player_id, team_id, action_type, action_date) VALUES
          (new.id, new.team_id, 0, now());
    ELSEIF old.team_id IS NOT NULL AND new.team_id IS NULL
      THEN
        INSERT INTO player_team_history (player_id, team_id, action_type, action_date) VALUES
          (new.id, old.team_id, 1, now());
    END IF;
  ELSEIF tg_op = 'INSERT'
    THEN
      IF new.team_id IS NOT NULL
      THEN
        INSERT INTO player_team_history (player_id, team_id, action_type, action_date) VALUES
          (new.id, new.team_id, 0, now());
      END IF;
  END IF;
  RETURN new;
END;
$$
LANGUAGE PLPGSQL;

CREATE TRIGGER on_insert_player
AFTER INSERT
  ON player
FOR EACH ROW EXECUTE PROCEDURE on_insert_or_update_player();

CREATE TRIGGER on_update_player
AFTER UPDATE OF team_id
  ON player
FOR EACH ROW EXECUTE PROCEDURE on_insert_or_update_player();

--при удалении игрока и изменении команды игрока брать первого свободного самого младшего - 2
CREATE OR REPLACE FUNCTION on_delete_player_from_team()
  RETURNS TRIGGER AS
$$
DECLARE
  r INT;
BEGIN
  IF old.team_id IS NULL
  THEN RETURN new;
  END IF;
  RAISE NOTICE 'old %', old;
  SELECT p.id
  INTO r
  FROM player p
  WHERE p.id <> old.id
        AND p.team_id IS NULL
        AND p.role_id = old.role_id
        AND p.age = (
    SELECT min(p2.age) AS m
    FROM player p2
    WHERE
      p2.id <> old.id
      AND
      p2.team_id IS NULL
      AND p2.role_id = old.role_id
  )
  LIMIT 1;
  RAISE NOTICE '%', r;
  IF r IS NOT NULL
  THEN
    UPDATE player
    SET team_id = old.team_id
    WHERE id = r;
    RAISE NOTICE '%blah blah', r;
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


CREATE TRIGGER on_delete_player_from_team
BEFORE DELETE OR UPDATE OF team_id
  ON player
FOR EACH ROW EXECUTE PROCEDURE on_delete_player_from_team();

CREATE OR REPLACE FUNCTION on_insert_or_update_player_1()
  RETURNS TRIGGER AS
$$
DECLARE
  r RECORD;
BEGIN
  SELECT s.*
  INTO r
  FROM player s
  WHERE s.id <> new.id
        AND s.name = new.name
        AND s.surname = new.surname
        AND s.age = new.age
        AND s.team_id = new.team_id
        AND s.role_id = new.role_id
        AND s.city_id = new.city_id;
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

CREATE TRIGGER on_insert_or_update_player_1
BEFORE INSERT OR UPDATE
  ON player
FOR EACH ROW EXECUTE PROCEDURE on_insert_or_update_player_1();


