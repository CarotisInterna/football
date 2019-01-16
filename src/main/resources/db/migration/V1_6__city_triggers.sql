--триггер на добавление и изменение города - 3
CREATE OR REPLACE FUNCTION on_insert_or_update_city()
  RETURNS TRIGGER AS
$$
DECLARE
  r RECORD;
BEGIN
  SELECT c.*
  INTO r
  FROM city c
  WHERE c.id <> new.id
        AND c.name = new.name;
  IF r IS NOT NULL
  THEN
    RAISE EXCEPTION 'Такой город уже существует';
    RETURN NULL;
  ELSE
    RETURN new;
  END IF;
END;
$$
LANGUAGE PLPGSQL;

CREATE TRIGGER on_insert_or_update_city
BEFORE INSERT OR UPDATE OF name
  ON city
FOR EACH ROW EXECUTE PROCEDURE on_insert_or_update_city();
