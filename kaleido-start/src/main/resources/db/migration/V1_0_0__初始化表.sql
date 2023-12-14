CREATE TABLE IF NOT EXISTS `data_source` (
                               "id" integer NOT NULL PRIMARY KEY AUTOINCREMENT,
                               "name" varchar(50),
                               "type" varchar(20),
                               "icon" varchar(50),
                               "url" varchar(200),
                               "port" integer,
                               "user_name" varchar(50),
                               "password" varchar(100),
                               "db_name" varchar(50),
                               "extend" text,
                               "deleted" integer DEFAULT 0,
                               "create_time" timestamp DEFAULT CURRENT_TIMESTAMP,
                               "update_time" timestamp DEFAULT CURRENT_TIMESTAMP
);