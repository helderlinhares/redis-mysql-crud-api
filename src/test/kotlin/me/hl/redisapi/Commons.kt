package me.hl.redisapi

import me.hl.redisapi.exception.ErrorResponse
import me.hl.redisapi.rest.ItemRequest
import me.hl.redisapi.Commons.Companion.ITEM_ALTERNATIVE_ID
import me.hl.redisapi.Commons.Companion.ITEM_DEFAULT_ID
import me.hl.redisapi.domain.Item
import me.hl.redisapi.exception.Error
import me.hl.redisapi.exception.ErrorCode
import org.springframework.context.MessageSource
import java.util.Locale

fun buildItem() = Item(ITEM_DEFAULT_ID, "Item $ITEM_DEFAULT_ID")
fun buildItemRequest() = ItemRequest("Item $ITEM_DEFAULT_ID")
fun buildModifiedItem() = Item(ITEM_DEFAULT_ID, "Item $ITEM_ALTERNATIVE_ID")
fun buildAlternativeItemRequest() = ItemRequest("Item $ITEM_ALTERNATIVE_ID")
fun buildNotFoundResponse(messageSource:MessageSource) = ErrorResponse(listOf(
    Error(code = ErrorCode.ITEM_NOT_FOUND, message = messageSource.getMessage(
        ErrorCode.ITEM_NOT_FOUND, null, Locale.getDefault()))
))

class Commons {
    companion object {
        const val ITEM_DEFAULT_ID: Long = 1
        const val ITEM_ALTERNATIVE_ID: Long = 2
    }
}