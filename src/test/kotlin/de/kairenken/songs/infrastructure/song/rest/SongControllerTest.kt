package de.kairenken.songs.infrastructure.song.rest

import com.ninjasquad.springmockk.MockkBean
import de.kairenken.songs.application.song.InvalidSongArguments
import de.kairenken.songs.application.song.SongAlreadyExists
import de.kairenken.songs.application.song.SongCreated
import de.kairenken.songs.application.song.SongCreation
import de.kairenken.songs.domain.song.songArtistFixture
import de.kairenken.songs.domain.song.songFixture
import de.kairenken.songs.domain.song.songIdFixture
import de.kairenken.songs.domain.song.songLyricsFixture
import de.kairenken.songs.domain.song.songNameFixture
import io.mockk.every
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@WebMvcTest(value = [SongController::class])
class SongControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var songCreationMock: SongCreation

    @Nested
    @DisplayName("Create Song")
    inner class CreateSongTest {

        @Test
        fun successfully() {
            val requestBody = """
                {
                    "name": "${songNameFixture.value}",
                    "artist": "${songArtistFixture.value}",
                    "lyrics": "${songLyricsFixture.value}"
                }
            """.trimIndent()
            val expectedResponseBody = """
                {
                    "id": "${songIdFixture.value}",
                    "name": "${songNameFixture.value}",
                    "artist": "${songArtistFixture.value}",
                    "lyrics": "${songLyricsFixture.value}"
                }
            """.trimIndent()
            every {
                songCreationMock.createSong(
                    name = songNameFixture.value,
                    artist = songArtistFixture.value,
                    lyrics = songLyricsFixture.value
                )
            } returns SongCreated(songFixture)

            mockMvc.post("/api/song/") {
                content = requestBody
                contentType = MediaType.APPLICATION_JSON
            }.andExpect {
                status {
                    isCreated()
                }
                content {
                    json(jsonContent = expectedResponseBody, strict = true)
                }
            }
        }

        @Test
        fun `with invalid arguments`() {
            val requestBody = """
                {
                    "name": "${songNameFixture.value}",
                    "artist": "${songArtistFixture.value}",
                    "lyrics": "${songLyricsFixture.value}"
                }
            """.trimIndent()
            val expectedResponseBody = """
                {
                    "messages": [
                      "error1",
                      "error2"
                    ]
                }
            """.trimIndent()
            every {
                songCreationMock.createSong(
                    name = songNameFixture.value,
                    artist = songArtistFixture.value,
                    lyrics = songLyricsFixture.value
                )
            } returns InvalidSongArguments(listOf("error1", "error2"))

            mockMvc.post("/api/song/") {
                content = requestBody
                contentType = MediaType.APPLICATION_JSON
            }.andExpect {
                status {
                    isBadRequest()
                }
                content {
                    json(jsonContent = expectedResponseBody, strict = true)
                }
            }
        }

        @Test
        fun `when song already exists`() {
            val requestBody = """
                {
                    "name": "${songNameFixture.value}",
                    "artist": "${songArtistFixture.value}",
                    "lyrics": "${songLyricsFixture.value}"
                }
            """.trimIndent()
            val expectedResponseBody = """
                {
                    "messages": [
                      "This song already exists"
                    ]
                }
            """.trimIndent()
            every {
                songCreationMock.createSong(
                    name = songNameFixture.value,
                    artist = songArtistFixture.value,
                    lyrics = songLyricsFixture.value
                )
            } returns SongAlreadyExists

            mockMvc.post("/api/song/") {
                content = requestBody
                contentType = MediaType.APPLICATION_JSON
            }.andExpect {
                status {
                    isBadRequest()
                }
                content {
                    json(jsonContent = expectedResponseBody, strict = true)
                }
            }
        }
    }
}