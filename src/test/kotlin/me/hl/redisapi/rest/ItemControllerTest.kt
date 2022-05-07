package me.hl.redisapi.rest

import com.google.gson.Gson
import me.hl.redisapi.Commons.Companion.ITEM_DEFAULT_ID
import me.hl.redisapi.buildAlternativeItemRequest
import me.hl.redisapi.buildItem
import me.hl.redisapi.buildItemRequest
import me.hl.redisapi.buildItemWithBlankNameRequest
import me.hl.redisapi.buildItemWithNameTooLongRequest
import me.hl.redisapi.buildItemWithoutNameRequest
import me.hl.redisapi.buildModifiedItem
import me.hl.redisapi.buildNameNotBlankResponse
import me.hl.redisapi.buildNameTooLongResponse
import me.hl.redisapi.buildNotFoundResponse
import me.hl.redisapi.domain.CacheService
import me.hl.redisapi.domain.Item
import me.hl.redisapi.domain.ItemService
import me.hl.redisapi.exception.ErrorCode
import me.hl.redisapi.exception.ItemNotFoundException
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
@Import(HttpEncodingAutoConfiguration::class)
class ItemControllerTest {

    @Autowired
    private lateinit var messageSource: MessageSource

    @MockBean
    private lateinit var itemService: ItemService

    @MockBean
    private lateinit var cacheService: CacheService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `Should return Item When Item exists`() {
        val item = buildItem()
        BDDMockito.given(itemService.findById(ITEM_DEFAULT_ID)).willReturn(item)

        mockMvc.get("/items/$ITEM_DEFAULT_ID")
            .andDo { print() }
            .andExpect { status { isOk() } }
            .andExpect {
                content {
                    json(Gson().toJson(item).trimIndent())
                }
            }
    }

    @Test
    fun `Should return Item list When at least one Item exists`() {
        val item = buildItem()
        BDDMockito.given(itemService.findAll()).willReturn(listOf(item).toMutableList())

        mockMvc.get("/items")
            .andDo { print() }
            .andExpect { status { isOk() } }
            .andExpect {
                content {
                    json(Gson().toJson(listOf(item)).trimIndent())
                }
            }
    }

    @Test
    fun `Should return empty list When no Item exists`() {
        BDDMockito.given(itemService.findAll()).willReturn(emptyList<Item>().toMutableList())

        mockMvc.get("/items")
            .andDo { print() }
            .andExpect { status { isOk() } }
            .andExpect {
                content {
                    json(Gson().toJson(emptyList<Item>()).trimIndent())
                }
            }
    }

    @Test
    fun `Should return NotFound When search for an Item that does not exists`() {
        BDDMockito.given(itemService.findById(ITEM_DEFAULT_ID))
            .willThrow(ItemNotFoundException(ErrorCode.ITEM_NOT_FOUND))

        mockMvc.get("/items/$ITEM_DEFAULT_ID")
            .andDo { print() }
            .andExpect { status { isNotFound() } }
            .andExpect {
                content {
                    json(Gson().toJson(buildNotFoundResponse(messageSource)).trimIndent())
                }
            }
    }

    @Test
    fun `Should create Item`() {
        val item = buildItem()
        val itemRequest = buildItemRequest().toDomain()
        BDDMockito.given(itemService.create(itemRequest)).willReturn(item)

        mockMvc.post("/items") {
            contentType = MediaType.APPLICATION_JSON
            content = Gson().toJson(itemRequest).trimIndent()
        }
            .andDo { print() }
            .andExpect { status { isCreated() } }
            .andExpect {
                content {
                    json(Gson().toJson(item).trimIndent())
                }
            }
    }

    @Test
    fun `Should not create Item with null name`(){
        val itemRequest = buildItemWithoutNameRequest()

        mockMvc.post("/items"){
            contentType = MediaType.APPLICATION_JSON
            content = Gson().toJson(itemRequest).trimIndent()
        }
            .andDo { print() }
            .andExpect { status { isBadRequest() } }
            .andExpect {
                content {
                    json(Gson().toJson(buildNameNotBlankResponse(messageSource)).trimIndent())
                }
            }
    }

