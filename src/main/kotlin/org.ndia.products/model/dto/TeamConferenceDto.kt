package org.ndia.products.model.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class TeamConferenceDto(val name: String, val rank: Int, val win: Int, val loss: Int)