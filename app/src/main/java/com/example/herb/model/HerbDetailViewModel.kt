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
import com.example.herb.helper.TimeHelper
import com.example.herb.state.HerbDetailState
import com.example.herb.util.StoredHerbSortType
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
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
    private val _state = MutableStateFlow(HerbDetailState(herb = herb))

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
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HerbDetailState(herb = herb))

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

                if (state.value.buyPriceL.isBlank()
                    || state.value.buyWeightF.isBlank()
                    || state.value.storeWeightF.isBlank()
                    || state.value.processTimeF.isBlank()
                    || state.value.additionalCostL.isBlank()
                    || state.value.laborCostL.isBlank()
                ) {
                    Log.d("HerbDetailViewModel", "onEvent: Error on Store Herb")
                    return
                }

                val buyTime = state.value.buyTime
                val buyLocalDate = state.value.buyLocalDate
                val buyPrice = state.value.buyPriceL.toLong()
                val buyWeight = state.value.buyWeightF.toFloat()
                val storeWeight = state.value.storeWeightF.toFloat()
                val processTime = state.value.processTimeF.toFloat()
                val additionalCost = state.value.additionalCostL.toLong()
                val laborCost = state.value.laborCostL.toLong()
                val isImport = state.value.isImport

                val buyDate =
                    TimeHelper.localDateClockToDate(localDate = buyLocalDate, localTime = buyTime)

                val totalCost =
                    (herb.totalWeight ?: 0f) * (herb.avgPrice
                        ?: 0) + processTime * laborCost + additionalCost + buyWeight * buyPrice
//                Log.d("HerbDetailViewModel", "onEvent: totalCost = $totalCost")
                val deltaWeight = if (isImport) storeWeight else -storeWeight
                val totalWeight = deltaWeight + (herb.totalWeight ?: 0f)

                val newHerb = Herb(
                    herbID = herb.herbID,
                    herbName = herb.herbName,
                    totalWeight = totalWeight,
                    avgPrice = ceil(totalCost / totalWeight).toLong()
                )

                val storeHerb = StoredHerb(
                    herbID = herb.herbID,
                    buyDate = TimeHelper.utilDateToSqlLong(
                        TimeHelper.convertToUTC(
                            buyDate,
                            ZoneId.systemDefault()
                        )
                    ),
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
                        buyTime = LocalTime.now(),
                        buyLocalDate = LocalDate.now(),
                        buyPriceL = "",
                        buyWeightF = "",
                        storeWeightF = "",
                        processTimeF = "",
                        additionalCostL = "",
                        laborCostL = "",
                        isImport = true,
                        isDialogOn = false,
                        herb = newHerb
                    )
                }
            }

            is StoredHerbEvent.SetAdditionalCost -> {
                _state.update {
                    it.copy(
                        additionalCostL = event.additionalCostL
                    )
                }
            }

            is StoredHerbEvent.SetBuyPrice -> {
                _state.update {
                    it.copy(
                        buyPriceL = event.buyPriceL
                    )
                }
            }

            is StoredHerbEvent.SetBuyWeight -> {
                _state.update {
                    it.copy(
                        buyWeightF = event.buyWeightF
                    )
                }
            }

            is StoredHerbEvent.SetProcessTime -> {
                _state.update {
                    it.copy(
                        processTimeF = event.processTimeF
                    )
                }
            }

            is StoredHerbEvent.SetStoreWeight -> {
                _state.update {
                    it.copy(
                        storeWeightF = event.storeWeightF
                    )
                }
            }

            is StoredHerbEvent.SetSortType -> {
                _sortType.update { event.storedHerbSortType }
            }

            StoredHerbEvent.ExportHerb -> {
                _state.update {
                    it.copy(
                        isImport = false,
                        isDialogOn = true,
                        processTimeF = "0.0",
                        additionalCostL = "0",
                        laborCostL = "0"
                    )
                }
            }

            StoredHerbEvent.ImportHerb -> {
                _state.update {
                    it.copy(
                        isImport = true,
                        isDialogOn = true
                    )
                }
            }

            is StoredHerbEvent.SetLaborCost -> {
                _state.update {
                    it.copy(
                        laborCostL = event.laborCostL
                    )
                }
            }

            is StoredHerbEvent.SetBuyLocalDate -> {
                _state.update {
                    it.copy(
                        buyLocalDate = event.localDate
                    )
                }
            }

            is StoredHerbEvent.SetBuyTime -> {
                _state.update {
                    it.copy(
                        buyTime = event.buyTime
                    )
                }
            }

            is StoredHerbEvent.SetSellValue -> {
                _state.update {
                    it.copy(
                        buyWeightF = event.sellWeightF,
                        storeWeightF = event.sellWeightF,
                        buyPriceL = event.sellPriceL
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
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(herb) as T
            }
        }
    }
}