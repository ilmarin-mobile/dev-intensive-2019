package ru.skillbranch.devintensive.models

import ru.skillbranch.devintensive.utils.Utils

data class Profile(
    val firstName: String,
    val lastName: String,
    val about: String,
    val repository: String,
    val rating: Int = 0,
    val respect: Int = 0
) {
    val rank: String = "Android Developer"
    val nickName: String
        get() = Utils.transliteration("${firstName} $lastName", "_")

    fun toMap(): Map<String, Any> = mapOf(
        "nickName" to nickName,
        "rank" to rank,
        "firstName" to firstName,
        "lastName" to lastName,
        "about" to about,
        "repository" to repository,
        "rating" to rating,
        "respect" to respect
    )
}

private val repositoryNicknameExcludeList = listOf("enterprise",
        "features",
        "topics",
        "collections",
        "trending",
        "events",
        "marketplace",
        "pricing",
        "nonprofit",
        "customer-stories",
        "security",
        "login",
        "join"
)

val validateRegexFirstPart: String = "((https://)*|(www.)*)*github.com/"
val validateRegexNickName: String = "[a-zA-Z]+[0-9]*-?[a-zA-Z]+[0-9]*/?"

fun isValidProfileRepository(rep: String?): Boolean {
    var repTrimmed = rep?.trim()
    if (rep == null || repTrimmed == null || repTrimmed?.isEmpty()) return true
    if (!repTrimmed.matches(Regex(validateRegexFirstPart+validateRegexNickName)))
        return false
    else {
        repTrimmed = repTrimmed.replaceFirst(Regex(validateRegexFirstPart), "")
        repositoryNicknameExcludeList.forEach {
            if (repTrimmed!!.equals(it))
                return false
        }
        return true
    }
}