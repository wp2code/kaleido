CREATE TABLE IF NOT EXISTS main.`data_source` (
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
                               "creator_id" varchar(20),
                               "deleted" integer DEFAULT 0,
                               "create_time" timestamp DEFAULT CURRENT_TIMESTAMP,
                               "update_time" timestamp DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS main.`code_generation_template` (
                                "id" integer NOT NULL,
                                "template_name" varchar NOT NULL,
                                "language" varchar NOT NULL,
                                "basic_config" TEXT,
                                "is_internal" integer NOT NULL DEFAULT 0,
                                "is_default" integer NOT NULL DEFAULT 0,
                                "creator_id" varchar,
                                "deleted" integer DEFAULT 0,
                                "create_time" timestamp DEFAULT CURRENT_TIMESTAMP,
                                "update_time" timestamp DEFAULT CURRENT_TIMESTAMP,
                                PRIMARY KEY ("id")
);


CREATE TABLE IF NOT EXISTS main.`code_generation_template_config` (
                                "id" integer NOT NULL,
                                "template_id" integer NOT NULL,
                                "name" varchar NOT NULL,
                                "alias" varchar,
                                "template_content" TEXT,
                                "template_params" TEXT,
                                "hide_status" integer NOT NULL DEFAULT 0,
                                "creator_id" varchar,
                                "deleted" integer DEFAULT 0,
                                "create_time" timestamp DEFAULT CURRENT_TIMESTAMP,
                                "update_time" timestamp DEFAULT CURRENT_TIMESTAMP,
                                PRIMARY KEY ("id")
);