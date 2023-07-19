package com.birdshoe.quizapp

object Constants {

    const val USER_NAME : String = "user_name"
    const val TOTAL_QUESTION : String = "total_questions"
    const val CORRECT_ANSWERS : String = "correct_answers"

    fun getQuestion():ArrayList<Question>{
        val questionsList = ArrayList<Question>()

        val que1 = Question(
            1,
            "¿De qué país es esta bandera?",
            R.drawable.ic_flag_of_argentina,
            "Méjico",
            "Argentina",
            "Australia",
            "España",
            2
        )

        val que2 = Question(
            2,
            "¿De qué país es esta bandera?",
            R.drawable.ic_flag_of_australia,
            "Austria",
            "Italia",
            "Australia",
            "España",
            3
        )

        val que3 = Question(
            3,
            "¿De qué país es esta bandera?",
            R.drawable.ic_flag_of_belgium,
            "Bélgica",
            "Alemania",
            "Francia",
            "Ecuador",
            1
        )

        val que4 = Question(
            4,
            "¿De qué país es esta bandera?",
            R.drawable.ic_flag_of_brazil,
            "Puerto Rico",
            "Brasil",
            "Marruecos",
            "Madagascar",
            2
        )

        val que5 = Question(
            5,
            "¿De qué país es esta bandera?",
            R.drawable.ic_flag_of_denmark,
            "Suecia",
            "Suiza",
            "Noruega",
            "Dinamarca",
            4
        )

        val que6 = Question(
            6,
            "¿De qué país es esta bandera?",
            R.drawable.ic_flag_of_fiji,
            "Fiji",
            "Tailandia",
            "India",
            "España",
            1
        )

        val que7 = Question(
            7,
            "¿De qué país es esta bandera?",
            R.drawable.ic_flag_of_germany,
            "Méjico",
            "Rusia",
            "Polonia",
            "Alemania",
            4
        )

        val que8 = Question(
            8,
            "¿De qué país es esta bandera?",
            R.drawable.ic_flag_of_india,
            "Tailandia",
            "India",
            "Vietnam",
            "China",
            2
        )

        val que9 = Question(
            9,
            "¿De qué país es esta bandera?",
            R.drawable.ic_flag_of_kuwait,
            "Jordania",
            "Arabia Saudi",
            "Kuwait",
            "Marruecos",
            3
        )

        val que10 = Question(
            10,
            "¿De qué país es esta bandera?",
            R.drawable.ic_flag_of_new_zealand,
            "Australia",
            "Nueva Zelanda",
            "Indonesia",
            "Japón",
            2
        )

        questionsList.add(que1)
        questionsList.add(que2)
        questionsList.add(que3)
        questionsList.add(que4)
        questionsList.add(que5)
        questionsList.add(que6)
        questionsList.add(que7)
        questionsList.add(que8)
        questionsList.add(que9)
        questionsList.add(que10)

        return questionsList
    }
}