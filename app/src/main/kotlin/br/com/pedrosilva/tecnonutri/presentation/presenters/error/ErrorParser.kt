package br.com.pedrosilva.tecnonutri.presentation.presenters.error

internal open class ErrorParser {
    // TODO handle many error types
    fun parse(error: Throwable) = ErrorData("Unexpected error")
}