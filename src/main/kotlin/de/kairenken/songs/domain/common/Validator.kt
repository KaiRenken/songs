package de.kairenken.songs.domain.common

class Validator private constructor(
    private val errors: MutableList<String> = ArrayList(),
) {
    fun require(
        checker: Boolean,
        message: String,
    ) {
        if (!checker) errors.add(message)
    }

    companion object {
        fun validate(block: Validator.() -> Unit): ValidationResult {

            val validationUtil = Validator()
            validationUtil.apply(block)

            if (validationUtil.errors.isEmpty()) return Success

            return Error(errors = validationUtil.errors.toList())
        }
    }

    sealed class ValidationResult
    data object Success : ValidationResult()
    class Error(val errors: List<String>) : ValidationResult()
}