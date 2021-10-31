package org.ndia.products.model

import kotlin.math.abs

class Participant(
    val name: String,
    val predictionEast: List<Team>,
    val predictionWest: List<Team>,
    var points: Int = 0,
    var correctPredictions: MutableList<Team> = mutableListOf()

) {

    fun calculatePoints(standingsEast: List<Team>, standingsWest: List<Team>) {
        predictionEast.forEach { team ->
            points += 5 - abs(predictionEast.indexOf(team) - standingsEast.indexOf(team))
        }

        predictionWest.forEach { team ->
            points += 5 - abs(predictionWest.indexOf(team) - standingsWest.indexOf(team))
        }

        setCorrectPredictions(standingsEast, standingsWest)
    }

    private fun setCorrectPredictions(standingsEast: List<Team>, standingsWest: List<Team>) {

        correctPredictions.addAll(predictionEast.filter { team ->
            predictionEast.indexOf(team) == standingsEast.indexOf(
                team
            )
        })
        correctPredictions.addAll(predictionWest.filter { team ->
            predictionWest.indexOf(team) == standingsWest.indexOf(
                team
            )
        })

    }


}