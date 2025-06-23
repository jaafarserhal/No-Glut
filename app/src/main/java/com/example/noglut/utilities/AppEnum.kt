package com.example.noglut.utilities

enum class HttpStatusCode(val code: Int) {
    OK(200),
    BadRequest(400),
    Unauthorized(401),
    Forbidden(403),
    NotFound(404),
    Conflict(409),
    InternalServerError(500);

    companion object {
        fun fromCode(code: Int): HttpStatusCode? {
            return entries.find { it.code == code }
        }
    }
}