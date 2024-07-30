package de.kairenken.songs.infrastructure.song.rest.model

import com.fasterxml.jackson.annotation.JsonProperty

data class CreateSongDto(
    @JsonProperty(value = "name", required = true) val name: String,
    @JsonProperty(value = "artist", required = true) val artist: String,
    @JsonProperty(value = "lyrics", required = false) val lyrics: String?,
)