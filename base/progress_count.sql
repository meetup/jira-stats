WITH
splitted AS (
  SELECT
    *
  FROM
    UNNEST( SPLIT(RPAD('',
          1 + DATE_DIFF(CURRENT_DATE(), DATE("2017-01-01"), DAY),
          '.'),''))),
  with_row_numbers AS (
  SELECT
    ROW_NUMBER() OVER() AS pos,
    *
  FROM
    splitted),
  calendar_day AS (
  SELECT
    DATE_ADD(DATE("2017-01-01"), INTERVAL (pos - 1) DAY) AS day
  FROM
    with_row_numbers)

SELECT key, epic, day
FROM calendar_day
JOIN `meetup-prod.scratch.issue_progress_range` AS i
  ON CAST(i.in_progress AS DATE) <= day
  AND CAST(i.out_progress AS DATE) >= day
ORDER BY 3
