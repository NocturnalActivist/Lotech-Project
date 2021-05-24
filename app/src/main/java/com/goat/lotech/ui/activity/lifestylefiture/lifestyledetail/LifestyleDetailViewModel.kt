package com.goat.lotech.ui.activity.lifestylefiture.lifestyledetail

import androidx.lifecycle.ViewModel
import com.goat.lotech.DataDummy
import com.goat.lotech.model.LifestyleModel

class LifestyleDetailViewModel: ViewModel() {

    private lateinit var lifestyleId : String

    fun setSelectedLifestyle(lifestyleId: String) {
        this.lifestyleId = lifestyleId
    }

    fun getLifestyle(): LifestyleModel {
        lateinit var lifestyle: LifestyleModel
        val lifestyleEntities = DataDummy.generateDummyLifestyle()
        for (lifestyleEntity in lifestyleEntities ) {
            if (lifestyleEntity.lifestyleId == lifestyleId) {
                lifestyle = lifestyleEntity
            }
        }

        return lifestyle
    }

}