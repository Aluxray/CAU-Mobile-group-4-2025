package com.example.yarni.ui.register.ui.register

/**
 * Authentication result : success (user details) or error message.
 */
data class RegisterResult(
    val success: RegisterInUserView? = null,
    val error: Int? = null
)