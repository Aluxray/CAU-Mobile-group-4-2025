package com.example.yarni.ui.register.ui.register

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.yarni.R
import com.example.yarni.ui.register.data.RegisterRepository
import com.example.yarni.ui.register.data.Result

class RegisterViewModel(private val registerRepository: RegisterRepository) : ViewModel() {

    private val _registerForm = MutableLiveData<RegisterFormState>()
    val registerFormState: LiveData<RegisterFormState> = _registerForm

    private val _registerResult = MutableLiveData<RegisterResult>()
    val registerResult: LiveData<RegisterResult> = _registerResult

    fun register(email: String, password: String) {
        // can be launched in a separate asynchronous job
        val result = registerRepository.register(email, password)

        if (result is Result.Success) {
            _registerResult.value =
                RegisterResult(success = RegisterInUserView(displayName = result.data.displayName))
        } else {
            _registerResult.value = RegisterResult(error = R.string.register_failed)
        }
    }

    fun registerDataChanged(email: String, password: String, confirmPassword: String) {
        if (!isUserNameValid(email)) {
            _registerForm.value = RegisterFormState(emailError = R.string.invalid_email)
        } else if (!isPasswordValid(password)) {
            _registerForm.value = RegisterFormState(passwordError = R.string.invalid_password)
        } else if (password.compareTo(confirmPassword) != 0) {
            _registerForm.value = RegisterFormState(passwordError = R.string.passwords_dont_match)
        } else {
            _registerForm.value = RegisterFormState(isDataValid = true)
        }
    }

    private fun isUserNameValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        return Regex
            .fromLiteral("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&_-])[A-Za-z\\d@$!%*?&_-]{8,}$")
            .matches(password)
    }
}