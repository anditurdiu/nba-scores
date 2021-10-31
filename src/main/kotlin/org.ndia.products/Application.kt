package org.ndia.products

import org.ndia.products.model.Conference
import org.ndia.products.model.Participant
import org.ndia.products.model.Team
import org.ndia.products.rest.NbaApi


suspend fun main() {
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
    println("${participants[0].name} predicted ${participants[0].correctPredictions} correctly.")
    println()
    println("In second place chasing the leader is ${participants[1].name}, with an impressive ${participants[1].points} points!")
    println("${participants[1].name} predicted ${participants[1].correctPredictions} correctly.")
    println()
    println("And finally, our scrub of the day is ${participants[2].name}, with ${participants[2].points} points!")
    println("${participants[2].name} predicted ${participants[2].correctPredictions} correctly.")
    println()
    println("As things stand: ${participants[2].name} will be buying the pizzas, ${participants[1].name} will take care of the drinks, and ${participants[0].name} will be enjoying the food and drinks.")
}

fun loadParticipants(): List<Participant> {
    return listOf(andi(), jurgi(), stoja())
}

private fun andi(): Participant {
    val east = listOf(
        Team.MILWAUKEE_BUCKS,
        Team.MIAMI_HEAT,
        Team.BROOKLYN_NETS,
        Team.PHILADELPHIA_76,
        Team.BOSTON_CELTICS,
        Team.ATLANTA_HAWKS,
        Team.NEW_YORK_KNICKS,
        Team.CHICAGO_BULLS,
        Team.CHARLOTTE_HORNETS,
        Team.WASHINGTON_WIZARDS,
        Team.TORONTO_RAPTORS,
        Team.INDIANA_PACERS,
        Team.ORLANDO_MAGIC,
        Team.CLEVELAND_CAVALIERS,
        Team.DETROIT_PISTONS
    )

    val west = listOf(
        Team.UTAH_JAZZ,
        Team.GOLDEN_STATE_WARRIORS,
        Team.DENVER_NUGGETS,
        Team.LOS_ANGELES_LAKERS,
        Team.PHOENIX_SUNS,
        Team.MEMPHIS_GRIZZLIES,
        Team.DALLAS_MAVERICKS,
        Team.PORTLAND_TRAIL_BLAZERS,
        Team.LOS_ANGELES_CLIPPERS,
        Team.SACRAMENTO_KINGS,
        Team.NEW_ORLEANS_PELICANS,
        Team.MINNESOTA_TIMBERWOLVES,
        Team.OKLAHOMA_CITY_THUNDER,
        Team.SAN_ANTONIO_SPURS,
        Team.HOUSTON_ROCKETS
    )

    return Participant("Andi", east, west)
}

private fun jurgi(): Participant {
    val east = listOf(
        Team.MILWAUKEE_BUCKS,
        Team.BROOKLYN_NETS,
        Team.ATLANTA_HAWKS,
        Team.CHICAGO_BULLS,
        Team.MIAMI_HEAT,
        Team.PHILADELPHIA_76,
        Team.NEW_YORK_KNICKS,
        Team.BOSTON_CELTICS,
        Team.CHARLOTTE_HORNETS,
        Team.INDIANA_PACERS,
        Team.TORONTO_RAPTORS,
        Team.DETROIT_PISTONS,
        Team.WASHINGTON_WIZARDS,
        Team.ORLANDO_MAGIC,
        Team.CLEVELAND_CAVALIERS
    )

    val west = listOf(
        Team.UTAH_JAZZ,
        Team.LOS_ANGELES_LAKERS,
        Team.PORTLAND_TRAIL_BLAZERS,
        Team.LOS_ANGELES_CLIPPERS,
        Team.PHOENIX_SUNS,
        Team.DENVER_NUGGETS,
        Team.MEMPHIS_GRIZZLIES,
        Team.GOLDEN_STATE_WARRIORS,
        Team.MINNESOTA_TIMBERWOLVES,
        Team.DALLAS_MAVERICKS,
        Team.SACRAMENTO_KINGS,
        Team.NEW_ORLEANS_PELICANS,
        Team.HOUSTON_ROCKETS,
        Team.SAN_ANTONIO_SPURS,
        Team.OKLAHOMA_CITY_THUNDER
    )

    return Participant("Jurgi", east, west)
}

private fun stoja(): Participant {
    val east = listOf(
        Team.MILWAUKEE_BUCKS,
        Team.BROOKLYN_NETS,
        Team.MIAMI_HEAT,
        Team.ATLANTA_HAWKS,
        Team.CHICAGO_BULLS,
        Team.PHILADELPHIA_76,
        Team.CHARLOTTE_HORNETS,
        Team.TORONTO_RAPTORS,
        Team.INDIANA_PACERS,
        Team.BOSTON_CELTICS,
        Team.NEW_YORK_KNICKS,
        Team.CLEVELAND_CAVALIERS,
        Team.DETROIT_PISTONS,
        Team.ORLANDO_MAGIC,
        Team.WASHINGTON_WIZARDS
    )

    val west = listOf(
        Team.UTAH_JAZZ,
        Team.DALLAS_MAVERICKS,
        Team.GOLDEN_STATE_WARRIORS,
        Team.DENVER_NUGGETS,
        Team.LOS_ANGELES_LAKERS,
        Team.MEMPHIS_GRIZZLIES,
        Team.PHOENIX_SUNS,
        Team.SACRAMENTO_KINGS,
        Team.LOS_ANGELES_CLIPPERS,
        Team.PORTLAND_TRAIL_BLAZERS,
        Team.MINNESOTA_TIMBERWOLVES,
        Team.NEW_ORLEANS_PELICANS,
        Team.SAN_ANTONIO_SPURS,
        Team.OKLAHOMA_CITY_THUNDER,
        Team.HOUSTON_ROCKETS
    )

    return Participant("Stoja", east, west)
}

