package org.ndia.products.model.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.ndia.products.model.dto.ApiDto

@JsonIgnoreProperties(ignoreUnknown = true)
data class ResponseDto(val api: ApiDto)