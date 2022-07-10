package pt.ulusofona.deisi.cm2122.g21805799.data.local.dao

import android.content.Context
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pt.ulusofona.deisi.cm2122.g21805799.data.local.entities.FireDB

@Dao
interface FireOperations {

    @Insert
    fun insert(operation: FireDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(operations: List<FireDB>)

    @Query("SELECT * FROM firedb")
    fun getAll(): List<FireDB>

    @Query("DELETE FROM firedb where name = :nothing")
    fun deleteAll(nothing: String)

}