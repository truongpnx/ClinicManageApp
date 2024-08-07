package com.example.herb.event

import com.example.herb.database.entity.StoredHerb
import com.example.herb.util.StoredHerbSortType
import java.time.LocalDate
import java.time.LocalTime

sealed interface StoredHerbEvent {
    data object SaveStoredHerb: StoredHerbEvent
    data class SetBuyLocalDate(val localDate: LocalDate): StoredHerbEvent
    data class SetBuyTime(val buyTime: LocalTime): StoredHerbEvent
    data class SetBuyPrice(val buyPriceL: String): StoredHerbEvent
    data class SetBuyWeight(val buyWeightF: String): StoredHerbEvent
    data class SetSellValue(val sellWeightF: String, val sellPriceL: String): StoredHerbEvent
    data class SetProcessTime(val processTimeF: String): StoredHerbEvent
    data class SetLaborCost(val laborCostL: String): StoredHerbEvent
    data class SetStoreWeight(val storeWeightF: String): StoredHerbEvent
    data class SetAdditionalCost(val additionalCostL: String): StoredHerbEvent

    data class SetSortType(val storedHerbSortType: StoredHerbSortType): StoredHerbEvent
    data object HideDialog: StoredHerbEvent
    data object ExportHerb: StoredHerbEvent
    data object ImportHerb: StoredHerbEvent
    data class DeleteHerb(val storedHerb: StoredHerb): StoredHerbEvent
}