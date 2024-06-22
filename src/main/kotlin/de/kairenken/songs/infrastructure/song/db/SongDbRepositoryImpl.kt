package de.kairenken.songs.infrastructure.song.db

import de.kairenken.songs.domain.song.Song
import de.kairenken.songs.domain.song.SongRepository
import org.springframework.stereotype.Repository

@Repository
class SongDbRepositoryImpl : SongRepository {
    override fun store(song: Song) {
        TODO("Not yet implemented")
    }
}