package de.kairenken.songs.domain.song

interface SongRepository {

    fun store(song: Song)
}