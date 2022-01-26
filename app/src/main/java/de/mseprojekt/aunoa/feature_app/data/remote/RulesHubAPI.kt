package de.mseprojekt.aunoa.feature_app.data.remote


import java.net.HttpURLConnection
import java.net.URL

class RulesHubAPI {

    fun getHubRules(): String {
        return URL("http://82.165.182.209:5029/rulesHubList").run {
            openConnection().run {
                this as HttpURLConnection
                inputStream.bufferedReader().readText()
            }
        }
    }
}