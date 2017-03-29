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

~/projects/jira-stats[master] ➜  bq load --schema key:STRING,type:STRING,priority:STRING,version:STRING,epic:STRING,created:TIMESTAMP --source_format NEWLINE_DELIMITED_JSON jira_stats.released_issues released_issues.json_list
Upload complete.
Waiting on bqjob_r1f96d8d0ac72a2a8_0000015add9d8abc_1 ... (2s) Current status: DONE
~/projects/jira-stats[master] ➜  edit released_issues_transitions.json_list
~/projects/jira-stats[master] ➜  bq load --schema key:STRING,from:STRING,to:STRING,time:TIMESTAMP --source_format NEWLINE_DELIMITED_JSON jira_stats.released_issues_transitions released_issues_transitions.json_list
Upload complete.
Waiting on bqjob_r4886242e60ef1318_0000015add9ecfc9_1 ... (1s) Current status: DONE   