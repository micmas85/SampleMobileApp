package pl.michalmaslak.samplemobileapp.api.main.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import pl.michalmaslak.samplemobileapp.models.WorkOrder

class WorkOrderListSearchResponse (

    @SerializedName("results")
    @Expose
    var results: List<WorkOrderSearchResponse>,

    @SerializedName("detail")
    @Expose
    var detail: String
    )
    {
        fun toList(): List<WorkOrder>{
            val workOrderList: ArrayList<WorkOrder> = ArrayList()
            for(workOrderResponse in results){
                workOrderList.add(
                    workOrderResponse.toWorkOrder()
                )
            }
            return workOrderList
        }
        
        
        override fun toString(): String {
            return "WorkOrderListSearchResponse(results=$results, detail='$detail')"
        }
    }