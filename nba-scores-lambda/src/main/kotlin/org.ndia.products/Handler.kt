package org.ndia.products

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.google.gson.Gson
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.ndia.products.model.Conference
import org.ndia.products.model.Participant
import org.ndia.products.model.Team
import org.ndia.products.rest.NbaApi

class Handler : RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private val corsHeaderMap: Map<String, String> = java.util.Map.of("Access-Control-Allow-Origin", "*")

    override fun handleRequest(input: APIGatewayProxyRequestEvent, context: Context): APIGatewayProxyResponseEvent = runBlocking {
        context.logger.log("Lambda invoked with input: $input")
        val response = runNbaScores()
        return@runBlocking makeResponse(response)
    }

    private fun makeResponse(src: Any?): APIGatewayProxyResponseEvent = APIGatewayProxyResponseEvent()
        .withStatusCode(200)
        .withHeaders(corsHeaderMap)
        .withBody(toJson(src))
        .withIsBase64Encoded(false)

    private fun toJson(src: Any?): String? {
        val gson = Gson()
        return gson.toJson(src)
    }

    suspend fun runNbaScores() : String {
        val output = StringBuilder()
        val api = NbaApi()
        var participants: List<Participant> = loadParticipants()

        val eastStandings = api.getConferenceStandings(Conference.EAST)
        val westStandings = api.getConferenceStandings(Conference.WEST)

        participants.forEach { participant ->
            participant.calculatePoints(
                eastStandings.map { it.team },
                westStandings.map { it.team })
        }

        participants = participants.sortedByDescending { it.points }

        output.append("""
                <h1>NBA Prediction Games - Choupo Mouting Fan Club</h1>
                <div class="rules">
                    <h2>Rules</h2>
                    <p>Each participant must give their prediction for the NBA regular season.</p>
                    <p>For every correct prediction, the participant is awarded a total of 5 points. 
                       For every position that their prediction is off, they lose a point.</p>
                    
                    <h3>Example:</h3>
                    <pre>
Prediction: Lakers, 3rd place
Standing: 1st place
Points awarded: 5 - abs(1-3) = 3</pre>
                </div>

                <h2>Current Standings</h2>
                <div class="standings">
                    <div class="conference">
                        <h3>Eastern Conference</h3>
            """)

        // East Standings
        for (i in 1..15) {
            output.append("""
                    <div class="standings-row">${i.toString().padStart(2)}. ${eastStandings[i - 1]}</div>
                """)
        }

        output.append("""
                    </div>
                    <div class="conference">
                        <h3>Western Conference</h3>
            """)

        // West Standings
        for (i in 1..15) {
            output.append("""
                    <div class="standings-row">${i.toString().padStart(2)}. ${westStandings[i - 1]}</div>
                """)
        }

        output.append("""
                    </div>
                </div>

                <div class="results">
                    <h2>Current Results</h2>
            """)

        // First Place
        output.append("""
                <div class="player first">
                    <h3>🥇 First Place - ${participants[0].name}</h3>
                    <p>Points: <strong>${participants[0].points}</strong></p>
                    <p>Correct Predictions: ${participants[0].correctPredictions}</p>
                    <p>Worst Prediction: ${participants[0].worstPrediction?.first} (${participants[0].worstPrediction?.second} points)</p>
                </div>
            """)

        // Second Place
        output.append("""
                <div class="player second">
                    <h3>🥈 Second Place - ${participants[1].name}</h3>
                    <p>Points: <strong>${participants[1].points}</strong></p>
                    <p>Correct Predictions: ${participants[1].correctPredictions}</p>
                    <p>Worst Prediction: ${participants[1].worstPrediction?.first} (${participants[1].worstPrediction?.second} points)</p>
                </div>
            """)

        // Third Place
        output.append("""
                <div class="player third">
                    <h3>🥉 Third Place - ${participants[2].name}</h3>
                    <p>Points: <strong>${participants[2].points}</strong></p>
                    <p>Correct Predictions: ${participants[2].correctPredictions}</p>
                    <p>Worst Prediction: ${participants[2].worstPrediction?.first} (${participants[2].worstPrediction?.second} points)</p>
                </div>

                <div class="conclusion">
                    <h3>Final Verdict</h3>
                    <p>${participants[2].name} will be buying the pizzas, ${participants[1].name} will take care of the drinks, 
                    and ${participants[0].name} will be enjoying the food and drinks! 🍕🥤</p>
                </div>
            """)

    output.append("""
            </div>
            </body>
            </html>
        """)
        return output.toString()
    }

    private fun loadParticipants(): List<Participant> {
        return listOf(andi(), jurgi(), stoja())
    }

    private fun andi(): Participant {
        val east = listOf(
            Team.BOSTON_CELTICS,
            Team.NEW_YORK_KNICKS,
            Team.PHILADELPHIA_76,
            Team.MILWAUKEE_BUCKS,
            Team.INDIANA_PACERS,
            Team.ORLANDO_MAGIC,
            Team.CLEVELAND_CAVALIERS,
            Team.TORONTO_RAPTORS,
            Team.ATLANTA_HAWKS,
            Team.CHICAGO_BULLS,
            Team.MIAMI_HEAT,
            Team.DETROIT_PISTONS,
            Team.CHARLOTTE_HORNETS,
            Team.BROOKLYN_NETS,
            Team.WASHINGTON_WIZARDS,
        )

        val west = listOf(
            Team.OKLAHOMA_CITY_THUNDER,
            Team.MINNESOTA_TIMBERWOLVES,
            Team.DENVER_NUGGETS,
            Team.DALLAS_MAVERICKS,
            Team.PHOENIX_SUNS,
            Team.LOS_ANGELES_CLIPPERS,
            Team.SACRAMENTO_KINGS,
            Team.LOS_ANGELES_LAKERS,
            Team.NEW_ORLEANS_PELICANS,
            Team.GOLDEN_STATE_WARRIORS,
            Team.SAN_ANTONIO_SPURS,
            Team.MEMPHIS_GRIZZLIES,
            Team.HOUSTON_ROCKETS,
            Team.UTAH_JAZZ,
            Team.PORTLAND_TRAIL_BLAZERS,
        )

        return Participant("Andi", east, west)
    }

    private fun jurgi(): Participant {
        val east = listOf(
            Team.MILWAUKEE_BUCKS,
            Team.BOSTON_CELTICS,
            Team.ORLANDO_MAGIC,
            Team.NEW_YORK_KNICKS,
            Team.INDIANA_PACERS,
            Team.CLEVELAND_CAVALIERS,
            Team.PHILADELPHIA_76,
            Team.ATLANTA_HAWKS,
            Team.MIAMI_HEAT,
            Team.DETROIT_PISTONS,
            Team.TORONTO_RAPTORS,
            Team.BROOKLYN_NETS,
            Team.WASHINGTON_WIZARDS,
            Team.CHICAGO_BULLS,
            Team.CHARLOTTE_HORNETS,
        )

        val west = listOf(
            Team.DALLAS_MAVERICKS,
            Team.OKLAHOMA_CITY_THUNDER,
            Team.DENVER_NUGGETS,
            Team.MINNESOTA_TIMBERWOLVES,
            Team.PHOENIX_SUNS,
            Team.SACRAMENTO_KINGS,
            Team.LOS_ANGELES_LAKERS,
            Team.HOUSTON_ROCKETS,
            Team.GOLDEN_STATE_WARRIORS,
            Team.NEW_ORLEANS_PELICANS,
            Team.MEMPHIS_GRIZZLIES,
            Team.SAN_ANTONIO_SPURS,
            Team.UTAH_JAZZ,
            Team.LOS_ANGELES_CLIPPERS,
            Team.PORTLAND_TRAIL_BLAZERS
        )

        return Participant("Jurgi", east, west)
    }

    private fun stoja(): Participant {
        val east = listOf(
            Team.BOSTON_CELTICS,
            Team.NEW_YORK_KNICKS,
            Team.PHILADELPHIA_76,
            Team.ORLANDO_MAGIC,
            Team.MILWAUKEE_BUCKS,
            Team.INDIANA_PACERS,
            Team.CLEVELAND_CAVALIERS,
            Team.MIAMI_HEAT,
            Team.TORONTO_RAPTORS,
            Team.DETROIT_PISTONS,
            Team.CHARLOTTE_HORNETS,
            Team.ATLANTA_HAWKS,
            Team.CHICAGO_BULLS,
            Team.WASHINGTON_WIZARDS

        )

        val west = listOf(
            Team.OKLAHOMA_CITY_THUNDER,
            Team.DALLAS_MAVERICKS,
            Team.PHOENIX_SUNS,
            Team.MINNESOTA_TIMBERWOLVES,
            Team.LOS_ANGELES_LAKERS,
            Team.NEW_ORLEANS_PELICANS,
            Team.MEMPHIS_GRIZZLIES,
            Team.DENVER_NUGGETS,
            Team.SACRAMENTO_KINGS,
            Team.GOLDEN_STATE_WARRIORS,
            Team.HOUSTON_ROCKETS,
            Team.SAN_ANTONIO_SPURS,
            Team.LOS_ANGELES_CLIPPERS,
            Team.UTAH_JAZZ,
            Team.PORTLAND_TRAIL_BLAZERS

        )

        return Participant("Stoja", east, west)
    }

}