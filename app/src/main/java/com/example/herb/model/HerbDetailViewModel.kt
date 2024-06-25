package com.example.herb.model

import com.example.herb.database.dao.HerbDao
import com.example.herb.database.dao.StoreHerbDao
import com.example.herb.database.entity.Herb
import com.example.herb.state.HerbDetailState
import com.example.herb.util.StoredHerbSortType
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class HerbDetailViewModel @AssistedInject constructor (
    @Assisted private val storeHerbDao: StoreHerbDao,
    private val herb: Herb
) {

    private val _sortType = MutableStateFlow(StoredHerbSortType.DATE)
    private val _state = MutableStateFlow(HerbDetailState())

}