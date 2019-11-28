package com.example.andrewapp.authentication

data class FirebaseAuthErrorWrapper(var error: FirebaseAuthError) {
    override fun toString(): String {
        return error.toString()
    }

    data class FirebaseAuthError(
        var code: Int = 0,
        var message: String = "",
        var errors: List<FirebaseAuthMultipleErrors>
    ) {
        override fun toString(): String {
            return message
        }
    }

    data class FirebaseAuthMultipleErrors(
        var domain: String = "",
        var reason: String = "",
        var message: String = ""
    ) {
        override fun toString(): String {
            return message
        }

    }
}
