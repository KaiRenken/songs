package de.kairenken.songs.infrastructure.song.repository

import de.kairenken.songs.domain.song.Song
import de.kairenken.songs.domain.song.SongRepository
import de.kairenken.songs.infrastructure.song.repository.model.SongEntity
import org.springframework.stereotype.Repository

@Repository
class SongRepositoryImpl(private val songJpaRepository: SongJpaRepository) : SongRepository {

    override fun store(song: Song): Unit {
        song
            .toEntity()
            .save()
    }

    override fun exists(name: Song.Name, artist: Song.Artist): Boolean = songJpaRepository
        .existsByNameIgnoreCaseAndArtistIgnoreCase(name = name.value, artist = artist.value)

    private fun Song.toEntity() = SongEntity(
        id = this.id.value,
        name = this.name.value,
        artist = this.artist.value,
        lyrics = this.lyrics.value
    )

    private fun SongEntity.save() = songJpaRepository.save(this)
}
