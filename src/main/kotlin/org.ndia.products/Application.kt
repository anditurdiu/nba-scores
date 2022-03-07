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
    val andi = participants.find { participant -> participant.name == "Andi" }
    if (andi != null) {
        andi.points -= 3
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

fun loadParticipants(): List<Participant> {
    return listOf(andi(), jurgi(), stoja())
}

private fun andi(): Participant {
    val east = listOf(
        MILWAUKEE_BUCKS,
        MIAMI_HEAT,
        BROOKLYN_NETS,
        PHILADELPHIA_76,
        BOSTON_CELTICS,
        ATLANTA_HAWKS,
        NEW_YORK_KNICKS,
        CHICAGO_BULLS,
        CHARLOTTE_HORNETS,
        WASHINGTON_WIZARDS,
        TORONTO_RAPTORS,
        INDIANA_PACERS,
        ORLANDO_MAGIC,
        CLEVELAND_CAVALIERS,
        DETROIT_PISTONS
    )

    val west = listOf(
        UTAH_JAZZ,
        GOLDEN_STATE_WARRIORS,
        DENVER_NUGGETS,
        LOS_ANGELES_LAKERS,
        PHOENIX_SUNS,
        MEMPHIS_GRIZZLIES,
        DALLAS_MAVERICKS,
        PORTLAND_TRAIL_BLAZERS,
        LOS_ANGELES_CLIPPERS,
        SACRAMENTO_KINGS,
        NEW_ORLEANS_PELICANS,
        MINNESOTA_TIMBERWOLVES,
        OKLAHOMA_CITY_THUNDER,
        SAN_ANTONIO_SPURS,
        HOUSTON_ROCKETS
    )

    return Participant("Andi", east, west)
}

private fun jurgi(): Participant {
    val east = listOf(
        MILWAUKEE_BUCKS,
        BROOKLYN_NETS,
        ATLANTA_HAWKS,
        CHICAGO_BULLS,
        MIAMI_HEAT,
        PHILADELPHIA_76,
        NEW_YORK_KNICKS,
        BOSTON_CELTICS,
        CHARLOTTE_HORNETS,
        INDIANA_PACERS,
        TORONTO_RAPTORS,
        DETROIT_PISTONS,
        WASHINGTON_WIZARDS,
        ORLANDO_MAGIC,
        CLEVELAND_CAVALIERS
    )

    val west = listOf(
        UTAH_JAZZ,
        LOS_ANGELES_LAKERS,
        PORTLAND_TRAIL_BLAZERS,
        LOS_ANGELES_CLIPPERS,
        PHOENIX_SUNS,
        DENVER_NUGGETS,
        MEMPHIS_GRIZZLIES,
        GOLDEN_STATE_WARRIORS,
        MINNESOTA_TIMBERWOLVES,
        DALLAS_MAVERICKS,
        SACRAMENTO_KINGS,
        NEW_ORLEANS_PELICANS,
        HOUSTON_ROCKETS,
        SAN_ANTONIO_SPURS,
        OKLAHOMA_CITY_THUNDER
    )

    return Participant("Jurgi", east, west)
}

private fun stoja(): Participant {
    val east = listOf(
        MILWAUKEE_BUCKS,
        BROOKLYN_NETS,
        MIAMI_HEAT,
        ATLANTA_HAWKS,
        CHICAGO_BULLS,
        PHILADELPHIA_76,
        CHARLOTTE_HORNETS,
        TORONTO_RAPTORS,
        INDIANA_PACERS,
        BOSTON_CELTICS,
        NEW_YORK_KNICKS,
        CLEVELAND_CAVALIERS,
        DETROIT_PISTONS,
        ORLANDO_MAGIC,
        WASHINGTON_WIZARDS
    )

    val west = listOf(
        UTAH_JAZZ,
        DALLAS_MAVERICKS,
        GOLDEN_STATE_WARRIORS,
        DENVER_NUGGETS,
        LOS_ANGELES_LAKERS,
        MEMPHIS_GRIZZLIES,
        PHOENIX_SUNS,
        SACRAMENTO_KINGS,
        LOS_ANGELES_CLIPPERS,
        PORTLAND_TRAIL_BLAZERS,
        MINNESOTA_TIMBERWOLVES,
        NEW_ORLEANS_PELICANS,
        SAN_ANTONIO_SPURS,
        OKLAHOMA_CITY_THUNDER,
        HOUSTON_ROCKETS
    )

    return Participant("Stoja", east, west)
}

