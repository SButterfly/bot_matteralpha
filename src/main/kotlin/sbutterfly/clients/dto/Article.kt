package sbutterfly.clients.dto

import java.time.Instant

/**
 * Represents an article from the website.
 *
 * @property id The unique identifier of the article.
 * @property title The title of the article.
 * @property description The description of the article.
 * @property publishedAt The date and time when the article was published.
 * @property categories The categories of the article.
 * @property tags The tags associated with the article.
 */
data class Article(
    val id: String,
    val title: String,
    val description: String,
    val author: String,
    val publishedAt: Instant,
    val categories: List<String>,
    val tags: List<String>,
)
