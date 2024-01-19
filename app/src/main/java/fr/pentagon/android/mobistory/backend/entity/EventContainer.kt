package fr.pentagon.android.mobistory.backend.entity

import androidx.room.ColumnInfo
import androidx.room.TypeConverter

data class AliasesContainer(
    @ColumnInfo(name = "aliases")
    val aliases: List<String>
)

class StringConverter {
    @TypeConverter
    fun stringToAliases(value: String): AliasesContainer = AliasesContainer(value.split(","))
    @TypeConverter
    fun aliasesToString(aliases: AliasesContainer): String = aliases.aliases.joinToString(",")
}

data class CoordinatesContainer(
    @ColumnInfo(name = "coordinates")
    val coordinates: List<String>
)

data class ImageContainer(
    @ColumnInfo(name = "images")
    val images: List<String>
)