package com.example.herb.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.herb.database.dao.HerbDao
import com.example.herb.database.entity.Herb
import com.example.herb.event.HerbEvent
import com.example.herb.state.HerbQuerySort
import com.example.herb.state.HerbState
import com.example.herb.util.SortType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HerbViewModel @Inject constructor(
    private val dao: HerbDao
) : ViewModel() {

    private val _querySort = MutableStateFlow(HerbQuerySort())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _herbs = _querySort
        .flatMapLatest { querySort ->
            when (querySort.sortType) {
                SortType.ID -> dao.getHerbsHasStringOrderByID(querySort.stringQuery)
                SortType.NAME -> dao.getHerbsHasStringOrderByName(querySort.stringQuery)
                SortType.AVG_PRICE -> dao.getHerbsHasStringOrderByAvgPrice(querySort.stringQuery)
                SortType.WEIGHT -> dao.getHerbsHasStringOrderByWeight(querySort.stringQuery)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(HerbState())
    val state = combine(_state, _querySort, _herbs) { state, querySort, herbs ->
        state.copy(
            herbs = herbs,
            querySort = querySort,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HerbState())


    fun onEvent(event: HerbEvent) {
        when (event) {

            is HerbEvent.DeleteHerb -> {
                viewModelScope.launch {
                    dao.deleteHerb(event.herb)
                }
            }

            is HerbEvent.FindHerbs -> {
                _state.update {
                    it.copy(
                        querySort = event.querySort
                    )
                }
            }

            HerbEvent.HideDialog -> {
                _state.update {
                    it.copy(
                        isAddingHerb = false
                    )
                }
            }

            HerbEvent.SaveHerb -> {
                val herbName = state.value.herbName
                val avgPrice = state.value.avgPrice
                val totalWeight = state.value.totalWeight

//                if (herbName.isBlank() || avgPrice <= 0 || totalWeight < 0.01f ) {
                if (herbName.isBlank()) {

                    Log.d("HerbViewModel", "onEvent: SaveHerb blank")
                    return
                }
                val herb = Herb(
                    herbName = herbName.trim().lowercase(),
                    avgPrice = avgPrice,
                    totalWeight = totalWeight
                )

                viewModelScope.launch {
                    dao.upsertHerb(herb)
                }

                _state.update {
                    it.copy(
                        isAddingHerb = false,
                        herbName = "",
                        avgPrice = 0,
                        totalWeight = 0f,
                    )
                }
            }

            is HerbEvent.SetHerbName -> {
                _state.update {
                    it.copy(
                        herbName = event.herbName
                    )
                }
            }

            HerbEvent.ShowDialog -> {
                _state.update {
                    it.copy(
                        isAddingHerb = true
                    )
                }
            }

            is HerbEvent.SetAvgPrice -> {
                _state.update {
                    it.copy(
                        avgPrice = event.avgPrice
                    )
                }
            }

            is HerbEvent.SetTotalWeight -> {
                _state.update {
                    it.copy(
                        totalWeight = event.totalWeight
                    )
                }
            }
        }
    }
}