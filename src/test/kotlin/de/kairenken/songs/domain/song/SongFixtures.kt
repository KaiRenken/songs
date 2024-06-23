package de.kairenken.songs.domain.song

import de.kairenken.songs.domain.common.Created
import java.util.UUID

val songIdFixture = (Song.Id(UUID.fromString("cd65a981-2657-48e0-bd95-ba2d7ac9bede")) as Created).value
val songNameFixture = (Song.Name("test-song-name") as Created).value
val songArtistFixture = (Song.Artist("test-song-artist") as Created).value
val songLyricsFixture = (Song.Lyrics("test-song-lyrics") as Created).value

val songFixture = Song(
    songIdFixture,
    songNameFixture,
    songArtistFixture,
    songLyricsFixture
)