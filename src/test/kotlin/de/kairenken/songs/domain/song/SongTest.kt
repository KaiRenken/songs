package de.kairenken.songs.domain.song

import de.kairenken.songs.domain.common.Created
import de.kairenken.songs.domain.common.InvalidArguments
import io.kotest.matchers.shouldBe
import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class SongTest {

    @Nested
    inner class CreateSongAtCreationWithoutLyrics {
        @Test
        fun successfully() {
            val result = Song("test-name", "test-artist")

            (result as Created).value.name.value shouldBe "test-name"
            result.value.artist.value shouldBe "test-artist"
            result.value.lyrics.value shouldBe ""
        }

        @ParameterizedTest
        @ValueSource(strings = ["", " "])
        fun `with blank values`(value: String) {
            val result = Song(value, value)

            (result as InvalidArguments).errors[0] shouldBe "Song.Name must not be blank"
            result.errors[1] shouldBe "Song.Artist must not be blank"
            assertThat(result.errors).hasSize(2)
        }
    }

    @Nested
    inner class CreateSongAtCreationWithLyrics {
        @Test
        fun successfully() {
            val result = Song("test-name", "test-artist", "test-lyrics")

            (result as Created).value.name.value shouldBe "test-name"
            result.value.artist.value shouldBe "test-artist"
            result.value.lyrics.value shouldBe "test-lyrics"
        }

        @ParameterizedTest
        @ValueSource(strings = ["", " "])
        fun `with blank values`(value: String) {
            val result = Song(value, value, value)

            (result as InvalidArguments).errors[0] shouldBe "Song.Name must not be blank"
            result.errors[1] shouldBe "Song.Artist must not be blank"
            assertThat(result.errors).hasSize(2)
        }
    }

    @Nested
    inner class CreateExistingSong {
        @Test
        fun successfully() {
            val id = UUID.randomUUID()
            val result = Song(id, "test-name", "test-artist", "test-lyrics")

            (result as Created).value.id.value shouldBe id
            result.value.name.value shouldBe "test-name"
            result.value.artist.value shouldBe "test-artist"
            result.value.lyrics.value shouldBe "test-lyrics"
        }

        @ParameterizedTest
        @ValueSource(strings = ["", " "])
        fun `with blank values`(value: String) {
            val result = Song(UUID.randomUUID(), value, value, value)

            (result as InvalidArguments).errors[0] shouldBe "Song.Name must not be blank"
            result.errors[1] shouldBe "Song.Artist must not be blank"
            assertThat(result.errors).hasSize(2)
        }
    }
}