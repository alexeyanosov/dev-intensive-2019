package ru.skillbranch.devintensive.utils

object Utils {
    fun parseFullName(fullName: String?): Pair<String?, String?> {
        if (fullName.isNullOrBlank()) {
            return null to null
        }
        val parts: List<String>? = fullName.split(" ")
        val firstName = parts?.getOrNull(0)
        val lastName = parts?.getOrNull(1)
        return firstName to lastName
    }

    fun transliteration(payload: String, divider: String = " "): String {
        val items = mapOf(
            ' ' to divider,
            'а' to "a",
            'б' to "b",
            'в' to "v",
            'г' to "g",
            'д' to "d",
            'е' to "e",
            'ё' to "e",
            'ж' to "zh",
            'з' to "z",
            'и' to "i",
            'й' to "i",
            'к' to "k",
            'л' to "l",
            'м' to "m",
            'н' to "n",
            'о' to "o",
            'п' to "p",
            'р' to "r",
            'с' to "s",
            'т' to "t",
            'у' to "u",
            'ф' to "f",
            'х' to "h",
            'ц' to "c",
            'ч' to "ch",
            'ш' to "sh",
            'щ' to "sh",
            'ъ' to "",
            'ы' to "i",
            'ь' to "",
            'э' to "e",
            'ю' to "yu",
            'я' to "ya"
        )

        var result = ""
        payload.forEach {
            if (it.isLowerCase()) {
                result += items[it] ?: it
            } else {
                val s = items[it.toLowerCase()]
                if (s.isNullOrBlank()) {
                    result += it
                } else {
                    result += s.get(0).toUpperCase()
                    if (s.length > 1) {
                        result += s.get(1)
                    }
                }
            }
        }

        return result
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        var res = ""
        if (!firstName.isNullOrBlank()) {
            res += firstName[0].toUpperCase().toString()
        }
        if (!lastName.isNullOrBlank()) {
            res += lastName[0].toUpperCase().toString()
        }

        return if (res.isEmpty()) null else res
    }
}