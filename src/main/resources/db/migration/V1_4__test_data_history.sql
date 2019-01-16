INSERT INTO player_team_history (player_id, team_id, action_type, action_date) VALUES
  (1, 1, 0, now() - INTERVAL '10 DAYS'),
  (1, 1, 1, now() - INTERVAL '5 DAYS'),
  (1, 2, 0, now() - INTERVAL '5 DAYS');

INSERT INTO coach_team_history (coach_id, team_id, action_type, action_date) VALUES
  (1, 1, 0, now() - INTERVAL '10 DAYS'),
  (2, 1, 0, now() - INTERVAL '11 DAYS'),
  (2, 1, 1, now() - INTERVAL '10 DAYS'),
  (2, 2, 0, now() - INTERVAL '10 DAYS'),
  (3, 3, 0, now() - INTERVAL '9 DAYS');