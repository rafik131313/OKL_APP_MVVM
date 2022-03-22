package com.example.okl_app_mvvm.other


object Constants {

    const val SIGNING_DATABASE_NAME = "signingdb"
    const val PHOTO_RECOGNIZE_REQUEST_CODE = 1
    const val SHARED_PREFERENCES_NAME = "SHARED_NAME"
    const val KEY_SHARED_PREFERENCES_OVERALLPOINTS = "POINTS_OVERALL"
    const val KEY_WAS_PLAYED_QUIZ = "QUIZ"
    const val KEY_SHAREDPREFERENCES_VISITING_CASTLE = "CASTLE"
    const val KEY_SHAREDPREFERENCES_VISITING_ANNEXE = "ANNEXE"
    const val KEY_SHAREDPREFERENCES_VISITING_BISONS = "BISONS"
    const val KEY_SHAREDPREFERENCES_VISITING_ROZA = "ROZA"
    const val KEY_SHAREDPREFERENCES_VISITING_TULIPAN = "TULIPAN"
    const val KEY_SHAREDPREFERENCES_VISITING_PRZEBISNIEG = "PRZEBISNIEG"
    const val WHATS_ON_PHOTO = "WHATS_ON_PHOTO"
    const val ZAMEK = "Zamek"
    const val OFICYNA = "Oficyna"
    const val YELLOW = "Yellow"
    const val TRACTOR = "Tractor"
    const val TOTEM = "Totem"
    const val MASK = "Mask"

    const val REQUEST_CODE_LOCATION_PERMISSION = 0

    const val ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE"
    const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
    const val ACTION_SHOW_TRACKING_FRAGMENT = "ACTION_SHOW_TRACKING_FRAGMENT"

    const val LOCATION_UPDATE_INTERVAL = 8000L
    const val FASTEST_LOCATION_INTERVAL = 2000L

    const val NOTIFICATION_CHANNEL_ID = "tracking_channel"
    const val NOTIFICATION_CHANNEL_NAME = "Tracking"
    const val NOTIFICATION_ID = 1

    const val TOTAL_QUESTIONS = "total_questions"
    const val CORRECT_ANSWERS = "correct_answers"

    const val NOTIFICATION_NAME = "OKL_APP"
    const val NOTIFICATION_TEXT = "Dobrej zabawy!"

    val CASTLE_COORDINATES = Coordinates(
        a = 51.852281,
        b = 51.852843,
        c = 17.933217,
        d = 17.934528
    )
    val ANNEXE_COORDINATES = Coordinates(
        a = 51.850835,
        b = 51.851660,
        c = 17.934839,
        d = 17.936244
    )
    val BISONS_COORDINATES = Coordinates(
        a = 51.858604,
        b = 51.860241,
        c = 17.922651,
        d = 17.925692
    )


    fun getQuestionsPL(): ArrayList<Question> {

        val questionsList = ArrayList<Question>()

        val que1 = Question(
            1, "Jaka rzeka płynie przez Gołuchowski park?",
            "Ciemna", "Wisła",
            "Odra", "Strumienka", 1
        )

        questionsList.add(que1)

        val que2 = Question(
            2, "Ile hektarów ma park w Gołuchowie?",
            "150ha", "50ha",
            "3ha", "400ha", 1
        )

        questionsList.add(que2)

        val que3 = Question(
            3, "Ile żubrów znajduje się w zagrodzie Gołuchowskiej?",
            "2", "4",
            "5", "9", 3
        )

        questionsList.add(que3)

        val que4 = Question(
            4, "Ile mostów znajduje się w parku w Gołuchowie?",
            "3", "4",
            "5", "8", 2
        )

        questionsList.add(que4)

        val que5 = Question(
            5, "Kiedy powstał park w Gołuchowie?",
            "1900", "1920",
            "1750", "1550", 3
        )

        questionsList.add(que5)

        return questionsList
    }

    fun getQuestionsEN(): ArrayList<Question> {

        val questionsList = ArrayList<Question>()

        val que1 = Question(
            1, "What river flows through the Gołuchowski Park?",
            "Ciemna", "Wisła",
            "Odra", "Strumienka", 1
        )

        questionsList.add(que1)

        val que2 = Question(
            2, "How many hectares does the park in Gołuchów have?",
            "150ha", "50ha",
            "3ha", "400ha", 1
        )

        questionsList.add(que2)

        val que3 = Question(
            3, "How many bisons are there in the Gołuchow farm?",
            "2", "4",
            "5", "9", 3
        )

        questionsList.add(que3)

        val que4 = Question(
            4, "How many bridges are there in the park in Gołuchów?",
            "3", "4",
            "5", "8", 2
        )

        questionsList.add(que4)

        val que5 = Question(
            5, "When was the park in Gołuchów built?",
            "1900", "1920",
            "1750", "1550", 3
        )

        questionsList.add(que5)

        return questionsList
    }
}