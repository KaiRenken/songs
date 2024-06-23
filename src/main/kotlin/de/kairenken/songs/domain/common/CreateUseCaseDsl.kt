package de.kairenken.songs.domain.common

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class CreateUseCaseDsl<T> private constructor(
    private val errors: MutableList<String> = ArrayList()
) {
    private var creationResult: T? = null
    private var checkResult: Any? = null

    fun create(result: CreationResult<T>) {
        when (result) {
            is Created -> creationResult = result.value
            is InvalidArguments -> {
                errors.addAll(result.errors)
            }
        }
    }

    fun checkAfterCreation(result: Any, block: (T) -> Boolean) {
        when (creationResult) {
            null -> {}
            else -> {
                if ((block(creationResult!!))) checkResult = result
            }
        }
    }

    fun doFinally(block: (T) -> Unit) {
        if (creationResult != null && checkResult == null) block(creationResult!!)
    }

    companion object {
        fun <T : Any, U : Any, E : Any> createUseCase(
            returnAtSuccess: KClass<U>,
            returnAtCreationFailure: KClass<E>,
            block: CreateUseCaseDsl<T>.() -> Unit
        ): Any {

            val createUseCaseDsl = CreateUseCaseDsl<T>()
            createUseCaseDsl.apply(block)

            if (createUseCaseDsl.creationResult == null) return returnAtCreationFailure.primaryConstructor!!.call(
                createUseCaseDsl.errors.toList()
            )
            if (createUseCaseDsl.checkResult != null) return createUseCaseDsl.checkResult!!

            return returnAtSuccess.primaryConstructor!!.call(createUseCaseDsl.creationResult)
        }
    }
}