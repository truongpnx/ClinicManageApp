package com.example.herb.factory

import com.example.herb.database.entity.Herb
import com.example.herb.model.HerbDetailViewModel
import dagger.assisted.AssistedFactory

@AssistedFactory
interface HerbDetailViewModelAssistedFactory {
    fun create(herb: Herb): HerbDetailViewModel
}