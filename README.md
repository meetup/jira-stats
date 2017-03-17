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
