package de.kairenken.songs

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SongsApplication

fun main(args: Array<String>) {
    runApplication<SongsApplication>(*args)
}
