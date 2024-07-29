package de.kairenken.songs.infrastructure.song.repository

import de.kairenken.songs.infrastructure.song.repository.model.SongEntity
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SongJpaRepository : JpaRepository<SongEntity, UUID> {

    fun existsByNameIgnoreCaseAndArtistIgnoreCase(name: String, artist: String): Boolean
}