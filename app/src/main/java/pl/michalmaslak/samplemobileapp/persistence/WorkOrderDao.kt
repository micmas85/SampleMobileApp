package pl.michalmaslak.samplemobileapp.persistence

import androidx.room.*
import pl.michalmaslak.samplemobileapp.models.WorkOrder
import pl.michalmaslak.samplemobileapp.util.Constants.Companion.PAGINATION_PAGE_SIZE

@Dao
interface WorkOrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(workOrder: WorkOrder): Long

    @Query("""
        SELECT * FROM work_order WHERE title LIKE '%' || :query || '%'
        OR description LIKE '%' || :query || '%'
        OR created_by LIKE '%' || :query || '%'
        LIMIT (:page * :pageSize)
    """)
    suspend fun getAllWorkOrders(
        query: String,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): List<WorkOrder>

    @Delete
    suspend fun deleteWorkOrder(workOrder: WorkOrder)

    @Query("DELETE FROM work_order")
    suspend fun deleteAllWorkOrders()

    @Query("""
        UPDATE work_order SET status = :status
        WHERE pk = :pk
        """)
    suspend fun updateWorkOrder(pk: Int, status: String)

    @Query("""
        SELECT * FROM work_order 
        WHERE title LIKE '%' || :query || '%' 
        OR description LIKE '%' || :query || '%' 
        OR created_by LIKE '%' || :query || '%' 
        ORDER BY created_at DESC LIMIT (:page * :pageSize)
        """)
    suspend fun searchWorkOrdersOrderByDateDESC(
        query: String,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): List<WorkOrder>

    @Query("""
        SELECT * FROM work_order 
        WHERE title LIKE '%' || :query || '%' 
        OR description LIKE '%' || :query || '%' 
        OR created_by LIKE '%' || :query || '%' 
        ORDER BY created_at  ASC LIMIT (:page * :pageSize)""")
    suspend fun searchWorkOrdersOrderByDateASC(
        query: String,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): List<WorkOrder>

    @Query("""
        SELECT * FROM work_order 
        WHERE title LIKE '%' || :query || '%' 
        OR description LIKE '%' || :query || '%' 
        OR created_by LIKE '%' || :query || '%' 
        ORDER BY created_by DESC LIMIT (:page * :pageSize)""")
    suspend fun searchWorkOrdersOrderByCreatedByDESC(
        query: String,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): List<WorkOrder>

    @Query("""
        SELECT * FROM work_order 
        WHERE title LIKE '%' || :query || '%' 
        OR description LIKE '%' || :query || '%' 
        OR created_by LIKE '%' || :query || '%' 
        ORDER BY created_by  ASC LIMIT (:page * :pageSize)
        """)
    suspend fun searchWorkOrdersOrderByCreatedByASC(
        query: String,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): List<WorkOrder>


}