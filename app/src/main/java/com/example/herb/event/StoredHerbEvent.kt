package com.example.herb.event

import com.example.herb.database.entity.Herb
import com.example.herb.util.StoredHerbSortType

interface StoredHerbEvent {
    data object SaveStoredHerb: StoredHerbEvent
    data class SetBuyDate(val buyDate: String): StoredHerbEvent
    data class SetBuyPrice(val buyPrice: Long): StoredHerbEvent
    data class SetBuyWeight(val buyWeight: Float): StoredHerbEvent
    data class SetProcessTime(val processTime: Float): StoredHerbEvent
    data class SetStoreWeight(val storeWeight: Float): StoredHerbEvent
    data class SetAdditionalCost(val additionalCost: Long): StoredHerbEvent

    data class SortStoredHerbByID(val storeID: Int, val storedHerbSortType: StoredHerbSortType): StoredHerbEvent
    data object ShowDialog: StoredHerbEvent
    data object HideDialog: StoredHerbEvent
    data class DeleteHerb(val storedHerbEvent: StoredHerbEvent): StoredHerbEvent
}