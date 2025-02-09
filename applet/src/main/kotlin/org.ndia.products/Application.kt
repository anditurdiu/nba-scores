package org.ndia.products

import org.ndia.products.model.Conference
import org.ndia.products.model.Participant
import org.ndia.products.model.Team.*
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
    /*
    val andi = participants.find { participant -> participant.name == "Andi" }
    if (andi != null) {
        andi.points -= 3
    }
     */
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

fun loadParticipants(): List<Participant> {
    return listOf(andi(), jurgi(), stoja())
}

private fun andi(): Participant {
    val east = listOf(
        BOSTON_CELTICS,
        NEW_YORK_KNICKS,
        PHILADELPHIA_76,
        MILWAUKEE_BUCKS,
        INDIANA_PACERS,
        ORLANDO_MAGIC,
        CLEVELAND_CAVALIERS,
        TORONTO_RAPTORS,
        ATLANTA_HAWKS,
        CHICAGO_BULLS,
        MIAMI_HEAT,
        DETROIT_PISTONS,
        CHARLOTTE_HORNETS,
        BROOKLYN_NETS,
        WASHINGTON_WIZARDS,
    )

    val west = listOf(
        OKLAHOMA_CITY_THUNDER,
        MINNESOTA_TIMBERWOLVES,
        DENVER_NUGGETS,
        DALLAS_MAVERICKS,
        PHOENIX_SUNS,
        LOS_ANGELES_CLIPPERS,
        SACRAMENTO_KINGS,
        LOS_ANGELES_LAKERS,
        NEW_ORLEANS_PELICANS,
        GOLDEN_STATE_WARRIORS,
        SAN_ANTONIO_SPURS,
        MEMPHIS_GRIZZLIES,
        HOUSTON_ROCKETS,
        UTAH_JAZZ,
        PORTLAND_TRAIL_BLAZERS,
    )

    return Participant("Andi", east, west)
}

private fun jurgi(): Participant {
    val east = listOf(
        MILWAUKEE_BUCKS,
        BOSTON_CELTICS,
        ORLANDO_MAGIC,
        NEW_YORK_KNICKS,
        INDIANA_PACERS,
        CLEVELAND_CAVALIERS,
        PHILADELPHIA_76,
        ATLANTA_HAWKS,
        MIAMI_HEAT,
        DETROIT_PISTONS,
        TORONTO_RAPTORS,
        BROOKLYN_NETS,
        WASHINGTON_WIZARDS,
        CHICAGO_BULLS,
        CHARLOTTE_HORNETS,
    )

    val west = listOf(
        DALLAS_MAVERICKS,
        OKLAHOMA_CITY_THUNDER,
        DENVER_NUGGETS,
        MINNESOTA_TIMBERWOLVES,
        PHOENIX_SUNS,
        SACRAMENTO_KINGS,
        LOS_ANGELES_LAKERS,
        HOUSTON_ROCKETS,
        GOLDEN_STATE_WARRIORS,
        NEW_ORLEANS_PELICANS,
        MEMPHIS_GRIZZLIES,
        SAN_ANTONIO_SPURS,
        UTAH_JAZZ,
        LOS_ANGELES_CLIPPERS,
        PORTLAND_TRAIL_BLAZERS
    )

    return Participant("Jurgi", east, west)
}

private fun stoja(): Participant {
    val east = listOf(
        BOSTON_CELTICS,
        NEW_YORK_KNICKS,
        PHILADELPHIA_76,
        ORLANDO_MAGIC,
        MILWAUKEE_BUCKS,
        INDIANA_PACERS,
        CLEVELAND_CAVALIERS,
        MIAMI_HEAT,
        TORONTO_RAPTORS,
        DETROIT_PISTONS,
        CHARLOTTE_HORNETS,
        ATLANTA_HAWKS,
        CHICAGO_BULLS,
        WASHINGTON_WIZARDS

    )

    val west = listOf(
        OKLAHOMA_CITY_THUNDER,
        DALLAS_MAVERICKS,
        PHOENIX_SUNS,
        MINNESOTA_TIMBERWOLVES,
        LOS_ANGELES_LAKERS,
        NEW_ORLEANS_PELICANS,
        MEMPHIS_GRIZZLIES,
        DENVER_NUGGETS,
        SACRAMENTO_KINGS,
        GOLDEN_STATE_WARRIORS,
        HOUSTON_ROCKETS,
        SAN_ANTONIO_SPURS,
        LOS_ANGELES_CLIPPERS,
        UTAH_JAZZ,
        PORTLAND_TRAIL_BLAZERS

    )

    return Participant("Stoja", east, west)
}
