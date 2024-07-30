package de.kairenken.songs.infrastructure.song.rest

import de.kairenken.songs.application.song.InvalidSongArguments
import de.kairenken.songs.application.song.SongAlreadyExists
import de.kairenken.songs.application.song.SongCreated
import de.kairenken.songs.application.song.SongCreation
import de.kairenken.songs.application.song.SongCreationResult
import de.kairenken.songs.domain.song.Song
import de.kairenken.songs.infrastructure.song.common.rest.ErrorResponseDto
import de.kairenken.songs.infrastructure.song.rest.model.CreateSongDto
import de.kairenken.songs.infrastructure.song.rest.model.ReadSongDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/song")
class SongController(
    private val songCreation: SongCreation
) {

    @PostMapping(value = ["/"], produces = ["application/json"], consumes = ["application/json"])
    fun createSong(@RequestBody body: CreateSongDto): ResponseEntity<out Any> =
        body
            .callCreateUseCase()
            .wrapInResponse()

    private fun CreateSongDto.callCreateUseCase(): SongCreationResult = songCreation.createSong(
        name = this.name,
        artist = this.artist,
        lyrics = this.lyrics
    )

    private fun SongCreationResult.wrapInResponse(): ResponseEntity<out Any> = when (this) {
        is SongCreated -> this.song.toReadSongDto().wrapInCreatedResponse()
        is InvalidSongArguments -> ErrorResponseDto(messages = this.errors).wrapInBadRequestResponse()
        is SongAlreadyExists -> ErrorResponseDto(messages = listOf("This song already exists")).wrapInBadRequestResponse()
    }

    private fun Song.toReadSongDto() = ReadSongDto(
        id = this.id.value,
        name = this.name.value,
        artist = this.artist.value,
        lyrics = this.lyrics.value
    )

    private fun ReadSongDto.wrapInCreatedResponse(): ResponseEntity<ReadSongDto> =
        ResponseEntity(this, HttpStatus.CREATED)

    private fun ErrorResponseDto.wrapInBadRequestResponse(): ResponseEntity<ErrorResponseDto> =
        ResponseEntity(this, HttpStatus.BAD_REQUEST)
}
