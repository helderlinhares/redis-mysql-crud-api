package me.hl.redisapi.domain

import org.slf4j.LoggerFactory
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Caching
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class CacheService(private val cacheManager: CacheManager) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Caching(evict = [
        CacheEvict("findAll", allEntries = true),
        CacheEvict(value = ["findById"], key = "#cacheKey", allEntries = true) ])
    fun evictItemCachesByKey(cacheKey: Long){
        logger.debug("Processed evictItemCachesByKey($cacheKey).")
    }

    fun evictAllCaches() = cacheManager.cacheNames.stream().forEach {
            cacheName -> cacheManager.getCache(cacheName)?.clear().also{
                logger.debug("Processed evictAllCaches() by CacheName: $cacheName.")
            }
    }

    @Scheduled(cron = "0 */5 * * * *")
    fun evictAllCachesAtIntervals() {
        evictAllCaches()
        logger.debug("Processed evictAllCachesAtIntervals() by Schedule.")
    }
}