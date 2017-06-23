SELECT key, MAX(time) AS closed_time
FROM `meetup-prod.team_eng_jira.transitions`
WHERE `to` = 'Closed'
GROUP BY key
