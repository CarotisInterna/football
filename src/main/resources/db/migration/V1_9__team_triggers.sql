-- --триггер на изменение города у команды (не может переехать в город, шде нет стадиона)
CREATE OR REPLACE FUNCTION on_update_team()
  RETURNS TRIGGER AS
$$
DECLARE
  r RECORD;
BEGIN
  SELECT s.*
  INTO r
  FROM stadium s
    WHERE s.city_id = new.city_id;
  IF r IS NULL
  THEN
    RAISE EXCEPTION 'Команда не может переехать в этот город, так как там нет стадиона';
    RETURN NULL;
  ELSE
    RETURN new;
  END IF;
END;
$$
LANGUAGE PLPGSQL;

CREATE TRIGGER on_update_team
BEFORE UPDATE OF city_id
  ON team
FOR EACH ROW EXECUTE PROCEDURE on_update_team();
