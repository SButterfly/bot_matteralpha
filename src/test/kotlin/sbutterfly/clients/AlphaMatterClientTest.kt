package sbutterfly.clients

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.core.io.ClassPathResource
import sbutterfly.clients.dto.Article
import java.time.Instant

class AlphaMatterClientTest {

    private val client = AlphaMatterClient()

    @Test
    fun `parseArticles should parse articles from mainpage html`() {
        // Given
        val html = ClassPathResource("mainpage.html").inputStream.bufferedReader().use { it.readText() }

        // When
        val articles = client.parseArticles(html)

        // Verify
        assertThat(articles)
            .usingRecursiveFieldByFieldElementComparator()
            .contains(
                Article(
                    id = "/how-to/how-anyone-can-add-a-matter-device-to-ikea-home",
                    title = "How anyone can add a Matter device to IKEA Home",
                    description = "Thanks to IKEA, adding a new smart home device is so easy, my mum could do it.",
                    author = "Christian Cawley",
                    publishedAt = Instant.ofEpochSecond(1753984800),
                    categories = listOf("matter-gateway"),
                    tags = listOf("how-to")
                ),
                Article(
                    id = "/news/home-assistant-extends-matter-capability-for-temperature-control-appliances",
                    title = "Home Assistant extends Matter capability for temperature control appliances",
                    description = "Home Assistant keeps expanding Matter capabilities in the August update.",
                    author = "Ward Zhou",
                    publishedAt = Instant.ofEpochSecond(1753977600),
                    categories = listOf("hub"),
                    tags = listOf("news")
                ),
                Article(
                    id = "/review/the-umbra-cono-smart-lamp-shows-the-importance-of-matter",
                    title = "The Umbra Cono smart lamp shows the importance of Matter",
                    description = "Thanks to Matter, these unsupported lights are still a decent purchase.",
                    author = "Bertel King",
                    publishedAt = Instant.ofEpochSecond(1753898400),
                    categories = listOf("extended-color-light"),
                    tags = listOf("explainer", "review")
                ),
                Article(
                    id = "/explainer/why-manufacturers-matter-over-wifi-not-thread",
                    title = "Why are manufacturers using Matter over Wi-Fi, and not Thread?",
                    description = "Despite Thread being a technically superior protocol for Matter devices, most manufacturers and consumers still prefer Wi-Fi due to confusing setup requirements, extra certification costs, and the need for additional hardware like Thread Border Routers.",
                    author = "James Bruce",
                    publishedAt = Instant.ofEpochSecond(1752516000),
                    categories = listOf("hub"),
                    tags = listOf("explainer")
                ),
                Article(
                    id = "/review/home-assistant-reviewed-is-this-the-matter-smart-home-solution-we-need",
                    title = "Home Assistant reviewed: Is this the Matter smart home solution we need?",
                    description = "It 's not for the masses yet, but if you 're ready to give Home Assistant a try, this is as easy as it gets.",
                    author = "Bertel King",
                    publishedAt = Instant.ofEpochSecond(1753120800),
                    categories = emptyList(),
                    tags = listOf("review")
                ),
                Article(
                    id = "/explainer/matter-and-cloud-services-the-role-of-cloud-platforms",
                    title = "Matter and cloud services: The role of cloud platforms",
                    description = "Matter doesn 't rely on the cloud, but many Matter-compatible devices and services do.",
                    author = "Bertel King",
                    publishedAt = Instant.ofEpochSecond(1752775200),
                    categories = listOf("on-off-light", "extended-color-light"),
                    tags = listOf("explainer")
                )
            )
    }
}