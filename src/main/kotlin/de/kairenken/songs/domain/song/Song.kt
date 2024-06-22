package de.kairenken.songs.domain.song

import de.kairenken.songs.domain.common.Created
import de.kairenken.songs.domain.common.CreationResult
import de.kairenken.songs.domain.common.InvalidArguments
import de.kairenken.songs.domain.common.Validator.Companion.validate
import de.kairenken.songs.domain.common.Validator.Error
import de.kairenken.songs.domain.common.Validator.Success
import de.kairenken.songs.domain.common.create
import java.util.UUID

private const val MAX_LENGTH = 500
private const val MAX_LYRICS_LENGTH = 20000

class Song(
    val id: Id,
    val name: Name,
    val artist: Artist,
    val lyrics: Lyrics
) {
    class Id private constructor(val value: UUID) {
        companion object {
            operator fun invoke(value: UUID = UUID.randomUUID()): CreationResult<Id> = Created(Id(value))
        }
    }

    class Name private constructor(val value: String) {
        companion object {
            operator fun invoke(value: String): CreationResult<Name> = when (val result = validate {
                require(value.isNotBlank(), "Song.Name must not be blank")
                require(value.length <= MAX_LENGTH, "Song.Name must not be longer than $MAX_LENGTH characters")
            }) {
                is Success -> Created(Name(value))
                is Error -> InvalidArguments(result.errors)
            }
        }
    }

    class Artist private constructor(val value: String) {
        companion object {
            operator fun invoke(value: String): CreationResult<Artist> = when (val result = validate {
                require(value.isNotBlank(), "Song.Artist must not be blank")
                require(value.length <= MAX_LENGTH, "Song.Artist must not be longer than $MAX_LENGTH characters")
            }) {
                is Success -> Created(Artist(value))
                is Error -> InvalidArguments(result.errors)
            }
        }
    }

    class Lyrics private constructor(val value: String) {
        companion object {
            operator fun invoke(value: String = ""): CreationResult<Lyrics> = when (val result = validate {
                require(
                    value.length <= MAX_LYRICS_LENGTH,
                    "Song.Lyrics must not be longer than $MAX_LYRICS_LENGTH characters"
                )
            }) {
                is Success -> Created(Lyrics(value))
                is Error -> InvalidArguments(result.errors)
            }
        }
    }

    companion object {
        operator fun invoke(
            name: String,
            artist: String
        ): CreationResult<Song> =
            create(listOf(Id(), Name(name), Artist(artist), Lyrics()), Song::class)

        operator fun invoke(
            name: String,
            artist: String,
            lyrics: String
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