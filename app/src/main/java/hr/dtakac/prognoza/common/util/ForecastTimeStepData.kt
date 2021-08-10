package hr.dtakac.prognoza.common.util

import hr.dtakac.prognoza.api.ForecastTimeStepData

fun ForecastTimeStepData.findSymbolCode() =
    next1Hours?.summary?.symbolCode
        ?: next6Hours?.summary?.symbolCode
        ?: next12Hours?.summary?.symbolCode

fun ForecastTimeStepData.findPrecipitationProbability() =
    next1Hours?.data?.probabilityOfPrecipitation
        ?: next6Hours?.data?.probabilityOfPrecipitation
        ?: next12Hours?.data?.probabilityOfPrecipitation

fun ForecastTimeStepData.findPrecipitationAmount() =
    next1Hours?.data?.precipitationAmount
        ?: next6Hours?.data?.precipitationAmount
        ?: next12Hours?.data?.precipitationAmount