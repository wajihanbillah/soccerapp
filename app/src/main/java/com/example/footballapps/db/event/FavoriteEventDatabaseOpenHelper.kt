package com.example.footballapps.db.event

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.footballapps.model.Event
import org.jetbrains.anko.db.*

/**
 * Created by root on 2/6/18.
 */
class FavoriteEventDatabaseOpenHelper(ctx: Context) :
    ManagedSQLiteOpenHelper(ctx, "Event.db", null, 2) {
    companion object {
        private var instance: FavoriteEventDatabaseOpenHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): FavoriteEventDatabaseOpenHelper {
            if (instance == null) {
                instance =
                    FavoriteEventDatabaseOpenHelper(ctx.applicationContext)
            }
            return instance as FavoriteEventDatabaseOpenHelper
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Here you create tables
        db.createTable(
            FavoriteEvent.TABLE_FAVORITE_EVENT, true,
            FavoriteEvent.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            FavoriteEvent.EVENT_ID to TEXT + UNIQUE,
            FavoriteEvent.EVENT_NAME to TEXT,
            FavoriteEvent.EVENT_DATETIME to TEXT,
            FavoriteEvent.EVENT_NAME_HOME to TEXT,
            FavoriteEvent.EVENT_NAME_AWAY to TEXT,
            FavoriteEvent.EVENT_SCORE_HOME to TEXT,
            FavoriteEvent.EVENT_SCORE_AWAY to TEXT,
            FavoriteEvent.EVENT_BADGE_HOME to TEXT,
            FavoriteEvent.EVENT_BADGE_AWAY to TEXT
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        db.dropTable(FavoriteEvent.TABLE_FAVORITE_EVENT, true)
    }
}

// Access property for Context
val Context.databaseEvent: FavoriteEventDatabaseOpenHelper
    get() = FavoriteEventDatabaseOpenHelper.getInstance(
        applicationContext
    )