package app.lawnchair.betterlawnicons.api

import app.lawnchair.betterlawnicons.model.GitHubContributor
import retrofit2.http.GET

interface GitHubContributorsAPI {

    @GET("repos/LawnchairLauncher/betterlawnicons/contributors")
    suspend fun getContributors(): List<GitHubContributor>
}
