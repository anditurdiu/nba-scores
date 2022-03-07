package org.ndia.products.model

import kotlin.math.abs

class Participant(
    val name: String,
    val predictionEast: List<Team>,
    val predictionWest: List<Team>,
    var points: Int = 0,
    var correctPredictions: MutableList<Team> = mutableListOf(),
    var worstPrediction: Pair<MutableList<Team>, Int>? = null

) {

    fun calculatePoints(standingsEast: List<Team>, standingsWest: List<Team>) {
        predictionEast.forEach { team ->
            points += 5 - abs(predictionEast.indexOf(team) - standingsEast.indexOf(team))
        }

        predictionWest.forEach { team ->
            points += 5 - abs(predictionWest.indexOf(team) - standingsWest.indexOf(team))
        }

        setCorrectPredictions(standingsEast, standingsWest)
        worstPrediction(standingsEast, standingsWest)
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

    private fun worstPrediction(standingsEast: List<Team>, standingsWest: List<Team>) {
        predictionEast.forEach { team ->
            if (worstPrediction == null) {
                worstPrediction =
                    Pair(mutableListOf(team), 5 - abs(predictionEast.indexOf(team) - standingsEast.indexOf(team)))
            } else {
                if (worstPrediction?.second!! > 5 - abs(predictionEast.indexOf(team) - standingsEast.indexOf(team))) {
                    worstPrediction =
                        Pair(mutableListOf(team), 5 - abs(predictionEast.indexOf(team) - standingsEast.indexOf(team)))
                } else if (worstPrediction?.second!! == 5 - abs(
                        predictionEast.indexOf(team) - standingsEast.indexOf(
                            team
                        )
                    )
                ) {
                    worstPrediction!!.first.add(team)
                }
            }
        }

        predictionWest.forEach { team ->
            if (worstPrediction == null) {
                worstPrediction =
                    Pair(mutableListOf(team), 5 - abs(predictionWest.indexOf(team) - standingsWest.indexOf(team)))
            } else {
                if (worstPrediction?.second!! > 5 - abs(predictionWest.indexOf(team) - standingsWest.indexOf(team))) {
                    worstPrediction =
                        Pair(mutableListOf(team), 5 - abs(predictionWest.indexOf(team) - standingsWest.indexOf(team)))
                } else if (worstPrediction?.second!! == 5 - abs(
                        predictionEast.indexOf(team) - standingsEast.indexOf(
                            team
                        )
                    )
                ) {
                    worstPrediction!!.first.add(team)
                }
            }
        }
    }


}