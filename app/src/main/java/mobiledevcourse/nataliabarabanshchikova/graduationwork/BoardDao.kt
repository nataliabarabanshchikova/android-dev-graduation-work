package mobiledevcourse.nataliabarabanshchikova.graduationwork

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface BoardDao {
    @Insert
    fun insert(chapter: Board)

    @Query("SELECT * FROM board_entity")
    fun getAll(): Array<Board>

    @Query("DELETE FROM board_entity")
    fun deleteAll()
}