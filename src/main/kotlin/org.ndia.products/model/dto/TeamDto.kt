package org.ndia.products.model.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class TeamDto(val teamId: Int, val winPercentage: Double, val conference: TeamConferenceDto)