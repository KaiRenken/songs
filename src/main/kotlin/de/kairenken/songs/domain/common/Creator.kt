package de.kairenken.songs.domain.common

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

/**
 *
 */
fun <T : Any> create(arguments: List<CreationResult<Any>>, classToInstantiate: KClass<T>): CreationResult<T> {
    val errors = mutableListOf<String>()

    arguments.forEach {
        when (it) {
            is Created -> {}
            is InvalidArguments -> errors.addAll(it.errors)
        }
    }

    return if (errors.isEmpty()) {
        Created(classToInstantiate.primaryConstructor!!.call(*(arguments.map { (it as Created).value }.toTypedArray())))
    } else {
        InvalidArguments(errors)
    }
}

sealed class CreationResult<out T>

class Created<T>(val value: T) : CreationResult<T>()

class InvalidArguments<T>(val errors: List<String>) : CreationResult<T>()