    @Test
    fun `Should not create Item with blank name`(){
        val itemRequest = buildItemWithBlankNameRequest()

        mockMvc.post("/items"){
            contentType = MediaType.APPLICATION_JSON
            content = Gson().toJson(itemRequest).trimIndent()
        }
            .andDo { print() }
            .andExpect { status { isBadRequest() } }
            .andExpect {
                content {
                    json(Gson().toJson(buildNameNotBlankResponse(messageSource)).trimIndent())
                }
            }
    }

    @Test
    fun `Should not create Item with name too long`(){
        val itemRequest = buildItemWithNameTooLongRequest()

        mockMvc.post("/items"){
            contentType = MediaType.APPLICATION_JSON
            content = Gson().toJson(itemRequest).trimIndent()
        }
            .andDo { print() }
            .andExpect { status { isBadRequest() } }
            .andExpect {
                content {
                    json(Gson().toJson(buildNameTooLongResponse(messageSource)).trimIndent())
                }
            }
    }

    @Test
    fun `Should update Item When Item exists`(){
        val item = buildModifiedItem()
        val itemRequest = buildAlternativeItemRequest().toDomain()
        BDDMockito.given(itemService.update(ITEM_DEFAULT_ID, itemRequest)).willReturn(item)

        mockMvc.put("/items/$ITEM_DEFAULT_ID") {
            contentType = MediaType.APPLICATION_JSON
            content = Gson().toJson(itemRequest).trimIndent()
        }
            .andDo { print() }
            .andExpect { status { isOk() } }
            .andExpect {
                content {
                    json(Gson().toJson(item).trimIndent())
                }
            }
    }

    @Test
    fun `Should return NotFound When try to update an Item that does not exists`(){
        val itemRequest = buildAlternativeItemRequest()
        BDDMockito.given(itemService.update(ITEM_DEFAULT_ID, itemRequest.toDomain())).willThrow(ItemNotFoundException(ErrorCode.ITEM_NOT_FOUND))

        mockMvc.put("/items/$ITEM_DEFAULT_ID") {
            contentType = MediaType.APPLICATION_JSON
            content = Gson().toJson(itemRequest).trimIndent()
        }
            .andDo { print() }
            .andExpect { status { isNotFound() } }
            .andExpect {
                content {
                    json(Gson().toJson(buildNotFoundResponse(messageSource)).trimIndent())
                }
            }
    }

    @Test
    fun `Should delete Item When Item exists`(){
        BDDMockito.`when`(itemService.delete(ITEM_DEFAULT_ID)).then { }

        mockMvc.delete("/items/$ITEM_DEFAULT_ID")
            .andDo { print() }
            .andExpect { status { isNoContent() } }
    }

    @Test
    fun `Should return NotFound When try to delete an Item that does not exists`(){
        BDDMockito.given(itemService.delete(ITEM_DEFAULT_ID)).willThrow(ItemNotFoundException(ErrorCode.ITEM_NOT_FOUND))

        mockMvc.delete("/items/$ITEM_DEFAULT_ID")
            .andDo { print() }
            .andExpect { status { isNotFound() } }
            .andExpect {
                content {
                    json(Gson().toJson(buildNotFoundResponse(messageSource)).trimIndent())
                }
            }
    }

    @Test
    fun `Should delete Item Cache`(){
        BDDMockito.`when`(cacheService.evictItemCachesByKey(ITEM_DEFAULT_ID)).then { }

        mockMvc.delete("/items/$ITEM_DEFAULT_ID/cache")
            .andDo { print() }
            .andExpect { status { isNoContent() } }
    }

    @Test
    fun `Should delete All Cache`(){
        BDDMockito.`when`(cacheService.evictAllCaches()).then { }

        mockMvc.delete("/items/cache")
            .andDo { print() }
            .andExpect { status { isNoContent() } }
    }

}