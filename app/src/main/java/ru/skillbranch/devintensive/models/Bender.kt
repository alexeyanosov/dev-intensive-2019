package ru.skillbranch.devintensive.models

import java.util.*

class Bender(var status: Status = Status.NORMAL, var question: Question = Question.NAME) {

    fun askQuestion(): String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {
        if (question == Question.IDLE) {
            return question.question to status.color
        }

        val errorText = question.validate(answer)
        if (errorText != null) {
            return errorText + "\n" + question.question to status.color
        }

        if (question.answers.contains(answer.toLowerCase(Locale.ROOT))) {
            question = question.nextQuestion()
            if (question == Question.IDLE) status = Status.NORMAL
            return "Отлично - ты справился\n${question.question}" to status.color
        } else {
            if (status == Status.CRITICAL) {
                question = Question.NAME
                status = Status.NORMAL
                return "Это неправильный ответ. Давай все по новой\n${question.question}" to status.color
            }
            status = status.nextStatus()
            return "Это неправильный ответ\n${question.question}" to status.color
        }
    }

    enum class Status(var color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus(): Status {
            return if (this.ordinal < values().lastIndex) {
                values()[this.ordinal + 1]
            } else {
                values()[0]
            }
        }
    }

    enum class Question(val question: String, val answers: List<String>) {
        NAME("Как меня зовут?", listOf("bender", "бендер")) {
            override fun nextQuestion(): Question = PROFESSION
            override fun validate(answer: String): String? =
                if (!answer[0].isUpperCase())
                    "Имя должно начинаться с заглавной буквы"
                else null
        },
        PROFESSION("Назови мою проффесию?", listOf("bender", "сгибальщик")) {
            override fun nextQuestion(): Question = MATERIAL
            override fun validate(answer: String): String? =
                if (!answer[0].isLowerCase())
                    "Профессия должна начинаться со строчной буквы"
                else null
        },
        MATERIAL("Из чего я сделан?", listOf("metal", "iron", "wood", "металл", "дерево")) {
            override fun nextQuestion(): Question = BDAY
            override fun validate(answer: String): String? =
                if (answer.contains(Regex("\\d")))
                    "Материал не должен содержать цифр"
                else null
        },
        BDAY("Когда меня создали?", listOf("2993")) {
            override fun nextQuestion(): Question = SERIAL
            override fun validate(answer: String): String? =
                if (answer.contains(Regex("\\D")))
                    "Год моего рождения должен содержать только цифры"
                else null
        },
        SERIAL("Мой серийный номер?", listOf("2715057")) {
            override fun nextQuestion(): Question = IDLE
            override fun validate(answer: String): String? =
                if (!Regex("\\d{7}").matches(answer))
                    "Серийный номер содержит только цифры, и их 7"
                else null
        },
        IDLE("На этом все, вопросов больше нет", listOf()) {
            override fun nextQuestion(): Question = IDLE
            override fun validate(answer: String): String? = ""
        };

        abstract fun nextQuestion(): Question
        abstract fun validate(answer: String): String?
    }
}