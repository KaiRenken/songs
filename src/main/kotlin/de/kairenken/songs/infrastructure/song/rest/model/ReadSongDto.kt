package de.kairenken.songs.infrastructure.song.rest.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class ReadSongDto(
    @JsonProperty(value = "id") val id: UUID,
    @JsonProperty(value = "name") val name: String,
    @JsonProperty(value = "artist") val artist: String,
    @JsonProperty(value = "lyrics") val lyrics: String,
)