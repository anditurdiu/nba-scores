package org.ndia.products.model

data class Standing(val team: Team, val winPercentage: Double, val rank: Int) {

    override fun toString(): String {
        return "$team ($winPercentage)".padEnd(28)
    }
}