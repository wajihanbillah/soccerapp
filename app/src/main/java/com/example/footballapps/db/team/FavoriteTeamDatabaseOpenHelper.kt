package com.example.footballapps.db.team

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.footballapps.model.Team
import org.jetbrains.anko.db.*

/**
 * Created by root on 2/6/18.
 */
class FavoriteTeamDatabaseOpenHelper(ctx: Context) :
    ManagedSQLiteOpenHelper(ctx, "Team.db", null, 2) {
    companion object {
        private var instance: FavoriteTeamDatabaseOpenHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): FavoriteTeamDatabaseOpenHelper {
            if (instance == null) {
                instance =
                    FavoriteTeamDatabaseOpenHelper(ctx.applicationContext)
            }
            return instance as FavoriteTeamDatabaseOpenHelper
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Here you create tables
        db.createTable(
            FavoriteTeam.TABLE_FAVORITE_TEAM, true,
            FavoriteTeam.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            FavoriteTeam.TEAM_ID to TEXT + UNIQUE,
            FavoriteTeam.TEAM_NAME to TEXT,
            FavoriteTeam.TEAM_DESCRIPTION to TEXT,
            FavoriteTeam.TEAM_BADGE to TEXT
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        db.dropTable(FavoriteTeam.TABLE_FAVORITE_TEAM, true)
    }
}

// Access property for Context
val Context.databaseTeam: FavoriteTeamDatabaseOpenHelper
    get() = FavoriteTeamDatabaseOpenHelper.getInstance(
        applicationContext
    )