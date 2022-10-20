package org.ndia.products.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.ndia.products.model.Conference
import org.ndia.products.model.Standing
import org.ndia.products.model.Team
import org.ndia.products.model.dto.ResponseDto

class NbaApi {

    private val mapper: ObjectMapper = ObjectMapper().registerModule(KotlinModule())
    private val url = "https://api-nba-v1.p.rapidapi.com/standings/standard/2022/conference/%s"

    suspend fun getConferenceStandings(conference: Conference): List<Standing> {
        val client = HttpClient(CIO) {
            install(JsonFeature) {
                serializer = JacksonSerializer()
            }
        }

        val response: HttpResponse =
            client.get(url.format(conference.toString().lowercase())) {
                buildHeaders()
            }

        val standingsDto: ResponseDto = mapper.readValue(response.receive() as String)

        return standingsDto.api.standings.map {
            Standing(Team.getTeamById(it.teamId)!!, it.winPercentage, it.conference.rank)
        }.sortedByDescending { it.rank }.reversed().toList()
    }

    private fun HttpRequestBuilder.buildHeaders() {
        headers.apply(fun HeadersBuilder.() {
            append("x-rapidapi-host", "api-nba-v1.p.rapidapi.com")
            append("x-rapidapi-key", "INSERT-API-KEY-HERE")
        })
    }
}