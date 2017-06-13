SELECT key, MAX(time) AS closed_time
FROM `meetup-prod.meetup_looker.transitions`
WHERE `to` = 'Closed'
GROUP BY key
