package com.goat.lotech.ui.activity.lifestylefiture

import androidx.lifecycle.ViewModel
import com.goat.lotech.DataDummy
import com.goat.lotech.model.LifestyleModel

class LifestyleViewModel: ViewModel() {

    fun getLifestyle() : List<LifestyleModel> = DataDummy.generateDummyLifestyle()
}