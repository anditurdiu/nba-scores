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
        MILWAUKEE_BUCKS,
        BROOKLYN_NETS,
        BOSTON_CELTICS,
        PHILADELPHIA_76,
        MIAMI_HEAT,
        CLEVELAND_CAVALIERS,
        ATLANTA_HAWKS,
        TORONTO_RAPTORS,
        CHICAGO_BULLS,
        CHARLOTTE_HORNETS,
        NEW_YORK_KNICKS,
        WASHINGTON_WIZARDS,
        DETROIT_PISTONS,
        ORLANDO_MAGIC,
        INDIANA_PACERS,
    )

    val west = listOf(
        LOS_ANGELES_CLIPPERS,
        GOLDEN_STATE_WARRIORS,
        DENVER_NUGGETS,
        DALLAS_MAVERICKS,
        MINNESOTA_TIMBERWOLVES,
        PHOENIX_SUNS,
        MEMPHIS_GRIZZLIES,
        NEW_ORLEANS_PELICANS,
        LOS_ANGELES_LAKERS,
        PORTLAND_TRAIL_BLAZERS,
        SACRAMENTO_KINGS,
        OKLAHOMA_CITY_THUNDER,
        HOUSTON_ROCKETS,
        SAN_ANTONIO_SPURS,
        UTAH_JAZZ
    )

    return Participant("Andi", east, west)
}

private fun jurgi(): Participant {
    val east = listOf(
        BROOKLYN_NETS,
        CLEVELAND_CAVALIERS,
        PHILADELPHIA_76,
        MIAMI_HEAT,
        ATLANTA_HAWKS,
        MILWAUKEE_BUCKS,
        BOSTON_CELTICS,
        CHICAGO_BULLS,
        DETROIT_PISTONS,
        WASHINGTON_WIZARDS,
        CHARLOTTE_HORNETS,
        TORONTO_RAPTORS,
        NEW_YORK_KNICKS,
        ORLANDO_MAGIC,
        INDIANA_PACERS
    )

    val west = listOf(
        LOS_ANGELES_CLIPPERS,
        GOLDEN_STATE_WARRIORS,
        MINNESOTA_TIMBERWOLVES,
        NEW_ORLEANS_PELICANS,
        DALLAS_MAVERICKS,
        PORTLAND_TRAIL_BLAZERS,
        DENVER_NUGGETS,
        MEMPHIS_GRIZZLIES,
        LOS_ANGELES_LAKERS,
        PHOENIX_SUNS,
        SACRAMENTO_KINGS,
        SAN_ANTONIO_SPURS,
        OKLAHOMA_CITY_THUNDER,
        HOUSTON_ROCKETS,
        UTAH_JAZZ
    )

    return Participant("Jurgi", east, west)
}

private fun stoja(): Participant {
    val east = listOf(
        PHILADELPHIA_76,
        CLEVELAND_CAVALIERS,
        MILWAUKEE_BUCKS,
        BROOKLYN_NETS,
        ATLANTA_HAWKS,
        BOSTON_CELTICS,
        MIAMI_HEAT,
        CHICAGO_BULLS,
        NEW_YORK_KNICKS,
        CHARLOTTE_HORNETS,
        WASHINGTON_WIZARDS,
        DETROIT_PISTONS,
        TORONTO_RAPTORS,
        ORLANDO_MAGIC,
        INDIANA_PACERS
    )

    val west = listOf(
        GOLDEN_STATE_WARRIORS,
        DENVER_NUGGETS,
        DALLAS_MAVERICKS,
        LOS_ANGELES_CLIPPERS,
        MEMPHIS_GRIZZLIES,
        NEW_ORLEANS_PELICANS,
        MINNESOTA_TIMBERWOLVES,
        LOS_ANGELES_LAKERS,
        PHOENIX_SUNS,
        PORTLAND_TRAIL_BLAZERS,
        UTAH_JAZZ,
        SACRAMENTO_KINGS,
        OKLAHOMA_CITY_THUNDER,
        HOUSTON_ROCKETS,
        SAN_ANTONIO_SPURS
    )

    return Participant("Stoja", east, west)
}

