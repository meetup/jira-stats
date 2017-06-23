-- Select the first into progress transition.
--   If null use the created time
-- Select the last out of progress transitions
--   if null use the into code review time
--     if null use closed time.
SELECT
  i.key,
  i.epic,
  i.created,
  closed.closed_time,
  IFNULL(min(t_progress.time), i.created) in_progress,
  IFNULL(max(t_next.time), IFNULL(max(t_code_review.time), closed.closed_time)) out_progress
FROM `meetup-prod.team_eng_jira.issues` AS i
LEFT JOIN `meetup-prod.team_eng_jira.transitions` AS t_progress ON i.key = t_progress.key AND t_progress.`from` = 'Open' AND t_progress.`to` = 'In Progress'
LEFT JOIN `meetup-prod.team_eng_jira.transitions` AS t_next ON i.key = t_next.key AND t_next.`from` = 'In Progress'
LEFT JOIN `meetup-prod.team_eng_jira.transitions` AS t_code_review ON i.key = t_code_review.key AND t_code_review.`to` = 'Code Review'
LEFT JOIN `meetup-prod.team_eng_jira.issue_closed_times` AS closed ON i.key = closed.key
GROUP BY 1,2,3,4
