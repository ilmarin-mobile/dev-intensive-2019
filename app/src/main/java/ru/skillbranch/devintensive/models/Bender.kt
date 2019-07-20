package ru.skillbranch.devintensive.models

import java.io.Serializable

class Bender (
    var status: Status = Status.NORMAL,
    var question: Question = Question.NAME
) : Serializable {

    fun askQuestion(): String =
        when (question) {
            Question.NAME -> Question.NAME.question
            Question.PROFESSION -> Question.PROFESSION.question
            Question.MATERIAL -> Question.MATERIAL.question
            Question.BDAY -> Question.BDAY.question
            Question.SERIAL -> Question.SERIAL.question
            Question.IDLE -> Question.IDLE.question
        }

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {
        var resultString = ""
//        if (question.isValidAnswer(answer)) {
        if (question.answers.size > 0)
            if (question.answers.contains(answer.toLowerCase())) {
                question = question.nextQuestion()
                resultString = "Отлично - ты справился"
            } else {
                status = status.nextStatus()
                resultString = "Это неправильный ответ${if (statusWasReset()) ". Давай все по новой" else ""}"
            }
//        } else
//            resultString = question.notValidAnswerResponse

        if (resultString.length > 0) resultString = "${resultString}\n"

        return "${resultString}${question.question}" to status.color
    }

    private fun statusWasReset(): Boolean {
        return if (status == Status.NORMAL) {
            question = Question.NAME
            true
        } else false
    }

    enum class Status(
        val color: Triple<Int, Int, Int>
    ) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus(): Status {
            return if (this.ordinal < values().lastIndex)
                values()[this.ordinal + 1]
            else
                values()[0]
        }
    }
    enum class Question(
        val question: String,
        val answers: List<String>,
        val notValidAnswerResponse: String = ""
    ) {
        NAME("Как меня зовут?", listOf("бендер", "bender"), "Имя должно начинаться с заглавной буквы") {
            override fun nextQuestion(): Question = PROFESSION
            override fun isValidAnswer(answer: String): Boolean = answer.length > 1 && answer[0].equals(answer[0].toUpperCase())
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender"), "Профессия должна начинаться со строчной буквы"){
            override fun nextQuestion(): Question = MATERIAL
            override fun isValidAnswer(answer: String): Boolean = answer.length > 1 && answer[0].equals(answer[0].toLowerCase())
        },
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood"), "Материал не должен содержать цифр") {
            override fun nextQuestion(): Question = BDAY
            override fun isValidAnswer(answer: String): Boolean = answer.length > 1 && !answer.contains(Regex("\\d+"))
        },
        BDAY("Когда меня создали?", listOf("2993"), "Год моего рождения должен содержать только цифры") {
            override fun nextQuestion(): Question = SERIAL
            override fun isValidAnswer(answer: String): Boolean = answer.length > 1 && !answer.contains(Regex("\\D+"))
        },
        SERIAL("Мой серийный номер?", listOf("2716057"), "Серийный номер содержит только цифры, и их 7") {
            override fun nextQuestion(): Question = IDLE
            override fun isValidAnswer(answer: String): Boolean = answer.length == 7 && answer.contains(Regex("\\d+"))
        },
        IDLE("На этом все, вопросов больше нет", listOf()) {
            override fun nextQuestion(): Question = IDLE
            override fun isValidAnswer(answer: String): Boolean = false
        };

        abstract fun nextQuestion(): Question

        abstract fun isValidAnswer(answer: String): Boolean
    }
}