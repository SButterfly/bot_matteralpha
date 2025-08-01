package sbutterfly.repositories

import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sbutterfly.repositories.entities.ArticleEntity

@Repository
interface ArticleRepository : CrudRepository<ArticleEntity, String>, UpsertRepository<ArticleEntity> {

    @Query("SELECT * FROM articles WHERE bot_published_at is null")
    fun findBotUnpublished(): List<ArticleEntity>
}
