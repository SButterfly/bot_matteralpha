package sbutterfly.repositories

import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface UpsertRepository<T> {
    @Modifying
    fun upsert(entity: T): Int

    @Modifying
    fun upsertAll(entities: Iterable<T>): IntArray

    @Modifying
    fun upsertAllOnConflictIgnore(entities: Iterable<T>): IntArray
}
