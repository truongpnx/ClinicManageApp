package com.example.herb.state

import com.example.herb.database.entity.Herb
import com.example.herb.database.entity.Prescription
import com.example.herb.database.entity.StoredHerb
import com.example.herb.util.StoredHerbSortType
import java.time.LocalDate
import java.time.LocalTime

data class HerbDetailState (
    val storedHerbs: List<StoredHerb> = emptyList(),
    val herb: Herb? = null,
    val buyLocalDate: LocalDate = LocalDate.now(),
    val buyTime: LocalTime = LocalTime.now(),
    val buyPriceL: String = "", // Long
    val buyWeightF: String = "", // Float

    val processTimeF: String = "", //Float
    val laborCostL: String = "", // Long
    val storeWeightF: String = "", // Float
    val additionalCostL: String = "", // Long
    val isImport: Boolean = true,

    val prescriptions: List<Prescription> = emptyList(),
    val isDialogOn: Boolean = false,
    val storedHerbSortType: StoredHerbSortType = StoredHerbSortType.DATE
)