package hr.dtakac.prognoza.repository.preferences

interface PreferencesRepository {
    suspend fun setSelectedPlaceId(placeId: String)
    suspend fun getSelectedPlaceId(): String
}