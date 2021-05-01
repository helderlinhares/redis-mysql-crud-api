package me.hl.redisapi.domain

import me.hl.redisapi.exception.ErrorCode
import me.hl.redisapi.exception.ItemNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ItemService(private val itemRepository: ItemRepository) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @CacheEvict("findAll", allEntries = true)
    fun create(item: Item) = itemRepository.save(item).let {
        logger.debug("Processed create(${it.id}).")
        it
    }

    @Caching(evict = [
        CacheEvict("findAll", allEntries = true),
        CacheEvict(value = ["findById"], key = "#id", allEntries = true) ])
    fun update(id: Long, item: Item) = findById(id).let {
        logger.debug("Processed update($id, ${item}).")
        it.name = item.name
        itemRepository.save(it)
    }

    @Caching(evict = [
        CacheEvict("findAll", allEntries = true),
        CacheEvict(value = ["findById"], key = "#id", allEntries = true) ])
    fun delete(id: Long) = findById(id).let {
        logger.debug("Processed delete($id).")
        itemRepository.deleteById(it.id!!)
    }

    @Cacheable("findAll")
    fun findAll(): MutableList<Item> = itemRepository.findAll().let {
        logger.debug("Processed findAll().")
        it
    }

    @Cacheable(value = ["findById"], key = "#id")
    fun findById(id: Long) = itemRepository.findByIdOrNull(id).apply {
        logger.debug("Processed findById($id).")
    } ?: throw ItemNotFoundException(ErrorCode.ITEM_NOT_FOUND)
}