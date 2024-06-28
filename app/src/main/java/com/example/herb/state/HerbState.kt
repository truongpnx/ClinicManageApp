package com.example.herb.state

import com.example.herb.database.entity.Herb
import com.example.herb.util.HerbSortType

data class HerbState (
    val herbs: List<Herb> = emptyList(),
    val herbName: String = "",
    val avgPrice: Long? = null,
    val totalWeight: Float? = null,
    val querySort: HerbQuerySort = HerbQuerySort(),
    val isAddingHerb: Boolean = false,
)

data class HerbQuerySort (
    val stringQuery: String = "",
    val herbSortType: HerbSortType = HerbSortType.ID,
)