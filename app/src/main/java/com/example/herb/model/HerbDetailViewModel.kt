package com.example.herb.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.herb.database.dao.HerbDao
import com.example.herb.database.dao.StoreHerbDao
import com.example.herb.database.entity.Herb
import com.example.herb.database.entity.StoredHerb
import com.example.herb.event.StoredHerbEvent
import com.example.herb.state.HerbDetailState
import com.example.herb.util.StoredHerbSortType
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.ceil

//@HiltViewModel(assistedFactory = HerbDetailViewModel.HerbDetailViewModelFactory::class)
class HerbDetailViewModel @AssistedInject constructor(
    private val storeHerbDao: StoreHerbDao,
    private val herbDao: HerbDao,
    @Assisted private val herb: Herb,
) : ViewModel() {

    private val _prescriptions = storeHerbDao.getPrescriptions(herbID = herb.herbID)
    private val _sortType = MutableStateFlow(StoredHerbSortType.DATE)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _storedHerbs = _sortType
        .flatMapLatest { sortType ->
            when (sortType) {
                StoredHerbSortType.DATE -> storeHerbDao.getStoredHerbByIDOrderByDate(herb.herbID)
                StoredHerbSortType.BUY_PRICE -> storeHerbDao.getStoredHerbByIDOrderByBuyPrice(herb.herbID)
                StoredHerbSortType.BUY_WEIGHT -> storeHerbDao.getStoredHerbByIDOrderByBuyWeight(herb.herbID)
                StoredHerbSortType.STORE_WEIGHT -> storeHerbDao.getStoredHerbByIDOrderByStoreWeight(
                    herb.herbID
                )
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(HerbDetailState())

    val state = combine(
        _prescriptions,
        _sortType,
        _storedHerbs,
        _state
    ) { prescriptions, sortType, storedHerbs, state ->
        state.copy(
            prescriptions = prescriptions,
            storedHerbSortType = sortType,
            storedHerbs = storedHerbs,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HerbDetailState())

    fun onEvent(event: StoredHerbEvent) {
        when (event) {
            is StoredHerbEvent.DeleteHerb -> {
                viewModelScope.launch {
                    storeHerbDao.deleteStoredHerb(event.storedHerb)
                }
            }

            StoredHerbEvent.HideDialog -> {
                _state.update {
                    it.copy(
                        isDialogOn = false
                    )
                }
            }

            StoredHerbEvent.SaveStoredHerb -> {
                val buyDate = state.value.buyDate
                val buyPrice = state.value.buyPrice
                val buyWeight = state.value.buyWeight
                val storeWeight = state.value.storeWeight
                val processTime = state.value.processTime
                val additionalCost = state.value.additionalCost
                val laborCost = state.value.laborCost
                val isImport = state.value.isImport

                if (buyDate.isBlank() || buyPrice == 0L || buyWeight < 0.01f || storeWeight > buyWeight || processTime < 0f || additionalCost < 0f || laborCost < 0) {
                    Log.d("HerbDetailViewModel", "onEvent: Error on Store Herb")
                    return
                }

                val totalCost =
                    herb.totalWeight * herb.avgPrice + processTime * laborCost + additionalCost + buyWeight * buyPrice
                Log.d("HerbDetailViewModel", "onEvent: totalCost = $totalCost")
                val totalWeight = herb.totalWeight + storeWeight

                val newHerb = Herb(
                    herbID = herb.herbID,
                    herbName = herb.herbName,
                    totalWeight = totalWeight,
                    avgPrice = ceil(totalCost / totalWeight).toLong()
                )

                val storeHerb = StoredHerb(
                    herbID = herb.herbID,
                    buyDate = buyDate,
                    buyPrice = buyPrice,
                    buyWeight = buyWeight,
                    storeWeight = storeWeight,
                    processTime = processTime,
                    additionalCost = additionalCost,
                    laborCost = laborCost,
                    isImport = isImport,
                )
                viewModelScope.launch {
                    herbDao.upsertHerb(newHerb)
                    storeHerbDao.upsertStoredHerb(storeHerb)
                }
                _state.update {
                    it.copy(
                        buyDate = "",
                        buyPrice = 0,
                        buyWeight = 0f,
                        storeWeight = 0f,
                        processTime = 0f,
                        additionalCost = 0,
                        laborCost = 0,
                        isImport = true,
                        isDialogOn = false
                    )
                }

            }

            is StoredHerbEvent.SetAdditionalCost -> {
                _state.update {
                    it.copy(
                        additionalCost = event.additionalCost
                    )
                }
            }

            is StoredHerbEvent.SetBuyDate -> {
                _state.update {
                    it.copy(
                        buyDate = event.buyDate
                    )
                }
            }

            is StoredHerbEvent.SetBuyPrice -> {
                _state.update {
                    it.copy(
                        buyPrice = event.buyPrice
                    )
                }
            }

            is StoredHerbEvent.SetBuyWeight -> {
                _state.update {
                    it.copy(
                        buyWeight = event.buyWeight
                    )
                }
            }

            is StoredHerbEvent.SetProcessTime -> {
                _state.update {
                    it.copy(
                        processTime = event.processTime
                    )
                }
            }

            is StoredHerbEvent.SetStoreWeight -> {
                _state.update {
                    it.copy(
                        storeWeight = event.storeWeight
                    )
                }
            }

            StoredHerbEvent.ShowDialog -> {
                _state.update {
                    it.copy(
                        isDialogOn = true
                    )
                }
            }

            is StoredHerbEvent.SetSortType -> {
                _sortType.update { event.storedHerbSortType }
            }

            StoredHerbEvent.ExportHerb -> {
                _state.update {
                    it.copy(
                        isImport = false
                    )
                }
            }

            StoredHerbEvent.ImportHerb -> {
                _state.update {
                    it.copy(
                        isImport = true
                    )
                }
            }

            is StoredHerbEvent.SetLaborCost -> {
                _state.update {
                    it.copy(
                        laborCost = event.laborCost
                    )
                }
            }
        }
    }


    @AssistedFactory
    interface HerbDetailViewModelFactory {
        fun create(herb: Herb): HerbDetailViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun providesFactory(
            assistedFactory: HerbDetailViewModelFactory,
            herb: Herb
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(herb) as T
            }
        }
    }
}