package ai.nodeiq.store

import androidx.room.TypeConverter

class SkillListConverter {
    @TypeConverter
    fun toStored(skills: List<String>): String = skills.joinToString(separator = ",")

    @TypeConverter
    fun fromStored(stored: String): List<String> =
        if (stored.isBlank()) emptyList() else stored.split(",")
}
