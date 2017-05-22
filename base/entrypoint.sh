#!/bin/bash -ex

# Pull down the latest yearly data from jira on issues
# and pull down all the epic details.
/opt/docker/bin/jira-stats

# Upload to BigQuery
bq load \
  --schema key:STRING,type:STRING,priority:STRING,version:STRING,epic:STRING,created:TIMESTAMP \
  --source_format NEWLINE_DELIMITED_JSON \
  --replace \
  --batch \
  meetup_looker.issues \
  issues.json.log

bq load \
  --schema key:STRING,from:STRING,to:STRING,time:TIMESTAMP \
  --source_format NEWLINE_DELIMITED_JSON \
  --replace \
  --batch \
  meetup_looker.transitions \
  transitions.json.log

bq load \
  --schema key:STRING,name:STRING,prefix:STRING \
  --source_format NEWLINE_DELIMITED_JSON \
  --replace \
  --batch \
  meetup_looker.epics \
  epics.json.log

cat issue_progress_range.sql | bq query \
  --destination_table scratch.issue_progress_range \
  --replace \
  --batch \
  -n 0 \
  --nouse_legacy_sql

cat progress_count.sql | bq query \
  --destination_table meetup_looker.issues_in_progress \
  --replace \
  --batch \
  -n 0 \
  --nouse_legacy_sql

# Create aggregated table from new data.
