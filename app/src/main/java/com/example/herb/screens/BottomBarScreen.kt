package com.example.herb.screens

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.herb.R

sealed class BottomBarScreen(
    val route: String,
    @StringRes val title: Int,
    @DrawableRes val icon: Int,

) {
    data object Patient: BottomBarScreen(
        route = "Patient",
        title = R.string.title_patient,
        icon = R.drawable.ic_people_24,

    )

    data object Herb: BottomBarScreen(
        route = "Herb",
        title = R.string.title_herb,
        icon = R.drawable.ic_herb_24
    )

    data object Setting: BottomBarScreen(
        route = "Setting",
        title = R.string.title_setting,
        icon = R.drawable.ic_settings_24
    )
}