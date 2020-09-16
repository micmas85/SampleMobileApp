package pl.michalmaslak.samplemobileapp.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "work_order")
data class WorkOrder (

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "pk")
    var pk: Int,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "status")
    var status: String,

    @ColumnInfo(name = "created_by")
    var created_by: String,

    @ColumnInfo(name = "created_at")
    var created_at: Long,

    @ColumnInfo(name = "updated_at")
    var updated_at: Long,

    @ColumnInfo(name = "assigned_to")
    var assigned_to: String

    ) : Parcelable {

        override fun toString(): String {
            return "WorkOrder(pk=$pk, " +
                    "title='$title', " +
                    "description='$description', " +
                    "status='$status', " +
                    "created_by='$created_by', " +
                    "created_at=$created_at, " +
                    "updated_at=$updated_at, " +
                    "assigned_to='$assigned_to')"
        }
}