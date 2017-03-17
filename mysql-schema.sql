CREATE TABLE `story` (
  `key` varchar(12) NOT NULL,
  `created` timestamp NOT NULL,
  PRIMARY KEY (`key`)
);

CREATE TABLE `version` (
  `version` varchar(40) NOT NULL,
  `completed` timestamp NOT NULL,
  PRIMARY KEY (`version`)
);

CREATE TABLE `issue` (
  `key` varchar(12) NOT NULL,
  `type` varchar(40) NOT NULL,
  `priority` varchar(40) NOT NULL,
  `version` varchar(40) NOT NULL,
  `created` timestamp NOT NULL,
  PRIMARY KEY (`key`),
  KEY `type` (`type`),
  KEY `version` (`version`)
);

CREATE TABLE `transition` (
  `key` varchar(12) NOT NULL,
  `from` varchar(40) NOT NULL,
  `to` varchar(40) NOT NULL,
  `time` timestamp NOT NULL,
  UNIQUE KEY `transition` (`key`, `from`, `to`, `time`),
  KEY `from` (`from`),
  KEY `to` (`to`)
);
