package pl.michalmaslak.samplemobileapp.repository.auth

import android.content.SharedPreferences
import android.util.Log
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pl.michalmaslak.samplemobileapp.api.auth.SampleApiAuthService
import pl.michalmaslak.samplemobileapp.api.auth.network_responses.LoginResponse
import pl.michalmaslak.samplemobileapp.api.auth.network_responses.RegistrationResponse
import pl.michalmaslak.samplemobileapp.api.auth.network_responses.ResetPasswordResponse
import pl.michalmaslak.samplemobileapp.di.auth.AuthScope
import pl.michalmaslak.samplemobileapp.models.AccountProperties
import pl.michalmaslak.samplemobileapp.models.AuthToken
import pl.michalmaslak.samplemobileapp.persistence.AccountPropertiesDao
import pl.michalmaslak.samplemobileapp.persistence.AuthTokenDao
import pl.michalmaslak.samplemobileapp.repository.buildError
import pl.michalmaslak.samplemobileapp.repository.safeApiCall
import pl.michalmaslak.samplemobileapp.repository.safeCacheCall
import pl.michalmaslak.samplemobileapp.session.SessionManager
import pl.michalmaslak.samplemobileapp.ui.auth.state.AuthViewState
import pl.michalmaslak.samplemobileapp.ui.auth.state.LoginFields
import pl.michalmaslak.samplemobileapp.ui.auth.state.RegistrationFields
import pl.michalmaslak.samplemobileapp.ui.auth.state.ResetPasswordFields
import pl.michalmaslak.samplemobileapp.util.*
import pl.michalmaslak.samplemobileapp.util.ErrorHandling.Companion.ERROR_SAVE_ACCOUNT_PROPERTIES
import pl.michalmaslak.samplemobileapp.util.ErrorHandling.Companion.ERROR_SAVE_AUTH_TOKEN
import pl.michalmaslak.samplemobileapp.util.ErrorHandling.Companion.GENERIC_AUTH_ERROR
import pl.michalmaslak.samplemobileapp.util.ErrorHandling.Companion.INVALID_CREDENTIALS
import pl.michalmaslak.samplemobileapp.util.SuccessHandling.Companion.RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE
import javax.inject.Inject

