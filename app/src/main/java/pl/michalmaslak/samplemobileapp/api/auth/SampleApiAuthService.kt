package pl.michalmaslak.samplemobileapp.api.auth

import pl.michalmaslak.samplemobileapp.api.auth.network_responses.LoginResponse
import pl.michalmaslak.samplemobileapp.api.auth.network_responses.RegistrationResponse
import pl.michalmaslak.samplemobileapp.api.auth.network_responses.ResetPasswordResponse
import pl.michalmaslak.samplemobileapp.di.auth.AuthScope
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

@AuthScope
interface SampleApiAuthService {
    @POST("login")
    @FormUrlEncoded
    suspend fun login(
        @Field("username") email: String,
        @Field("password") password: String
    ): LoginResponse

    @POST("register")
    @FormUrlEncoded
    suspend fun register(
        @Field("email") email: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("password2") password2: String
    ): RegistrationResponse

    @POST("password_reset_request")
    @FormUrlEncoded
    suspend fun resetPassword(
        @Field("email") email: String
    ): ResetPasswordResponse
}