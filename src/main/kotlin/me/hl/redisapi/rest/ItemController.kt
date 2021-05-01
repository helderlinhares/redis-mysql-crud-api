package me.hl.redisapi.rest

import me.hl.redisapi.domain.CacheService
import me.hl.redisapi.domain.Item
import me.hl.redisapi.exception.ErrorCode
import me.hl.redisapi.domain.ItemService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@RestController
@RequestMapping("/items", produces = [MediaType.APPLICATION_JSON_VALUE])
internal class ItemController(private val itemService: ItemService, private val cacheService: CacheService){

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun saveItem(@Valid @RequestBody itemRequest: ItemRequest) = itemService.create(itemRequest.toDomain())

    @PutMapping("/{id}", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun updateItem(@Valid @RequestBody itemRequest: ItemRequest, @PathVariable id: Long) =
        itemService.update(id, itemRequest.toDomain())

    @GetMapping("/{id}")
    fun findItem(@PathVariable id: Long) = itemService.findById(id)

    @DeleteMapping("/{id}/cache")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteItemCache(@PathVariable id: Long) = cacheService.evictItemCachesByKey(id)

    @GetMapping
    fun findItems() = itemService.findAll()

    @DeleteMapping("/cache")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAllCache() = cacheService.evictAllCaches()

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteItem(@PathVariable id: Long) = itemService.delete(id)
}

data class ItemRequest(
    @field:NotBlank(message = ErrorCode.FIELD_VALUE_MUST_NOT_BE_BLANK)
    @field:Size(max = 255, message = ErrorCode.FIELD_VALUE_TOO_LONG)
    val name: String?
)

fun ItemRequest.toDomain() = Item(name=name!!, id=null)