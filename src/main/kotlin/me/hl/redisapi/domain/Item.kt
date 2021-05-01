package me.hl.redisapi.domain

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator
import javax.persistence.Table

@Entity
@Table(name = "ITEM")
@SequenceGenerator(name = "SQ_ITEM", sequenceName = "SQ_ITEM_ID", allocationSize = 1)
data class Item(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_ITEM")
    @Column(name = "ID_ITEM")
    var id: Long?,
    @Column(name = "TXT_NAME")
    var name: String
) : Serializable