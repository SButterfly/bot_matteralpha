package sbutterfly.repositories

import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
abstract class UpsertRepositoryImpl<T : Any>(
    protected val jdbcTemplate: JdbcTemplate,
) : UpsertRepository<T> {
    
    override fun upsert(entity: T): Int {
        val result = upsertAll(listOf(entity))
        return result[0]
    }

    override fun upsertAll(entities: Iterable<T>): IntArray {
        val entityList = entities.toList()

        val sql = buildUpsertSql()
        val batchArgs = entityList.map { entity -> extractValues(entity) }

        val result = jdbcTemplate.batchUpdate(sql, batchArgs)

        result.forEachIndexed { index, count ->
            val entity = entityList[index]
            if (count == 0) {
                throw OptimisticLockingFailureException("Entity was modified by another transaction. Entity: $entity")
            }
        }
        return result
    }

    override fun upsertAllOnConflictIgnore(entities: Iterable<T>): IntArray {
        val entityList = entities.toList()

        val sql = buildUpsertIgnoreSql()
        val batchArgs = entityList.map { entity -> extractValues(entity) }

        return jdbcTemplate.batchUpdate(sql, batchArgs)
    }
    
    protected abstract fun buildUpsertSql(): String
    protected abstract fun buildUpsertIgnoreSql(): String
    protected abstract fun extractValues(entity: T): Array<Any?>
}
