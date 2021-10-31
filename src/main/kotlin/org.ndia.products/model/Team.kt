package org.ndia.products.model

enum class Team(val id: Int, val conference: Conference) {

    NEW_YORK_KNICKS(24, Conference.EAST),
    MIAMI_HEAT(20, Conference.EAST),
    WASHINGTON_WIZARDS(41, Conference.EAST),
    CHICAGO_BULLS(6, Conference.EAST),
    CHARLOTTE_HORNETS(5, Conference.EAST),
    PHILADELPHIA_76(27, Conference.EAST),
    TORONTO_RAPTORS(38, Conference.EAST),
    MILWAUKEE_BUCKS(21, Conference.EAST),
    ATLANTA_HAWKS(1, Conference.EAST),
    BROOKLYN_NETS(4, Conference.EAST),
    CLEVELAND_CAVALIERS(7, Conference.EAST),
    BOSTON_CELTICS(2, Conference.EAST),
    DETROIT_PISTONS(10, Conference.EAST),
    ORLANDO_MAGIC(26, Conference.EAST),
    INDIANA_PACERS(15, Conference.EAST),

    GOLDEN_STATE_WARRIORS(11, Conference.WEST),
    UTAH_JAZZ(40, Conference.WEST),
    DENVER_NUGGETS(9, Conference.WEST),
    DALLAS_MAVERICKS(8, Conference.WEST),
    SACRAMENTO_KINGS(30, Conference.WEST),
    MINNESOTA_TIMBERWOLVES(22, Conference.WEST),
    PORTLAND_TRAIL_BLAZERS(29, Conference.WEST),
    LOS_ANGELES_LAKERS(17, Conference.WEST),
    MEMPHIS_GRIZZLIES(19, Conference.WEST),
    PHOENIX_SUNS(28, Conference.WEST),
    SAN_ANTONIO_SPURS(31, Conference.WEST),
    LOS_ANGELES_CLIPPERS(16, Conference.WEST),
    HOUSTON_ROCKETS(14, Conference.WEST),
    OKLAHOMA_CITY_THUNDER(25, Conference.WEST),
    NEW_ORLEANS_PELICANS(23, Conference.WEST);

    companion object {
        fun getEasternConferenceTeams(): Set<Team> {
            return values().asSequence().filter { team -> Conference.EAST == team.conference }.toSet()
        }

        fun getWesternConferenceTeams(): Set<Team> {
            return values().asSequence().filter { team -> Conference.WEST == team.conference }.toSet()
        }

        fun getTeamById(id: Int): Team? {
            return values().asSequence().find { team -> team.id == id }
        }
    }

}