package de.kairenken.songs.testutils.testcontainers

import de.kairenken.songs.infrastructure.song.repository.SongJpaRepository
import de.kairenken.songs.infrastructure.song.repository.SongRepositoryImpl
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Transactional(propagation = Propagation.NEVER)
@DataJpaTest
@ActiveProfiles("database")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = [PostgresContextInitializer::class])
@Import(
    value = [
        SongRepositoryImpl::class
    ]
)
abstract class AbstractDatabaseTest {

    @Autowired
    protected lateinit var songJpaRepository: SongJpaRepository

    @Autowired
    protected lateinit var songRepositoryImpl: SongRepositoryImpl

    @BeforeEach
    fun setUp() {
        songJpaRepository.deleteAll()
    }
}