@FlowPreview
@AuthScope
class AuthRepositoryImpl
@Inject
constructor(
    val authTokenDao: AuthTokenDao,
    val accountPropertiesDao: AccountPropertiesDao,
    val sampleApiAuthService: SampleApiAuthService,
    val sessionManager: SessionManager,
    val sharedPreferences: SharedPreferences,
    val sharedPrefsEditor: SharedPreferences.Editor
): AuthRepository
{
    private val TAG: String = "AppDebug"

    override fun attemptLogin(
        stateEvent: StateEvent,
        email: String,
        password: String
    ): Flow <DataState<AuthViewState>> = flow {

        val loginFieldErrors = LoginFields(email, password).isValidForLogin()
        if(loginFieldErrors.equals(LoginFields.LoginError.none())){
            val apiResult = safeApiCall(IO){
                sampleApiAuthService.login(email, password)
            }
            emit(
                object: ApiResponseHandler<AuthViewState, LoginResponse>(
                    response = apiResult,
                    stateEvent = stateEvent
                ) {
                    override suspend fun handleSuccess(resultObj: LoginResponse): DataState<AuthViewState> {

                        Log.d(TAG, "handleSuccess ")

                        // Incorrect login credentials counts as a 200 response from server, so need to handle that
                        if(resultObj.response.equals(GENERIC_AUTH_ERROR)){
                            return DataState.error(
                                response = Response(
                                    INVALID_CREDENTIALS,
                                    UIComponentType.Dialog(),
                                    MessageType.Error()
                                ),
                                stateEvent = stateEvent
                            )
                        }
                        accountPropertiesDao.insertOrIgnore(
                            AccountProperties(
                                resultObj.pk,
                                resultObj.email,
                                ""
                            )
                        )

                        // will return -1 if failure
                        val authToken = AuthToken(
                            resultObj.pk,
                            resultObj.token
                        )
                        val result = authTokenDao.insert(authToken)
                        if(result < 0){
                            return DataState.error(
                                response = Response(
                                    ERROR_SAVE_AUTH_TOKEN,
                                    UIComponentType.Dialog(),
                                    MessageType.Error()
                                ),
                                stateEvent = stateEvent
                            )
                        }
                        saveAuthenticatedUserToPrefs(email)

                        return DataState.data(
                            data = AuthViewState(
                                authToken = authToken
                            ),
                            stateEvent = stateEvent,
                            response = null
                        )
                    }

                }.getResult()
            )
        }
        else{
            Log.d(TAG, "emitting error: ${loginFieldErrors}")
            emit(
                buildError(
                    loginFieldErrors,
                    UIComponentType.Dialog(),
                    stateEvent
                )
            )
        }
    }

    override fun attemptRegistration(
        stateEvent: StateEvent,
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): Flow<DataState<AuthViewState>> = flow {
        val registrationFieldErrors = RegistrationFields(email, username, password, confirmPassword).isValidForRegistration()
        if(registrationFieldErrors.equals(RegistrationFields.RegistrationError.none())){

            val apiResult = safeApiCall(IO){
                sampleApiAuthService.register(
                    email,
                    username,
                    password,
                    confirmPassword
                )
            }
            emit(
                object: ApiResponseHandler<AuthViewState, RegistrationResponse>(
                    response = apiResult,
                    stateEvent = stateEvent
                ){
                    override suspend fun handleSuccess(resultObj: RegistrationResponse): DataState<AuthViewState> {
                        if(resultObj.response.equals(GENERIC_AUTH_ERROR)){
                            return DataState.error(
                                response = Response(
                                    resultObj.errorMessage,
                                    UIComponentType.Dialog(),
                                    MessageType.Error()
                                ),
                                stateEvent = stateEvent
                            )
                        }
                        val result1 = accountPropertiesDao.insertOrReplace(
                            AccountProperties(
                                resultObj.pk,
                                resultObj.email,
                                resultObj.username
                            )
                        )
                        // will return -1 if failure
                        if(result1 < 0){
                            return DataState.error(
                                response = Response(
                                    ERROR_SAVE_ACCOUNT_PROPERTIES,
                                    UIComponentType.Dialog(),
                                    MessageType.Error()
                                ),
                                stateEvent = stateEvent
                            )
                        }

                        // will return -1 if failure
                        val authToken = AuthToken(
                            resultObj.pk,
                            resultObj.token
                        )
                        val result2 = authTokenDao.insert(authToken)
                        if(result2 < 0){
                            return DataState.error(
                                response = Response(
                                    ERROR_SAVE_AUTH_TOKEN,
                                    UIComponentType.Dialog(),
                                    MessageType.Error()
                                ),
                                stateEvent = stateEvent
                            )
                        }
                        saveAuthenticatedUserToPrefs(email)
                        return DataState.data(
                            data = AuthViewState(
                                authToken = authToken
                            ),
                            stateEvent = stateEvent,
                            response = null
                        )
                    }
                }.getResult()
            )

        }
        else{
            emit(
                buildError(
                    registrationFieldErrors,
                    UIComponentType.Dialog(),
                    stateEvent
                )
            )
        }

    }

    override fun checkPreviousAuthUser(
        stateEvent: StateEvent
    ): Flow<DataState<AuthViewState>> = flow {
        Log.d(TAG, "checkPreviousAuthUser: ")
        val previousAuthUserEmail: String? = sharedPreferences.getString(PreferenceKeys.PREVIOUS_AUTH_USER, null)

        if(previousAuthUserEmail.isNullOrBlank()){
            Log.d(TAG, "checkPreviousAuthUser: No previously authenticated user found.")
            emit(returnNoTokenFound(stateEvent))
        }
        else{
            val apiResult = safeCacheCall(IO){
                accountPropertiesDao.searchByEmail(previousAuthUserEmail)
            }
            emit(
                object: CacheResponseHandler<AuthViewState, AccountProperties>(
                    response = apiResult,
                    stateEvent = stateEvent
                ){
                    override suspend fun handleSuccess(resultObj: AccountProperties): DataState<AuthViewState> {

                        if(resultObj.pk > -1){
                            authTokenDao.searchByPk(resultObj.pk).let { authToken ->
                                if(authToken != null){
                                    if(authToken.token != null){
                                        return DataState.data(
                                            data = AuthViewState(
                                                authToken = authToken
                                            ),
                                            response = null,
                                            stateEvent = stateEvent
                                        )
                                    }
                                }
                            }
                        }
                        Log.d(TAG, "createCacheRequestAndReturn: AuthToken not found...")
                        return DataState.error(
                            response = Response(
                                RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE,
                                UIComponentType.None(),
                                MessageType.Error()
                            ),
                            stateEvent = stateEvent
                        )
                    }
                }.getResult()
            )
        }
    }

    override fun saveAuthenticatedUserToPrefs(email: String) {
        sharedPrefsEditor.putString(PreferenceKeys.PREVIOUS_AUTH_USER, email)
        sharedPrefsEditor.apply()
    }

    override fun returnNoTokenFound(
        stateEvent: StateEvent
    ): DataState<AuthViewState> {

        return DataState.error(
            response = Response(
                RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE,
                UIComponentType.None(),
                MessageType.Error()
            ),
            stateEvent = stateEvent
        )
    }


    override fun attemptResetPassword(
        stateEvent: StateEvent,
        email: String
    ): Flow<DataState<AuthViewState>> = flow{

        val resetPasswordFieldsErrors = ResetPasswordFields(email).isValidForResetPassword()
        if(resetPasswordFieldsErrors.equals(ResetPasswordFields.ResetPasswordError.none())){
            val apiResult = safeApiCall(IO){
                sampleApiAuthService.resetPassword(email)
            }
            emit(
                object: ApiResponseHandler<AuthViewState, ResetPasswordResponse>(
                    response = apiResult,
                    stateEvent = stateEvent
                ){
                    override suspend fun handleSuccess(resultObj: ResetPasswordResponse): DataState<AuthViewState> {
                        if(resultObj.response.equals(GENERIC_AUTH_ERROR)){
                            return DataState.error(
                                response = Response(
                                    resultObj.errorMessage,
                                    UIComponentType.Dialog(),
                                    MessageType.Error()
                                ),
                                stateEvent = stateEvent
                            )
                        }
                        else{
                            return DataState.data(
                                data = AuthViewState(
                                    resetPasswordResponse = resultObj.response.toString()
                                ),
                                response = null,
                                stateEvent = stateEvent
                            )
                        }
                    }
                }.getResult()
            )

        }

   }

}