package pl.michalmaslak.samplemobileapp.api.auth.network_responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResetPasswordResponse(

    @SerializedName("response")
    @Expose
    var response: String,

    @SerializedName("error_message")
    @Expose
    var errorMessage: String?
)