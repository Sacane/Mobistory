package fr.pentagon.android.mobistory.backend.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LanguageReferenceDTO<T>(
    val fr: T,
    val en: T
)

@Serializable
data class LanguageReferenceListDTO<T>(
    val fr: List<T>,
    val en: List<T>
)

@Serializable
data class EventDTO(
    val id: Int,
    val label: LanguageReferenceDTO<String>,
    val aliases: LanguageReferenceListDTO<String>,
    val description: LanguageReferenceDTO<String>,
    val keywords: LanguageReferenceListDTO<String>,
    val images: List<String>,
    val wikipedia: LanguageReferenceDTO<String>,
    val popularity: LanguageReferenceDTO<Int>,
    val type: LanguageReferenceListDTO<String>,
    val locations: LanguageReferenceListDTO<String>,
    val countries: LanguageReferenceListDTO<String>,
    @SerialName("start_date")
    val startDate: String,
    @SerialName("end_date")
    val endDate: String? = null
)