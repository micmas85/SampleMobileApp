package pl.michalmaslak.samplemobileapp.persistence

import pl.michalmaslak.samplemobileapp.models.WorkOrder
import pl.michalmaslak.samplemobileapp.persistence.WorkOrderQueryUtils.Companion.ORDER_BY_ASC_CREATED_AT
import pl.michalmaslak.samplemobileapp.persistence.WorkOrderQueryUtils.Companion.ORDER_BY_ASC_CREATED_BY
import pl.michalmaslak.samplemobileapp.persistence.WorkOrderQueryUtils.Companion.ORDER_BY_DESC_CREATED_AT
import pl.michalmaslak.samplemobileapp.persistence.WorkOrderQueryUtils.Companion.ORDER_BY_DESC_CREATED_BY

class WorkOrderQueryUtils {


    companion object{
        private val TAG: String = "AppDebug"

        // values
        const val WORK_ORDER_ORDER_ASC: String = ""
        const val WORK_ORDER_ORDER_DESC: String = "-"
        const val WORK_ORDER_FILTER_CREATED_BY = "created_by"
        const val WORK_ORDER_FILTER_CREATED_AT = "created_at"

        val ORDER_BY_ASC_CREATED_AT = WORK_ORDER_ORDER_ASC + WORK_ORDER_FILTER_CREATED_AT
        val ORDER_BY_DESC_CREATED_AT = WORK_ORDER_ORDER_DESC + WORK_ORDER_FILTER_CREATED_AT
        val ORDER_BY_ASC_CREATED_BY = WORK_ORDER_ORDER_ASC + WORK_ORDER_FILTER_CREATED_BY
        val ORDER_BY_DESC_CREATED_BY = WORK_ORDER_ORDER_DESC + WORK_ORDER_FILTER_CREATED_BY
    }
}


suspend fun WorkOrderDao.returnOrderedWorkOrderQuery(
    query: String,
    filterAndOrder: String,
    page: Int
): List<WorkOrder> {

    when{

        filterAndOrder.contains(ORDER_BY_DESC_CREATED_AT) ->{
            return searchWorkOrdersOrderByDateDESC(
                query = query,
                page = page)
        }

        filterAndOrder.contains(ORDER_BY_ASC_CREATED_AT) ->{
            return searchWorkOrdersOrderByDateASC(
                query = query,
                page = page)
        }

        filterAndOrder.contains(ORDER_BY_DESC_CREATED_BY) ->{
            return searchWorkOrdersOrderByCreatedByDESC(
                query = query,
                page = page)
        }

        filterAndOrder.contains(ORDER_BY_ASC_CREATED_BY) ->{
            return searchWorkOrdersOrderByCreatedByASC(
                query = query,
                page = page)
        }
        else ->
            return searchWorkOrdersOrderByDateDESC(
                query = query,
                page = page
            )
    }
}