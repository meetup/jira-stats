-- Minutes between issue.created and closed_time
--  If value is 0 return at least 1.
--    not having 0 values helps with means.
--    and we know that at least a minute was spent.
SELECT
 key,
 IF(lead_time > 0, lead_time, 1) lead_time_minutes
FROM (
SELECT
  closed.key,
  TIMESTAMP_DIFF(closed.closed_time, issue.created, MINUTE) AS lead_time
FROM `meetup-prod.team_eng_jira.issue_closed_times` AS closed
JOIN `meetup-prod.team_eng_jira.issues` AS issue ON closed.key = issue.key
) AS t
