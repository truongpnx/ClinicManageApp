package com.example.herb.event

import com.example.herb.database.entity.Herb
import com.example.herb.state.HerbQuerySort

sealed interface HerbEvent {
    data object SaveHerb: HerbEvent
    data class SetHerbName(val herbName: String): HerbEvent
    data class SetAvgPrice(val avgPrice: Long): HerbEvent
    data class SetTotalWeight(val totalWeight: Float): HerbEvent
    data object ShowDialog: HerbEvent
    data object HideDialog: HerbEvent
    data class DeleteHerb(val herb: Herb): HerbEvent
    data class FindHerbs(val querySort: HerbQuerySort): HerbEvent
}