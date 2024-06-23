package de.kairenken.songs.application.song

import de.kairenken.songs.domain.song.Song
import de.kairenken.songs.domain.song.SongRepository
import de.kairenken.songs.domain.song.songArtistFixture
import de.kairenken.songs.domain.song.songFixture
import de.kairenken.songs.domain.song.songLyricsFixture
import de.kairenken.songs.domain.song.songNameFixture
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Test

class SongCreationTest {

    private val repositoryMock = mockk<SongRepository>()

    private val songCreationToTest = SongCreation(repositoryMock)

    @Test
    fun `create song with lyrics successfully`() {
        every { repositoryMock.exists(songNameFixture, songArtistFixture) } returns false
        every { repositoryMock.store(any<Song>()) } just runs

        val result = songCreationToTest.createSong(
            songNameFixture.value,
            songArtistFixture.value,
            songLyricsFixture.value
        )

        val createdSong = result.shouldBeInstanceOf<SongCreated>().song

        createdSong shouldBe songFixture.copy(id = createdSong.id)
        verify(exactly = 1) {
            repositoryMock.store(
                songFixture.copy(
                    id = createdSong.id
                )
            )
        }
    }

    @Test
    fun `create song without lyrics successfully`() {
        every { repositoryMock.exists(songNameFixture, songArtistFixture) } returns false
        every { repositoryMock.store(any<Song>()) } just runs

        val result = songCreationToTest.createSong(
            songNameFixture.value,
            songArtistFixture.value
        )

        val createdSong = result.shouldBeInstanceOf<SongCreated>().song
        createdSong shouldBe songFixture.copy(
            id = createdSong.id,
            lyrics = createdSong.lyrics
        )
        verify(exactly = 1) {
            repositoryMock.store(
                songFixture.copy(
                    id = createdSong.id,
                    lyrics = createdSong.lyrics
                )
            )
        }
    }

    @Test
    fun `create song with invalid arguments`() {
        val result = songCreationToTest.createSong(
            "",
            " "
        )

        val errors = result.shouldBeInstanceOf<InvalidSongArguments>().errors
        errors shouldHaveSize 2
        errors[0] shouldBe "Song.Name must not be blank"
        errors[1] shouldBe "Song.Artist must not be blank"
        verify(exactly = 0) { repositoryMock.store(any()) }
    }

    @Test
    fun `create duplicated song`() {
        every { repositoryMock.exists(songNameFixture, songArtistFixture) } returns true

        songCreationToTest.createSong(
            songNameFixture.value,
            songArtistFixture.value
        )
            .shouldBeInstanceOf<SongAlreadyExists>()
        verify(exactly = 0) { repositoryMock.store(any()) }
    }
}