CREATE TABLE IF NOT EXISTS main.`data_source`
(
    "id"          integer NOT NULL PRIMARY KEY AUTOINCREMENT,
    "name"        varchar(50),
    "type"        varchar(20),
    "icon"        varchar(50),
    "url"         varchar(200),
    "port"        integer(2),
    "user_name"   varchar(50),
    "password"    varchar(100),
    "db_name"     varchar(50),
    "extend"      text,
    "creator_id"  varchar(20),
    "deleted"     integer(2)   DEFAULT 0,
    "create_time" timestamp DEFAULT CURRENT_TIMESTAMP,
    "update_time" timestamp DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS main.`code_generation_template`
(
    "id"            integer(8) NOT NULL,
    "template_name" varchar(100) NOT NULL,
    "language"      varchar(20) NOT NULL,
    "source_type"   integer(2) DEFAULT 0,
    "source"        varchar(20) DEFAULT NULL,
    "basic_config"  TEXT,
    "is_internal"   integer(2) NOT NULL DEFAULT 0,
    "is_default"    integer(2) NOT NULL DEFAULT 0,
    "creator_id"    varchar(20),
    "deleted"       integer(2)          DEFAULT 0,
    "create_time"   timestamp        DEFAULT CURRENT_TIMESTAMP,
    "update_time"   timestamp        DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY ("id")
);
CREATE TABLE IF NOT EXISTS main.`code_generation_template_config`
(
    "id"               integer(8) NOT NULL,
    "template_id"      integer(8) NOT NULL,
    "name"             varchar(20) NOT NULL,
    "alias"            varchar(50),
    "code_path"            varchar(5000),
    "template_content" TEXT,
    "template_params"  TEXT,
    "hide_status"      integer NOT NULL DEFAULT 0,
    "creator_id"       varchar(20),
    "deleted"          integer          DEFAULT 0,
    "create_time"      timestamp        DEFAULT CURRENT_TIMESTAMP,
    "update_time"      timestamp        DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY ("id")
);

CREATE UNIQUE INDEX "UK0_code_generation_template"
    ON "code_generation_template" (
                                   "template_name"
        );

CREATE UNIQUE INDEX "UK0_code_generation_template_config"
    ON "code_generation_template_config" (
                                          "template_id" ASC,
                                          "name" ASC,
                                          "hide_status" ASC,
                                          "deleted" ASC
        );