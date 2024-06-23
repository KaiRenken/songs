package de.kairenken.songs.domain.song

interface SongRepository {

    fun store(song: Song)

    fun exists(name: Song.Name, artist: Song.Artist): Boolean
}