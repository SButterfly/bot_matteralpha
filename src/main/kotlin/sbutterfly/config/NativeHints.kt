package sbutterfly.config

import liquibase.changelog.ChangeLogHistoryServiceFactory
import liquibase.changelog.FastCheckService
import liquibase.changelog.StandardChangeLogHistoryService
import liquibase.changelog.visitor.ValidatingVisitorGeneratorFactory
import liquibase.database.LiquibaseTableNamesFactory
import liquibase.parser.SqlParserFactory
import liquibase.report.ShowSummaryGeneratorFactory
import liquibase.ui.LoggerUIService
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding
import org.springframework.context.annotation.Configuration

/**
 * Collection of native hints for the application.
 */
@Configuration
@RegisterReflectionForBinding(
    // Liquibase https://github.com/oracle/graalvm-reachability-metadata/issues/431
    LoggerUIService::class,
    ChangeLogHistoryServiceFactory::class,
    FastCheckService::class,
    LiquibaseTableNamesFactory::class,
    StandardChangeLogHistoryService::class,
    ValidatingVisitorGeneratorFactory::class,
    SqlParserFactory::class,
    ShowSummaryGeneratorFactory::class,
)
class NativeHints