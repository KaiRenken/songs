package de.kairenken.songs.infrastructure.song.repository

import de.kairenken.songs.domain.song.songArtistFixture
import de.kairenken.songs.domain.song.songFixture
import de.kairenken.songs.domain.song.songNameFixture
import de.kairenken.songs.testutils.testcontainers.AbstractDatabaseTest
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class SongRepositoryImplTest : AbstractDatabaseTest() {

    @Nested
    @DisplayName("Store song")
    inner class StoreSongTest {

        @Test
        fun successfully() {
            songRepositoryImpl.store(songFixture)

            val songsInDb = songJpaRepository.findAll()
            songsInDb shouldHaveSize 1
            songsInDb[0] shouldBeEqualToComparingFields songEntityFixture
        }
    }

    @Nested
    @DisplayName("Check if song exists")
    inner class ExistsSongTest {

        @Test
        fun successfully() {
            songJpaRepository.save(songEntityFixture)

            songRepositoryImpl.exists(songNameFixture, songArtistFixture) shouldBe true
        }

        @Test
        fun `with no song found`() {
            songRepositoryImpl.exists(songNameFixture, songArtistFixture) shouldBe false
        }
    }
}