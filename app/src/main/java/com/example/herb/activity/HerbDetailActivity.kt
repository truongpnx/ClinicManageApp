package com.example.herb.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.herb.database.entity.Herb
import com.example.herb.model.HerbDetailViewModel
import com.example.herb.screens.HerbDetailScreen
import com.example.herb.ui.theme.HerbTheme
import com.example.herb.util.IntentExtraName
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HerbDetailActivity : ComponentActivity() {

    @Inject
    lateinit var herbDetailViewModelFactory: HerbDetailViewModel.HerbDetailViewModelFactory

    private lateinit var viewModel: HerbDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var herb = Herb(
            herbID = intent.getIntExtra(IntentExtraName.HERB_ID, 0),
            herbName = intent.getStringExtra(IntentExtraName.HERB_NAME)!!,
            totalWeight = intent.getFloatExtra(IntentExtraName.HERB_WEIGHT, 0f),
            avgPrice = intent.getLongExtra(IntentExtraName.HERB_PRICE, 0),
        )
        viewModel = ViewModelProvider(this, HerbDetailViewModel.providesFactory(herbDetailViewModelFactory, herb))
            .get(HerbDetailViewModel::class.java)

        setContent {
            val state by viewModel.state.collectAsState()
            HerbTheme {
                HerbDetailScreen(state, viewModel::onEvent, Modifier.fillMaxSize())
            }
        }
    }
}