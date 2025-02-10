package org.ndia.products

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.ndia.products.model.Conference
import org.ndia.products.model.Participant
import org.ndia.products.model.Team
import org.ndia.products.rest.NbaApi

class Handler : RequestHandler<Map<String, Any>, String> {
    override fun handleRequest(input: Map<String, Any>, context: Context): String {
        context.logger.log("Lambda invoked with input: $input")
        runNbaScores()
        ContentType.Application
        return "NBA Scores API is running!"
    }

    fun runNbaScores() {
        val api = NbaApi()
        var participants: List<Participant> = loadParticipants()

        runBlocking {
            val eastStandings = api.getConferenceStandings(Conference.EAST)
            val westStandings = api.getConferenceStandings(Conference.WEST)

            participants.forEach { participant ->
                participant.calculatePoints(
                    eastStandings.map { it.team },
                    westStandings.map { it.team })
            }

            participants = participants.sortedByDescending { it.points }

            println("Welcome, to the annual NBA prediction games by Choupo Mouting Fan Club!")
            println("The rules are simple. Each participant must give his prediction for the nba regular season.")
            println("For every correct prediction, the participant is awared a total of 5 points. For every position that their prediction is off, they loose a point")
            println()
            println("Example: ")
            println("Prediction: Lakers, 3rd place")
            println("Standing: 1st place")
            println("Points awarded: 5 - abs(1-3) = 3")
            println()

            println("Current Standings :")
            println(" #   EAST                                WEST")
            for (i in 1..15) {
                println("${i.toString().padStart(2)}.  ${eastStandings[i - 1]}        ${westStandings[i - 1]} ")
            }
            println()
            println("Current Results:")
            println("In first place we have ${participants[0].name}, with ${participants[0].points} points!")
            println("${participants[0].name} predicted ${participants[0].correctPredictions} correctly. Worst prediction ${participants[0].worstPrediction?.first} (${participants[0].worstPrediction?.second} points)")
            println()
            println("In second place chasing the leader is ${participants[1].name}, with an impressive ${participants[1].points} points!")
            println("${participants[1].name} predicted ${participants[1].correctPredictions} correctly. Worst prediction ${participants[1].worstPrediction?.first} (${participants[1].worstPrediction?.second} points)")
            println()
            println("And finally, our scrub of the day is ${participants[2].name}, with ${participants[2].points} points!")
            println("${participants[2].name} predicted ${participants[2].correctPredictions} correctly. Worst prediction ${participants[2].worstPrediction?.first} (${participants[2].worstPrediction?.second} points)")
            println()
            println("As things stand: ${participants[2].name} will be buying the pizzas, ${participants[1].name} will take care of the drinks, and ${participants[0].name} will be enjoying the food and drinks.")

        }

        /*
        val andi = participants.find { participant -> participant.name == "Andi" }
        if (andi != null) {
            andi.points -= 3
        }
         */
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