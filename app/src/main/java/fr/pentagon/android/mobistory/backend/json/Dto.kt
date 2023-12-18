package fr.pentagon.android.mobistory.backend.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LanguageReferenceDTO(
    val fr: String,
    val en: String
)

@Serializable
data class EventDTO(
    val id: Int,
    val label: LanguageReferenceDTO,
    @SerialName("start_date")
    val startDate: String,
    @SerialName("end_date")
    val endDate: String? = null
)