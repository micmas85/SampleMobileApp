package pl.michalmaslak.samplemobileapp.ui.auth.state

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import pl.michalmaslak.samplemobileapp.models.AuthToken

const val AUTH_VIEW_STATE_BUNDLE_KEY = "pl.michalmaslak.samplemobileapp.ui.auth.state.AuthViewState"

@Parcelize
data class AuthViewState(
    var registrationFields: RegistrationFields? = null,
    var loginFields: LoginFields? = null,
    var resetPasswordFields: ResetPasswordFields? = null,
    var authToken: AuthToken? = null,
    var resetPasswordResponse: String? = null
): Parcelable

@Parcelize
data class RegistrationFields(
    var registration_email: String? = null,
    var registration_username: String? = null,
    var registration_password: String? = null,
    var registration_confirm_password: String? = null
) : Parcelable {

    class RegistrationError {
        companion object{

            fun mustFillAllFields(): String{
                return "All fields are required."
            }

            fun passwordsDoNotMatch(): String{
                return "Passwords must match."
            }

            fun none():String{
                return "None"
            }

        }
    }

    fun isValidForRegistration(): String{
        if(registration_email.isNullOrEmpty()
            || registration_username.isNullOrEmpty()
            || registration_password.isNullOrEmpty()
            || registration_confirm_password.isNullOrEmpty()){
            return RegistrationError.mustFillAllFields()
        }

        if(!registration_password.equals(registration_confirm_password)){
            return RegistrationError.passwordsDoNotMatch()
        }
        return RegistrationError.none()
    }
}

@Parcelize
data class LoginFields(
    var login_email: String? = null,
    var login_password: String? = null
) : Parcelable {
    class LoginError {

        companion object {

            fun mustFillAllFields(): String {
                return "You can't login without an email and password."
            }

            fun none(): String {
                return "None"
            }

        }
    }

    fun isValidForLogin(): String {

        if (login_email.isNullOrEmpty()
            || login_password.isNullOrEmpty()
        ) {

            return LoginError.mustFillAllFields()
        }
        return LoginError.none()
    }

    override fun toString(): String {
        return "LoginState(email=$login_email, password=$login_password)"
    }
}

@Parcelize
data class ResetPasswordFields(
    var reset_password_email: String? = null
) : Parcelable {

    class ResetPasswordError {
        companion object{

            fun emailCannotBeEmpty(): String{
                return "Field email cannot be empty."
            }

            fun emailHasNoAtSign(): String{
                return "Field email require @ sign."
            }

            fun none():String{
                return "None"
            }

        }
    }

    fun isValidForResetPassword(): String{
        if(reset_password_email.isNullOrEmpty())
          {
            return ResetPasswordError.emailCannotBeEmpty()
        }

        if(!reset_password_email!!.contains("@")){
            return ResetPasswordError.emailHasNoAtSign()
        }

        return ResetPasswordError.none()
    }
}


