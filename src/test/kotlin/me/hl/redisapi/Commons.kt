package me.hl.redisapi

import me.hl.redisapi.Commons.Companion.FIELD_NAME_SIZE_LIMIT
import me.hl.redisapi.Commons.Companion.ITEM_ALTERNATIVE_ID
import me.hl.redisapi.Commons.Companion.ITEM_DEFAULT_ID
import me.hl.redisapi.domain.Item
import me.hl.redisapi.exception.Error
import me.hl.redisapi.exception.ErrorCode
import me.hl.redisapi.exception.ErrorResponse
import me.hl.redisapi.rest.ItemRequest
import org.springframework.context.MessageSource
import java.util.Locale

fun buildItem() = Item(ITEM_DEFAULT_ID, "Item $ITEM_DEFAULT_ID")
fun buildModifiedItem() = Item(ITEM_DEFAULT_ID, "Item $ITEM_ALTERNATIVE_ID")

fun buildItemWithoutNameRequest() = ItemRequest(null)
fun buildItemWithBlankNameRequest() = ItemRequest( " ")
fun buildItemWithNameTooLongRequest() = ItemRequest("a".repeat(FIELD_NAME_SIZE_LIMIT + 1))
fun buildItemRequest() = ItemRequest("Item $ITEM_DEFAULT_ID")
fun buildAlternativeItemRequest() = ItemRequest("Item $ITEM_ALTERNATIVE_ID")

fun buildNotFoundResponse(messageSource: MessageSource) =
    buildErrorReponse(messageSource, ErrorCode.ITEM_NOT_FOUND, null)

fun buildNameNotBlankResponse(messageSource: MessageSource) =
    buildErrorReponse(messageSource, ErrorCode.FIELD_VALUE_MUST_NOT_BE_BLANK, arrayOf("name"))

fun buildNameTooLongResponse(messageSource: MessageSource) =
    buildErrorReponse(messageSource, ErrorCode.FIELD_VALUE_TOO_LONG, arrayOf("name"))

fun buildErrorReponse(messageSource: MessageSource, errorCode: String, params: Array<String>?) = ErrorResponse(listOf(
    Error(code = errorCode, message = messageSource.getMessage(
        errorCode, params, Locale.getDefault()))
))

class Commons {
    companion object {
        const val ITEM_DEFAULT_ID: Long = 1
        const val ITEM_ALTERNATIVE_ID: Long = 2
        const val FIELD_NAME_SIZE_LIMIT: Int = 255
    }
}