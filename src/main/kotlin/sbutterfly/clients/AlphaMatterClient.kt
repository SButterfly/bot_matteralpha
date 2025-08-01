package sbutterfly.clients

import org.jsoup.Jsoup
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.requiredBody
import sbutterfly.clients.dto.Article
import java.time.Instant

/**
 * A client for https://www.matteralpha.com/
 */
@Component
@RegisterReflectionForBinding(
    Article::class
)
class AlphaMatterClient() {
    private val url: String = "https://www.matteralpha.com/"
    private val client: RestClient = RestClient.builder()
        .build()

    /**
     * Fetches the main page data and returns a map of categories to articles.
     */
    fun fetchMainPage(): List<Article> {
        // Fetch the homepage HTML
        val responseSpec = client.get()
            .uri(url)
            .retrieve()
        val html = responseSpec.requiredBody<String>()

        // Parse html
        return parseArticles(html)
    }

    fun parseArticles(html: String): List<Article> {
        val doc = Jsoup.parse(html)
        val articles = mutableListOf<Article>()
        
        doc.select("article").forEach { articleElement ->
            val linkElement = articleElement.select("a[href]").first()
            val href = linkElement?.attr("href") ?: throw RuntimeException("No href found for article")
            
            val titleElement = articleElement.select("h2 a").first()
            val title = titleElement?.text()?.trim() ?: throw RuntimeException("No title found for article")

            val descElement = articleElement.select("a")[2]
            val description = descElement.text()
            
            val timeElement = articleElement.select("time[datetime]").first()
            val datetimeStr = timeElement?.attr("datetime") ?: throw RuntimeException("No datetime found for '$title'")
            val publishedAt = Instant.ofEpochSecond(datetimeStr.toLong())
            
            val tagElements = articleElement.select("a[href^='/tag/']")
            val tags = tagElements.map { it.attr("href").removePrefix("/tag/") }
            
            val categoryElements = articleElement.select("a[href^='/category/']")
            val categories = categoryElements.map { it.attr("href").removePrefix("/category/") }

            val authorElement = articleElement.select("span")[1]
            val author = authorElement.text()

            articles.add(Article(
                id = href,
                title = title,
                description = description,
                author = author,
                publishedAt = publishedAt,
                categories = categories,
                tags = tags
            ))
        }
        
        return articles
    }

    companion object {

        fun url(articleId: String): String {
            return "https://www.matteralpha.com$articleId"
        }

        fun instantViewUrl(articleId: String, instantViewId: String): String {
            val articleUrl = url(articleId)

            return "https://t.me/iv?url=$articleUrl&rhash=$instantViewId"
        }
    }
}