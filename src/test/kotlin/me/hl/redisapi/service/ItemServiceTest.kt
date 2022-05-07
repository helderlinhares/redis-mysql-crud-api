package me.hl.redisapi.service

import me.hl.redisapi.Commons.Companion.ITEM_DEFAULT_ID
import me.hl.redisapi.buildAlternativeItemRequest
import me.hl.redisapi.buildItem
import me.hl.redisapi.buildItemRequest
import me.hl.redisapi.buildModifiedItem
import me.hl.redisapi.domain.Item
import me.hl.redisapi.domain.ItemRepository
import me.hl.redisapi.domain.ItemService
import me.hl.redisapi.exception.ErrorCode
import me.hl.redisapi.exception.ItemNotFoundException
import me.hl.redisapi.rest.toDomain
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.BDDMockito
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import java.util.Optional

@SpringBootTest
class ItemServiceTest {

    private val itemRepository = Mockito.mock(ItemRepository::class.java)
    private val itemService = ItemService(itemRepository)

    @Test
    fun `Should return Item When Item exists`(){
        val item = buildItem()
        BDDMockito.given(itemRepository.findById(ITEM_DEFAULT_ID)).willReturn(Optional.of(item))

        val serviceResult = itemService.findById(ITEM_DEFAULT_ID)

        assertThat(serviceResult).usingRecursiveComparison().isEqualTo(item)
    }

    @Test
    fun `Should return Item list When at least one Item exists`(){
        val items = listOf(buildItem())
        BDDMockito.given(itemRepository.findAll()).willReturn(items)

        val serviceResult = itemService.findAll()

        assertThat(serviceResult).usingRecursiveComparison().isEqualTo(items)
    }

    @Test
    fun `Should return empty list When no Item exists`(){
        val items = listOf<Item>()
        BDDMockito.given(itemRepository.findAll()).willReturn(items)

        val serviceResult = itemService.findAll()

        assertThat(serviceResult).usingRecursiveComparison().isEqualTo(items)
    }

    @Test(expected = ItemNotFoundException::class)
    fun `Should return ItemNotFoundException When search for an Item that does not exists`(){
        BDDMockito.given(itemRepository.findById(ITEM_DEFAULT_ID))
            .willThrow(ItemNotFoundException(ErrorCode.ITEM_NOT_FOUND))

        itemService.findById(ITEM_DEFAULT_ID)
    }

    @Test
    fun `Should create Item`(){
        val item = buildItem()
        val itemRequest = buildItemRequest().toDomain()
        BDDMockito.given(itemRepository.save(itemRequest)).willReturn(item)

        val serviceResult = itemService.create(itemRequest)

        assertThat(serviceResult).usingRecursiveComparison().isEqualTo(item)
    }


    @Test
    fun `Should update Item When Item exists`(){
        val item = buildItem()
        val itemRequest = buildAlternativeItemRequest().toDomain()
        val modifiedItem = buildModifiedItem()
        BDDMockito.given(itemRepository.findById(ITEM_DEFAULT_ID)).willReturn(Optional.of(item))
        BDDMockito.given(itemRepository.save(modifiedItem)).willReturn(modifiedItem)

        val serviceResult = itemService.update(ITEM_DEFAULT_ID, itemRequest)

        assertThat(serviceResult).usingRecursiveComparison().isEqualTo(modifiedItem)
    }

    @Test(expected = ItemNotFoundException::class)
    fun `Should return ItemNotFoundException When try to update an Item that does not exists`(){
        val itemRequest = buildAlternativeItemRequest().toDomain()
        BDDMockito.given(itemRepository.findById(ITEM_DEFAULT_ID)).willReturn(Optional.empty())

        itemService.update(ITEM_DEFAULT_ID, itemRequest)
    }

    @Test
    fun `Should delete Item When Item exists`(){
        val item = buildItem()
        BDDMockito.given(itemRepository.findById(ITEM_DEFAULT_ID)).willReturn(Optional.of(item))
        BDDMockito.`when`(itemRepository.deleteById(ITEM_DEFAULT_ID)).then { }

        val serviceResult = itemService.delete(ITEM_DEFAULT_ID)

        assertThat(serviceResult).usingRecursiveComparison().isEqualTo(Unit)
    }

    @Test(expected = ItemNotFoundException::class)
    fun `Should return ItemNotFoundException When try to delete an Item that does not exists`(){
        BDDMockito.given(itemRepository.findById(ITEM_DEFAULT_ID)).willReturn(Optional.empty())

        itemService.delete(ITEM_DEFAULT_ID)
    }

}
