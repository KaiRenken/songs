package de.kairenken.songs

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
    fromApplication<SongsApplication>().with(TestcontainersConfiguration::class).run(*args)
}
