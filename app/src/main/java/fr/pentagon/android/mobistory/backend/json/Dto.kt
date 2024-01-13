package fr.pentagon.android.mobistory.backend.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LanguageReferenceDTO<T>(
    val fr: T? = null,
    val en: T? = null
)

@Serializable
data class LanguageReferenceListDTO<T>(
    val fr: List<T> = emptyList(),
    val en: List<T> = emptyList()
)

private fun <T> emptyLanguageListReference() = LanguageReferenceListDTO<T>()

@Serializable
data class EventDTO(
    val id: Int,
    val label: LanguageReferenceDTO<String>,
    val aliases: LanguageReferenceListDTO<String> = emptyLanguageListReference(),
    val description: LanguageReferenceDTO<String>? = null,
    val keywords: LanguageReferenceListDTO<String> = emptyLanguageListReference(),
    val images: List<String> = emptyList(),
    val wikipedia: LanguageReferenceDTO<String>? = null,
    val popularity: LanguageReferenceDTO<Int>? = null,
    val type: LanguageReferenceListDTO<String> = emptyLanguageListReference(),
    val locations: LanguageReferenceListDTO<String> = emptyLanguageListReference(),
    val countries: LanguageReferenceListDTO<String> = emptyLanguageListReference(),
    val participants: LanguageReferenceListDTO<String> = emptyLanguageListReference(),
    val diaporama: String? = null,
    val coords: List<String> = emptyList(),
    @SerialName("start_date")
    val startDate: String? = null,
    @SerialName("end_date")
    val endDate: String? = null,
    val dates: List<String> = emptyList()
)