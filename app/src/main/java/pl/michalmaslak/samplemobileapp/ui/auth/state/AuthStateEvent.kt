package pl.michalmaslak.samplemobileapp.ui.auth.state

import pl.michalmaslak.samplemobileapp.util.StateEvent

sealed class AuthStateEvent: StateEvent {

    data class LoginAttemptEvent(
        val email: String,
        val password: String
    ): AuthStateEvent() {

        override fun errorInfo(): String {
            return "Login attempt failed."
        }

        override fun toString(): String {
            return "LoginStateEvent"
        }
    }

    data class RegisterAttemptEvent(
        val email:String,
        val username:String,
        val password:String,
        val confirm_password:String
    ): AuthStateEvent() {
        override fun errorInfo(): String {
            return "Register attempt failed."
        }

        override fun toString(): String {
            return "RegisterAttemptEvent"
        }
    }

    data class ResetPasswordAttemptEvent(
        val email:String
    ): AuthStateEvent() {
        override fun errorInfo(): String {
            return "Reset password attempt failed."
        }
        override fun toString(): String {
            return "ResetPasswordAttemptEvent"
        }
    }

    class CheckPreviousAuthEvent: AuthStateEvent() {

        override fun errorInfo(): String {
            return "Error checking for previous authenticated user."
        }

        override fun toString(): String {
            return "CheckPreviousAuthEvent"
        }
    }

    class None: AuthStateEvent() {
        override fun errorInfo(): String {
            return "None."
        }
    }
}