package com.example.wirecode.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import kotlinx.coroutines.launch


private val PIN_NUMBER : Int = 32

//entity = row
//entity entires = columns
@Entity(tableName = "wire_mapping")
data class Mapping(
    @PrimaryKey
    @ColumnInfo(name = "part_number") val partNumber: String,
    @ColumnInfo(name = "pin_mapping") val pinMapping: ArrayList<String>

)

class Converters {
    @TypeConverter
    fun fromString(value: String?): ArrayList<String?>? {
        val listType =
            object : TypeToken<ArrayList<String?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<String?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}
@Dao
interface MappingDao {
    @Query("SELECT * FROM wire_mapping")
    fun getAll(): List<Mapping>

    //return livedata to use with UI controller
    //async in background
    @Query("SELECT * from wire_mapping ORDER BY part_number ASC")
    fun getAlphabetizedMappings(): LiveData<List<Mapping>>

    @Query("SELECT * FROM wire_mapping WHERE part_number LIKE :partNumber LIMIT 1")
    fun findByName(partNumber: String): Mapping

    //replaces if creating a new entry
    //todo: save a copy before replacement
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(wireMapping: Mapping)

    @Insert
    fun insertAll(vararg users: Mapping)

    @Delete
    fun delete(wireMapping: Mapping)

    @Query("DELETE FROM wire_mapping")
    suspend fun deleteAll()

}

//declare entities belonging to
//todo: not database migrations (exportschema false)
//When you modify the database schema, you'll need to update the version number and define a migration strategy

@TypeConverters(Converters::class)
@Database(entities = arrayOf(Mapping::class), version = 1, exportSchema = false)
abstract class MappingDatabase : RoomDatabase() {
    abstract fun mappingDao(): MappingDao

    companion object {
        @Volatile
        private var INSTANCE: MappingDatabase? = null

        @InternalCoroutinesApi
        fun getDatabase (
            context: Context,
            scope: CoroutineScope
        ): MappingDatabase {

            val tempInstance =
                INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MappingDatabase::class.java,
                    "wire_mapping"
                )
                    .addCallback(MappingDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                return  instance
            }
        }
    }

    class MappingDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.mappingDao())
                }
            }
        }

        suspend fun populateDatabase(mappingDao: MappingDao) {
            // Delete all content here.
            mappingDao.deleteAll()

            val testarray : ArrayList<String> = ArrayList()
            for (i in 1..32) {
                testarray.add("Pin $i")
            }

            // Add sample words.
            var testMapping = Mapping(
                "Test Part Number",
                testarray
            )
            mappingDao.insert(testMapping)

            // TODO: Add your own words!
        }
    }

}
