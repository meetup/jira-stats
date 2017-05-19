# Jira Stats

A few simple tools to pull and print jira histories
in wanted formats.

## Running

Different apps are available:

* PrintReleasedIssuesApp
* PrintStoriesApp

```
$ sbt --error "runMain com.meetup.jirastats.PrintStoriesApp" | tee stories.sql
```

or

```
$ sbt --error "runMain com.meetup.jirastats.PrintReleasedIssuesApp" | tee released_issues.sql

```


## Importing

➜  bq load --schema key:STRING,type:STRING,priority:STRING,version:STRING,epic:STRING,created:TIMESTAMP --source_format NEWLINE_DELIMITED_JSON jira_stats.issues issues.json.log
➜  bq load --schema key:STRING,from:STRING,to:STRING,time:TIMESTAMP --source_format NEWLINE_DELIMITED_JSON jira_stats.transitions transitions.json_list

bq load --schema launch_id:INTEGER,rel:INTEGER,launcher:STRING,targets:STRING,actions:STRING,processes:STRING,launch_date:DATE,launch_started:TIMESTAMP,launch_live:TIMESTAMP,launch_completed:TIMESTAMP,status:INTEGER --replace --field_delimiter tab --source_format CSV --null_marker NULL --skip_leading_rows 1 jira_stats.launch_log ne_launch_log.csv
Upload complete.
Waiting on bqjob_r4886242e60ef1318_0000015add9ecfc9_1 ... (1s) Current status: DONE   
