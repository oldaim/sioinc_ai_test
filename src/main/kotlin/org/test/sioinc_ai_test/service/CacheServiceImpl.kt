package org.test.sioinc_ai_test.service

import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.test.sioinc_ai_test.entity.User
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID

@Service
class CacheServiceImpl(
    private val cacheManager: CacheManager
) : CacheService {

    companion object{

        private const val USER_CURRENT_THREAD_CACHE = "currentThreadCache"
        private const val THREAD_TIME_CACHE = "threadTimeCache"
        private const val TIMEOUT_MINUTES = 30
    }

    override fun getOrPutThreadUUID(user: User): UUID {

        val currentThreadCache: Cache? = cacheManager.getCache(USER_CURRENT_THREAD_CACHE)
        val threadTimeCache: Cache? = cacheManager.getCache(THREAD_TIME_CACHE)
        val currentTime: LocalDateTime = LocalDateTime.now()

        val userIdKey: String = user.id.toString()
        val currentThreadId: UUID? = currentThreadCache?.get(userIdKey)?.get() as? UUID


        if (currentThreadId == null){

            val uuid = createUUID()

            currentThreadCache?.put(userIdKey, uuid)

            threadTimeCache?.put(uuid, currentTime)

            return uuid

        }else{

            val currentThreadTime: LocalDateTime? = threadTimeCache?.get(currentThreadId)?.get() as? LocalDateTime

            if (ChronoUnit.MINUTES.between(currentThreadTime, currentTime) > TIMEOUT_MINUTES){

                val uuid = createUUID()

                currentThreadCache.put(userIdKey, uuid)

                threadTimeCache?.put(uuid, currentTime)

                return uuid

            }else{

                threadTimeCache?.put(currentThreadId, currentTime)

                return currentThreadId
            }

        }

    }

    override fun getUuidTime(currentThreadId: UUID): LocalDateTime {
        val threadTimeCache: Cache? = cacheManager.getCache(THREAD_TIME_CACHE)
        return threadTimeCache?.get(currentThreadId)?.get() as? LocalDateTime ?: LocalDateTime.now()
    }

    private fun createUUID(): UUID = UUID.randomUUID()
}