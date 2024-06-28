package de.kairenken.songs.domain.song

import de.kairenken.songs.domain.common.Created
import de.kairenken.songs.domain.common.CreationResult
import de.kairenken.songs.domain.common.InvalidArguments
import de.kairenken.songs.domain.common.create
import java.util.UUID

data class Song(
    val id: Id,
    val name: Name,
    val artist: Artist,
    val lyrics: Lyrics
) {
    data class Id private constructor(val value: UUID) {
        companion object {
            operator fun invoke(value: UUID = UUID.randomUUID()): CreationResult<Id> = Created(Id(value))
        }
    }

    data class Name private constructor(val value: String) {
        companion object {
            const val MAX_NAME_LENGTH = 500

            operator fun invoke(value: String): CreationResult<Name> {
                if (value.isBlank()) return InvalidArguments(listOf("Song.Name must not be blank"))
                if (value.length >= MAX_NAME_LENGTH) return InvalidArguments(listOf("Song.Name must not be longer than $MAX_NAME_LENGTH characters"))
                return Created(Name(value))
            }
        }
    }

    data class Artist private constructor(val value: String) {
        companion object {
            const val MAX_ARTIST_LENGTH = 500

            operator fun invoke(value: String): CreationResult<Artist> {
                if (value.isBlank()) return InvalidArguments(listOf("Song.Artist must not be blank"))
                if (value.length >= MAX_ARTIST_LENGTH) return InvalidArguments(listOf("Song.Artist must not be longer than $MAX_ARTIST_LENGTH characters"))
                return Created(Artist(value))
            }
        }
    }

    data class Lyrics private constructor(val value: String) {
        companion object {
            const val MAX_LYRICS_LENGTH = 20000

            operator fun invoke(value: String = ""): CreationResult<Lyrics> {
                if (value.length >= MAX_LYRICS_LENGTH) return InvalidArguments(listOf("Song.Lyrics must not be longer than $MAX_LYRICS_LENGTH characters"))
                return Created(Lyrics(value))
            }
        }
    }

    companion object {
        operator fun invoke(
            name: String,
            artist: String,
            lyrics: String = ""
        ): CreationResult<Song> =
            create(listOf(Id(), Name(name), Artist(artist), Lyrics(lyrics)), Song::class)

        operator fun invoke(
            id: UUID,
            name: String,
            artist: String,
            lyrics: String
        ): CreationResult<Song> =
            create(listOf(Id(id), Name(name), Artist(artist), Lyrics(lyrics)), Song::class)
    }
}