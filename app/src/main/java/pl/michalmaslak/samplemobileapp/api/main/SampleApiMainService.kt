package pl.michalmaslak.samplemobileapp.api.main

import pl.michalmaslak.samplemobileapp.api.GenericResponse
import pl.michalmaslak.samplemobileapp.api.main.responses.WorkOrderCreateUpdateResponse
import pl.michalmaslak.samplemobileapp.api.main.responses.WorkOrderListSearchResponse
import pl.michalmaslak.samplemobileapp.di.main.MainScope
import pl.michalmaslak.samplemobileapp.models.AccountProperties
import retrofit2.http.*

@MainScope
interface SampleApiMainService {

    @GET("properties")
    suspend fun getAccountProperties(
        @Header("Authorization") authorization: String
    ): AccountProperties

    @PUT("properties/update")
    @FormUrlEncoded
    suspend fun saveAccountProperties(
        @Header("Authorization") authorization: String,
        @Field("email") email: String,
        @Field("username") username: String
        ): GenericResponse

    @PUT("change_password/")
    @FormUrlEncoded
    suspend fun updatePassword(
        @Header("Authorization") authorization: String,
        @Field("old_password") currentPassword: String,
        @Field("new_password") newPassword: String,
        @Field("confirm_new_password") confirmNewPassword: String
        ): GenericResponse

    @GET("work_order_list")
    suspend fun searchListWorkOrders(
        @Header("Authorization") authorization: String,
        @Query("search") query: String,
        @Query("ordering") ordering: String,
        @Query("page") page: Int
        ): WorkOrderListSearchResponse

    @GET("is_creator_of_work_order/{pk}")
    suspend fun isCreatorOfWorkOrder(
        @Header("Authorization") authorization: String,
        @Path("pk") pk: Int
        ): GenericResponse

    @DELETE("work_order/delete/{pk}")
    suspend fun deleteWorkOrder(
        @Header("Authorization") authorization: String,
        @Path("pk") pk: Int
    ): GenericResponse

    @PUT("work_order/update/{pk}")
    @FormUrlEncoded
    suspend fun updateWorkOrder(
        @Header("Authorization") authorization: String,
        @Path("pk") pk: Int,
        @Field("status") status: String
        ): WorkOrderCreateUpdateResponse

    @POST("work_order/create")
    @FormUrlEncoded
    suspend fun createWorkOrder(
        @Header("Authorization") authorization: String,
        @Field("title") title: String,
        @Field("description") description: String
        ): WorkOrderCreateUpdateResponse
}