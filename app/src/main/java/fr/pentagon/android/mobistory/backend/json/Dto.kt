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
    val aliases: LanguageReferenceListDTO<String> = emptyLanguageListReference(), // specific
    val description: LanguageReferenceDTO<String>? = null,
    val keywords: LanguageReferenceListDTO<String> = emptyLanguageListReference(), // table
    val images: List<String> = emptyList(), // specific
    val wikipedia: LanguageReferenceDTO<String>? = null,
    val popularity: LanguageReferenceDTO<Int>? = null,
    val type: LanguageReferenceListDTO<String> = emptyLanguageListReference(), // table
    val locations: LanguageReferenceListDTO<String> = emptyLanguageListReference(), // table
    val countries: LanguageReferenceListDTO<String> = emptyLanguageListReference(), // table
    val participants: LanguageReferenceListDTO<String> = emptyLanguageListReference(), // table
    val diaporama: String? = null,
    val coords: List<String> = emptyList(), // specific
    @SerialName("start_date")
    val startDate: String? = null,
    @SerialName("end_date")
    val endDate: String? = null,
    val dates: List<String> = emptyList() // specific
)