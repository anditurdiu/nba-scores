package org.ndia.products.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import org.ndia.products.model.Conference
import org.ndia.products.model.Standing
import org.ndia.products.model.Team
import org.ndia.products.model.dto.ResponseDto

class NbaApi {

    private val mapper: ObjectMapper = ObjectMapper().registerModule(
        KotlinModule.Builder()
            .withReflectionCacheSize(512)
            .configure(KotlinFeature.NullToEmptyCollection, false)
            .configure(KotlinFeature.NullToEmptyMap, false)
            .configure(KotlinFeature.NullIsSameAsDefault, false)
            .configure(KotlinFeature.SingletonSupport, false)
            .configure(KotlinFeature.StrictNullChecks, false)
            .build()
    )
    private val url = "https://api-nba-v1.p.rapidapi.com/standings/standard/2023/conference/%s"

    suspend fun getConferenceStandings(conference: Conference): List<Standing> {
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                jackson()
            }
        }

        val response: HttpResponse =
            client.get(url.format(conference.toString().lowercase())) {
                buildHeaders()
            }

        val standingsDto: ResponseDto = mapper.readValue(response.body() as String)

        return standingsDto.api.standings.map {
            Standing(Team.getTeamById(it.teamId)!!, it.winPercentage, it.conference.rank)
        }.sortedByDescending { it.rank }.reversed().toList()
    }

    private fun HttpRequestBuilder.buildHeaders() {
        headers.apply(fun HeadersBuilder.() {
            append("x-rapidapi-host", "api-nba-v1.p.rapidapi.com")
            append("x-rapidapi-key", "4f4c20b41fmsh697f40a872efa3cp12dfadjsn6d0797b920e2")
        })
    }
}