#!/bin/bash -ex

# Pull down the latest yearly data from jira on issues
# and pull down all the epic details.
/opt/bin/jira-stats

# Upload to BigQuery
bq load \
  --schema key:STRING,type:STRING,priority:STRING,version:STRING,epic:STRING,created:TIMESTAMP \
  --source_format NEWLINE_DELIMITED_JSON \
  --replace \
  team_eng_jira.issues \
  issues.json.log

bq load \
  --schema key:STRING,from:STRING,to:STRING,time:TIMESTAMP \
  --source_format NEWLINE_DELIMITED_JSON \
  --replace \
  team_eng_jira.transitions \
  transitions.json.log

bq load \
  --schema key:STRING,name:STRING,prefix:STRING \
  --source_format NEWLINE_DELIMITED_JSON \
  --replace \
  team_eng_jira.epics \
  epics.json.log

bq load \
  --schema project:STRING,name:STRING,release_date:DATE \
  --source_format NEWLINE_DELIMITED_JSON \
  --replace \
  team_eng_jira.versions \
  versions.json.log

cat issue_closed_times.sql | bq query \
  --destination_table team_eng_jira.issue_closed_times \
  --replace \
  --batch \
  -n 0 \
  --nouse_legacy_sql

cat issue_progress_range.sql | bq query \
  --destination_table team_eng_jira.issue_progress_range \
  --replace \
  --batch \
  -n 0 \
  --nouse_legacy_sql

cat progress_count.sql | bq query \
  --destination_table team_eng_jira.issues_in_progress \
  --replace \
  --batch \
  -n 0 \
  --nouse_legacy_sql

cat issue_cycle_time.sql | bq query \
  --destination_table team_eng_jira.issue_cycle_time \
  --replace \
  --batch \
  -n 0 \
  --nouse_legacy_sql

# Create aggregated table from new data.
