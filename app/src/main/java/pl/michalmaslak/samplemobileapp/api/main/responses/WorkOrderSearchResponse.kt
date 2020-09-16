package pl.michalmaslak.samplemobileapp.api.main.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import pl.michalmaslak.samplemobileapp.models.WorkOrder
import pl.michalmaslak.samplemobileapp.util.DateUtils.Companion.convertServerStringDateToLong

class WorkOrderSearchResponse(

    @SerializedName("pk")
    @Expose
    var pk: Int,

    @SerializedName("title")
    @Expose
    var title: String,

    @SerializedName("description")
    @Expose
    var description: String,

    @SerializedName("status")
    @Expose
    var status: String,

    @SerializedName("created_by")
    @Expose
    var created_by: String,

    @SerializedName("created_at")
    @Expose
    var created_at: String,

    @SerializedName("updated_at")
    @Expose
    var updated_at: String,

    @SerializedName("assigned_to")
    @Expose
    var assigned_to: String

) {
    fun toWorkOrder(): WorkOrder{
        return WorkOrder(
            pk = pk,
            title = title,
            description = description,
            status = status,
            created_by = created_by,
            created_at = convertServerStringDateToLong(created_at),
            updated_at = convertServerStringDateToLong(updated_at),
            assigned_to = assigned_to
        )
    }


        override fun toString(): String {
        return "WorkOrderSearchResponse(pk=$pk, title='$title', description='$description',  status='$status'," +
                "  created_by='$created_by',  created_at='$created_at', updated_at='$updated_at', assigned_to='$assigned_to')"
    }
}