package sbutterfly.repositories

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import sbutterfly.repositories.entities.ArticleEntity

@Repository
class ArticleRepositoryImpl(
    jdbcTemplate: JdbcTemplate
) : UpsertRepositoryImpl<ArticleEntity>(jdbcTemplate) {
    
    override fun buildUpsertSql(): String = """
        INSERT INTO articles (id, title, description, author, published_at, categories_json, tags_json, bot_published_at, version)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        ON CONFLICT(id) DO UPDATE SET
            title = excluded.title,
            description = excluded.description,
            author = excluded.author,
            published_at = excluded.published_at,
            categories_json = excluded.categories_json,
            tags_json = excluded.tags_json,
            bot_published_at = excluded.bot_published_at,
            version = excluded.version
        WHERE articles.version <= excluded.version
    """

    override fun buildUpsertIgnoreSql(): String = """
        INSERT INTO articles (id, title, description, author, published_at, categories_json, tags_json, bot_published_at, version)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        ON CONFLICT(id) DO NOTHING
    """.trimIndent()

    override fun extractValues(entity: ArticleEntity): Array<Any?> = arrayOf(
        entity.id,
        entity.title,
        entity.description,
        entity.author,
        entity.publishedAt.toString(),
        entity.categoriesJson,
        entity.tagsJson,
        entity.botPublishedAt?.toString(),
        entity.version,
    )
}
