#!/bin/bash -ex

# Pull down the latest yearly data from jira on issues
# and pull down all the epic details.
/opt/docker/bin/jira-stats

# Upload to BigQuery
bq load \
  --schema key:STRING,type:STRING,priority:STRING,version:STRING,epic:STRING,created:TIMESTAMP \
  --source_format NEWLINE_DELIMITED_JSON \
  jira_stats.issues \
  issues.json.log

bq load \
  --schema key:STRING,from:STRING,to:STRING,time:TIMESTAMP \
  --source_format NEWLINE_DELIMITED_JSON \
  jira_stats.transitions transitions.json_list

bq query \
  --destination_table

# Create aggregated table from new data.
