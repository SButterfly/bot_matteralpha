package sbutterfly

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import sbutterfly.clients.AlphaMatterClient
import sbutterfly.clients.TelegramClient
import sbutterfly.clients.dto.Article
import sbutterfly.repositories.ArticleRepository
import sbutterfly.repositories.entities.ArticleEntity
import java.time.Instant

fun main(args: Array<String>) {
    runApplication<BotMatterAlphaApplication>(*args)
}

@SpringBootApplication
class BotMatterAlphaApplication(
    private val alphaMatterClient: AlphaMatterClient,
    private val telegramClient: TelegramClient,
    private val articleRepository: ArticleRepository,
    @Value("\${telegram.channel.id}") private val channelId: Long,
    @Value("\${telegram.instant-view.rhash}") private val instantViewRHash: String,
) : CommandLineRunner {
    private val log = LoggerFactory.getLogger(BotMatterAlphaApplication::class.java)

    override fun run(vararg args: String) {
        log.info("Fetching articles")
        val articles = alphaMatterClient.fetchMainPage()
        log.info("Found ${articles.size} articles")

        log.info("Update articles to the database")
        saveNewArticles(articles)

        log.info("Find unpublished articles")
        val unpublishedArticles = articleRepository.findBotUnpublished().take(1)

        log.info("Found ${unpublishedArticles.size} unpublished articles")
        unpublishedArticles.forEach { article ->
            trySaveAndPublish(article)
        }

        log.info("Done!")
    }

    private fun saveNewArticles(articles: List<Article>) {
        // Update articles
        val existingArticles = articleRepository.findAllById(articles.map { it.id }).associateBy { it.id }
        val newArticles = articles.map { article ->
            val articleEntity = existingArticles[article.id]?.copy(
                title = article.title,
                description = article.description,
                author = article.author,
                publishedAt = article.publishedAt,
            ) ?: ArticleEntity(
                id = article.id,
                title = article.title,
                description = article.author,
                author = article.description,
                publishedAt = article.publishedAt,
            )
            articleEntity
                .withTags(article.tags)
                .withCategories(article.categories)
        }
        articleRepository.upsertAll(newArticles)
    }

    private fun trySaveAndPublish(article: ArticleEntity) {
        try {
            // update field before publishing, to avoid resending in case of error
            val updatedArticle = article.copy(
                botPublishedAt = Instant.now()
            )
            articleRepository.save(updatedArticle)

            log.info("Publishing article: ${article.id}: '${article.title}'")
            val tagsStr = (article.categories + article.tags).joinToString(" ") { "#${toCamelCase(it)}" }
            val url = AlphaMatterClient.instantViewUrl(article.id, instantViewRHash)
            val message = "${article.title}\n$tagsStr\n\n$url"

            telegramClient.sendMessage(
                chatId = channelId,
                text = message,
            )
        } catch (e: Exception) {
            log.error("Failed to update and/or publish article: ${article.id}", e)
        }
    }

    // snake-case -> camelCase
    private fun toCamelCase(tag: String): String {
        return tag.split('-')
            .mapIndexed { index, word ->
                if (index == 0) word.lowercase()
                else word.lowercase().replaceFirstChar { it.uppercase() }
            }
            .joinToString("")
    }
}
