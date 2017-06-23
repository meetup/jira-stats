-- Minutes between in_progress and closed_time
--  If value is 0 return at least 1.
--    not having 0 values helps with means.
--    and we know that at least a minute was spent.
SELECT
 key,
 IF(cycle_time > 0, cycle_time, 1) cycle_time_minutes
FROM (
SELECT
  closed.key,
  TIMESTAMP_DIFF(closed.closed_time, progress.in_progress, MINUTE) AS cycle_time
FROM `meetup-prod.team_eng_jira.issue_closed_times` AS closed
JOIN `meetup-prod.team_eng_jira.issue_progress_range` AS progress ON closed.key = progress.key
) AS t
