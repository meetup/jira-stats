SELECT
  i.key,
  i.epic,
  min(t_progress.time) in_progress,
  max(t_next.time) out_progress
FROM `meetup-prod.meetup_looker.issues` AS i
JOIN `meetup-prod.meetup_looker.transitions` AS t_progress ON i.key = t_progress.key AND t_progress.`from` = 'Open' AND t_progress.`to` = 'In Progress'
JOIN `meetup-prod.meetup_looker.transitions` AS t_next ON i.key = t_next.key AND t_next.`from` = 'In Progress'
GROUP BY 1,2
