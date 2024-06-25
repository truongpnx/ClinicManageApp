package com.example.herb.state

import com.example.herb.database.entity.Prescription
import com.example.herb.database.entity.StoredHerb
import com.example.herb.util.StoredHerbSortType

data class HerbDetailState (
    val storedHerbs: List<StoredHerb> = emptyList(),
    val buyDate: String = "",
    val buyPrice: Long = 0,
    val buyWeight: Float = 0f,

    val processTime: Float = 0f,
    val storeWeight: Float = 0f,
    val additionalCost: Long = 0,

    val prescriptions: List<Prescription> = emptyList(),
    val isDialogOn: Boolean = false,
    val storedHerbSortType: StoredHerbSortType = StoredHerbSortType.DATE
)