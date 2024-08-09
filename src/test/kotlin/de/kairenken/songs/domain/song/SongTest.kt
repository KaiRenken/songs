package de.kairenken.songs.domain.song

import de.kairenken.songs.domain.common.Created
import de.kairenken.songs.domain.common.InvalidArguments
import de.kairenken.songs.domain.song.Song.Artist.Companion.MAX_ARTIST_LENGTH
import de.kairenken.songs.domain.song.Song.Lyrics.Companion.MAX_LYRICS_LENGTH
import de.kairenken.songs.domain.song.Song.Name.Companion.MAX_NAME_LENGTH
import io.kotest.matchers.shouldBe
import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class SongTest {

    @Nested
    inner class CreateSongAtCreation {

        @Test
        fun `successfully without lyrics`() {
            val result = Song(Song.Name("test-name"), Song.Artist("test-artist"))

            (result as Created).value.name.value shouldBe "test-name"
            result.value.artist.value shouldBe "test-artist"
            result.value.lyrics.value shouldBe ""
        }

        @Test
        fun `successfully with lyrics`() {
            val result = Song(Song.Name("test-name"), Song.Artist("test-artist"), Song.Lyrics("test-lyrics"))

            (result as Created).value.name.value shouldBe "test-name"
            result.value.artist.value shouldBe "test-artist"
            result.value.lyrics.value shouldBe "test-lyrics"
        }

        @ParameterizedTest
        @ValueSource(strings = ["", " "])
        fun `with blank values`(value: String) {
            val result = Song(Song.Name(value), Song.Artist(value))

            (result as InvalidArguments).errors[0] shouldBe "Song.Name must not be blank"
            result.errors[1] shouldBe "Song.Artist must not be blank"
            assertThat(result.errors).hasSize(2)
        }

        @Test
        fun `with too large values`() {
            val tooLargeNameValue = (0..500).joinToString("")
            val tooLargeArtistValue = (0..500).joinToString("")
            val tooLargeLyricsValue = (0..20000).joinToString("")

            val result =
                Song(Song.Name(tooLargeNameValue), Song.Artist(tooLargeArtistValue), Song.Lyrics(tooLargeLyricsValue))

            (result as InvalidArguments).errors[0] shouldBe "Song.Name must not be longer than $MAX_NAME_LENGTH characters"
            result.errors[1] shouldBe "Song.Artist must not be longer than $MAX_ARTIST_LENGTH characters"
            result.errors[2] shouldBe "Song.Lyrics must not be longer than $MAX_LYRICS_LENGTH characters"
            assertThat(result.errors).hasSize(3)
        }
    }

    @Nested
    inner class CreateExistingSong {

        val id = UUID.randomUUID()

        @Test
        fun successfully() {
            val result =
                Song(Song.Id(id), Song.Name("test-name"), Song.Artist("test-artist"), Song.Lyrics("test-lyrics"))

            (result as Created).value.id.value shouldBe id
            result.value.name.value shouldBe "test-name"
            result.value.artist.value shouldBe "test-artist"
            result.value.lyrics.value shouldBe "test-lyrics"
        }
    }
}