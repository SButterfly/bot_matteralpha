package sbutterfly.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions
import org.springframework.data.jdbc.core.dialect.JdbcH2Dialect
import org.springframework.data.relational.core.dialect.Dialect
import java.time.Instant

@Configuration
class DatabaseConfig {
    
    @Bean
    fun jdbcDialect(): Dialect {
        // Use H2 dialect as it's compatible with SQLite for basic operations
        return JdbcH2Dialect.INSTANCE
    }

    @Bean
    fun jdbcCustomConversions(): JdbcCustomConversions {
        return JdbcCustomConversions(listOf(
            IntegerToBooleanConverter(),
            InstantToStringConverter(),
            StringToInstantConverter(),
            InstantToLongConverter(),
            LongToInstantConverter()
        ))
    }

    @ReadingConverter
    class IntegerToBooleanConverter : Converter<Int, Boolean> {
        override fun convert(source: Int): Boolean {
            return source != 0
        }
    }

    @WritingConverter
    class InstantToStringConverter : Converter<Instant, String> {
        override fun convert(source: Instant): String {
            return source.toString()
        }
    }

    @ReadingConverter
    class StringToInstantConverter : Converter<String, Instant> {
        override fun convert(source: String): Instant {
            return Instant.parse(source)
        }
    }

    @WritingConverter
    class InstantToLongConverter : Converter<Instant, Long> {
        override fun convert(source: Instant): Long {
            return source.toEpochMilli() / 1000
        }
    }

    @ReadingConverter
    class LongToInstantConverter : Converter<Long, Instant> {
        override fun convert(source: Long): Instant {
            return Instant.ofEpochSecond(source)
        }
    }
}