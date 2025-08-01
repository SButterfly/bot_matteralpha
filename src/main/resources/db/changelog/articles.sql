--liquibase formatted sql

--changeset bot-matteralpha:create-articles-table
CREATE TABLE articles
(
    id               TEXT PRIMARY KEY NOT NULL,
    title            TEXT             NOT NULL,
    description      TEXT             NOT NULL,
    author           TEXT             NOT NULL,
    published_at     TIMESTAMP        NOT NULL,
    categories_json  TEXT             NOT NULL,
    tags_json        TEXT             NOT NULL,
    bot_published_at TIMESTAMP                 DEFAULT NULL,
    version          INTEGER          NOT NULL DEFAULT 0
);
create index articles_not_bot_published_idx on articles (id) where bot_published_at is null;
