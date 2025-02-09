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
        MILWAUKEE_BUCKS,
        CLEVELAND_CAVALIERS,
        PHILADELPHIA_76,
        NEW_YORK_KNICKS,
        ATLANTA_HAWKS,
        MIAMI_HEAT,
        BROOKLYN_NETS,
        INDIANA_PACERS,
        ORLANDO_MAGIC,
        CHICAGO_BULLS,
        TORONTO_RAPTORS,
        CHARLOTTE_HORNETS,
        DETROIT_PISTONS,
        WASHINGTON_WIZARDS
    )

    val west = listOf(
        DENVER_NUGGETS,
        PHOENIX_SUNS,
        SACRAMENTO_KINGS,
        MEMPHIS_GRIZZLIES,
        LOS_ANGELES_LAKERS,
        GOLDEN_STATE_WARRIORS,
        LOS_ANGELES_CLIPPERS,
        NEW_ORLEANS_PELICANS,
        MINNESOTA_TIMBERWOLVES,
        UTAH_JAZZ,
        DALLAS_MAVERICKS,
        OKLAHOMA_CITY_THUNDER,
        HOUSTON_ROCKETS,
        PORTLAND_TRAIL_BLAZERS,
        SAN_ANTONIO_SPURS
    )

    return Participant("Andi", east, west)
}

private fun jurgi(): Participant {
    val east = listOf(
        CLEVELAND_CAVALIERS,
        MILWAUKEE_BUCKS,
        BOSTON_CELTICS,
        DETROIT_PISTONS,
        CHICAGO_BULLS,
        NEW_YORK_KNICKS,
        PHILADELPHIA_76,
        ATLANTA_HAWKS,
        ORLANDO_MAGIC,
        MIAMI_HEAT,
        BROOKLYN_NETS,
        INDIANA_PACERS,
        TORONTO_RAPTORS,
        WASHINGTON_WIZARDS,
        CHARLOTTE_HORNETS
    )

    val west = listOf(
        DENVER_NUGGETS,
        LOS_ANGELES_CLIPPERS,
        PHOENIX_SUNS,
        LOS_ANGELES_LAKERS,
        SACRAMENTO_KINGS,
        MINNESOTA_TIMBERWOLVES,
        NEW_ORLEANS_PELICANS,
        OKLAHOMA_CITY_THUNDER,
        GOLDEN_STATE_WARRIORS,
        DALLAS_MAVERICKS,
        MEMPHIS_GRIZZLIES,
        HOUSTON_ROCKETS,
        PORTLAND_TRAIL_BLAZERS,
        SAN_ANTONIO_SPURS,
        UTAH_JAZZ
    )

    return Participant("Jurgi", east, west)
}

private fun stoja(): Participant {
    val east = listOf(
        MILWAUKEE_BUCKS,
        CLEVELAND_CAVALIERS,
        BOSTON_CELTICS,
        NEW_YORK_KNICKS,
        BROOKLYN_NETS,
        PHILADELPHIA_76,
        INDIANA_PACERS,
        ATLANTA_HAWKS,
        ORLANDO_MAGIC,
        DETROIT_PISTONS,
        MIAMI_HEAT,
        TORONTO_RAPTORS,
        CHICAGO_BULLS,
        WASHINGTON_WIZARDS,
        CHARLOTTE_HORNETS
    )

    val west = listOf(
        DENVER_NUGGETS,
        PHOENIX_SUNS,
        DALLAS_MAVERICKS,
        OKLAHOMA_CITY_THUNDER,
        LOS_ANGELES_LAKERS,
        LOS_ANGELES_CLIPPERS,
        NEW_ORLEANS_PELICANS,
        GOLDEN_STATE_WARRIORS,
        MINNESOTA_TIMBERWOLVES,
        MEMPHIS_GRIZZLIES,
        SAN_ANTONIO_SPURS,
        SACRAMENTO_KINGS,
        UTAH_JAZZ,
        PORTLAND_TRAIL_BLAZERS,
        HOUSTON_ROCKETS
    )

    return Participant("Stoja", east, west)
}

