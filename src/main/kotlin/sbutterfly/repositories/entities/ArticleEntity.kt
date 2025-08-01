package sbutterfly.repositories.entities

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("articles")
data class ArticleEntity(
    @Id
    val id: String,
    val title: String,
    val description: String,
    val author: String,
    val publishedAt: Instant,
    // These are the only fields Spring Data JDBC cares about for persistence!
    val categoriesJson: String = "[]",
    val tagsJson: String = "[]",
    val botPublishedAt: Instant? = null,
    @Version
    val version: Long = 1L
) {
    val categories: List<String>
        get() = objectMapper.readValue(categoriesJson)
    val tags: List<String>
        get() = objectMapper.readValue(tagsJson)

    // Utility copy methods for updating categories and tags
    fun withCategories(newCategories: List<String>) = this.copy(categoriesJson = objectMapper.writeValueAsString(newCategories))
    fun withTags(newTags: List<String>) = this.copy(tagsJson = objectMapper.writeValueAsString(newTags))

    companion object {
        private val objectMapper = jacksonObjectMapper()
    }
}
