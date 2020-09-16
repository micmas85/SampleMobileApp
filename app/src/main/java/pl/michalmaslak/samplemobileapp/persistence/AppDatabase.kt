package pl.michalmaslak.samplemobileapp.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import pl.michalmaslak.samplemobileapp.models.AccountProperties
import pl.michalmaslak.samplemobileapp.models.AuthToken
import pl.michalmaslak.samplemobileapp.models.WorkOrder

@Database(entities = [AuthToken::class, AccountProperties::class, WorkOrder::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getAuthTokenDao(): AuthTokenDao

    abstract fun getAccountPropertiesDao(): AccountPropertiesDao

    abstract fun getWorkOrderDao(): WorkOrderDao

    companion object{
        const val DATABASE_NAME = "sample_app_db"
    }

}