package mobiledevcourse.nataliabarabanshchikova.graduationwork

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = arrayOf(Board::class), version = 1)
abstract class BoardDatabase : RoomDatabase() {
    abstract fun boardDao(): BoardDao
    companion object {
        private var INSTANCE: BoardDatabase? = null
        fun getDatabase(context: Context): BoardDatabase? {
            if (INSTANCE == null) {
                synchronized(BoardDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        BoardDatabase::class.java, "boards.db"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}