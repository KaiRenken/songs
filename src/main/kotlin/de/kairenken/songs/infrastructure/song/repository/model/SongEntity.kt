package de.kairenken.songs.infrastructure.song.repository.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "song")
class SongEntity(

    @Id
    var id: UUID,

    @Column(name = "name")
    var name: String,

    @Column(name = "artist")
    var artist: String,

    @Column(name = "lyrics")
    var lyrics: String
)