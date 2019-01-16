CREATE TABLE action_type (
  id   INT NOT NULL PRIMARY KEY,
  name TEXT
);

ALTER TABLE player_team_history
  ADD FOREIGN KEY (action_type) REFERENCES action_type (id);

ALTER TABLE coach_team_history
  ADD FOREIGN KEY (action_type) REFERENCES action_type(id);

INSERT INTO action_type (id, name) VALUES
  (0, 'Пришел'),
  (1, 'Ушел');