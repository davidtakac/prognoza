package hr.dtakac.prognoza.extensions

import hr.dtakac.prognoza.apimodel.ForecastTimeStepData

fun ForecastTimeStepData.findSymbolCode(): String? {
    return when {
        next1Hours != null -> {
            next1Hours.summary?.symbolCode
        }
        next6Hours != null -> {
            next6Hours.summary?.symbolCode
        }
        next12Hours != null -> {
            next12Hours.summary?.symbolCode
        }
        else -> {
            null
        }
    }
}

fun ForecastTimeStepData.findPrecipitationProbability(): Double? {
    return when {
        next1Hours != null -> {
            next1Hours.data?.probabilityOfPrecipitation
        }
        next6Hours != null -> {
            next6Hours.data?.probabilityOfPrecipitation
        }
        next12Hours != null -> {
            next12Hours.data?.probabilityOfPrecipitation
        }
        else -> {
            null
        }
    }
}

fun ForecastTimeStepData.findPrecipitationAmount(): Double? {
    return when {
        next1Hours != null -> {
            next1Hours.data?.precipitationAmount
        }
        next6Hours != null -> {
            next6Hours.data?.precipitationAmount
        }
        next12Hours != null -> {
            next12Hours.data?.precipitationAmount
        }
        else -> {
            null
        }
    }
}