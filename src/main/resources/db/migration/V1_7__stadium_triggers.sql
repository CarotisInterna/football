--триггер на добавление и изменение стадиона - 4
CREATE OR REPLACE FUNCTION on_insert_or_update_stadium_1()
  RETURNS TRIGGER AS
$$
DECLARE
  r RECORD;
BEGIN
  SELECT s.*
  INTO r
  FROM stadium s
  WHERE s.id <> new.id
        AND s.name = new.name
        AND s.city_id = new.city_id;
  IF r IS NOT NULL
  THEN
    RAISE EXCEPTION 'Такой стадион в этом городе уже существует';
    RETURN NULL;
  ELSE
    RETURN new;
  END IF;
END;
$$
LANGUAGE PLPGSQL;

CREATE TRIGGER on_insert_or_update_stadium_1
BEFORE INSERT OR UPDATE OF name, city_id
  ON stadium
FOR EACH ROW EXECUTE PROCEDURE on_insert_or_update_stadium_1();



