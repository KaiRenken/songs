package de.kairenken.songs.infrastructure.song.repository

import de.kairenken.songs.domain.song.songArtistFixture
import de.kairenken.songs.domain.song.songIdFixture
import de.kairenken.songs.domain.song.songLyricsFixture
import de.kairenken.songs.domain.song.songNameFixture
import de.kairenken.songs.infrastructure.song.repository.model.SongEntity

val songEntityFixture = SongEntity(
    id = songIdFixture.value,
    name = songNameFixture.value,
    artist = songArtistFixture.value,
    lyrics = songLyricsFixture.value
)