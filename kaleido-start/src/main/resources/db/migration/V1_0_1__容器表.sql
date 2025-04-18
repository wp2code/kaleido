CREATE TABLE "container_config" (
                                    "id" integer(8) NOT NULL,
                                    "name" varchar(255) NOT NULL,
                                    "path" TEXT NOT NULL,
                                    "type" integer NOT NULL,
                                    "config" TEXT,
                                    "params" TEXT,
                                    "status" integer NOT NULL,
                                    "remark" TEXT,
                                    "deleted" integer NOT NULL,
                                    "create_time" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    "update_time" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    PRIMARY KEY ("id")
);

CREATE INDEX "index0_container_config"
    ON "container_config" (
                           "name" ASC
        );