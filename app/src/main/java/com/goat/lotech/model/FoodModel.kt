package com.goat.lotech.model

data class FoodModel(
    var foodName: String? = null,
    var calories: String? = null,
    var protein: String? = null,
    var fat: String? = null,
    var karbohidrat: String? = null,
    var serat: String? = null,
    var vitaminc: String? = null,

    var calTot: Double? = 0.0,
    var protTot: Double? = 0.0,
    var fatTor: Double? = 0.0,
    var carTor: Double? = 0.0,
    var seratTor: Double? = 0.0,
    var vitcTot: Double? = 0.0,
)