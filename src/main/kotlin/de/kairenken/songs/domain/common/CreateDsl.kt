package de.kairenken.songs.domain.common

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class CreateDsl<TypeToCreate, ResultType> {
    private val errors: MutableList<String> = ArrayList()

    private var creationResultAtSuccess: TypeToCreate? = null
    private var checkResultAtFailure: ResultType? = null

    fun create(result: CreationResult<TypeToCreate>) {
        when (result) {
            is Created -> creationResultAtSuccess = result.value
            is InvalidArguments -> {
                errors.addAll(result.errors)
            }
        }
    }

    fun checkAfterCreation(resultAtFailure: ResultType, block: (TypeToCreate) -> Boolean) {
        when (creationResultAtSuccess) {
            null -> {}
            else -> {
                if ((block(creationResultAtSuccess!!))) checkResultAtFailure = resultAtFailure
            }
        }
    }

    fun doFinally(block: (TypeToCreate) -> Unit) {
        if (creationResultAtSuccess != null && checkResultAtFailure == null) block(creationResultAtSuccess!!)
    }

    companion object {
        operator fun <TypeToCreate : Any, ResultType : Any, SuccessType : ResultType, InvalidArgumentsType : ResultType> invoke(
            typeToCreate: KClass<TypeToCreate>,
            resultType: KClass<ResultType>,
            successType: KClass<SuccessType>,
            invalidArgumentsType: KClass<InvalidArgumentsType>,
            block: CreateDsl<TypeToCreate, ResultType>.() -> Unit
        ): ResultType {
            val createDsl = CreateDsl<TypeToCreate, ResultType>()

            createDsl.apply(block)

            if (createDsl.creationResultAtSuccess == null) return invalidArgumentsType.primaryConstructor!!.call(
                createDsl.errors.toList()
            )

            if (createDsl.checkResultAtFailure != null) return createDsl.checkResultAtFailure!!

            return successType.primaryConstructor!!.call(createDsl.creationResultAtSuccess)
        }
    }
}