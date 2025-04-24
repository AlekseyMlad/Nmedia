package ru.netology.nmedia.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dao.PostDaoImpl


class AppDb private constructor(db: SQLiteDatabase) {
    val postDao: PostDao = PostDaoImpl(db)

    companion object {
        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: AppDb(
                    buildDatabase(context, arrayOf(PostDaoImpl.DDL))
                ).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context, DDLs: Array<String>) = DbHelper(
            context, 3, "app.db", DDLs,
        ).writableDatabase
    }
}

class DbHelper(context: Context, dbVersion: Int, dbName: String, private val DDLs: Array<String>) :
    SQLiteOpenHelper(context, dbName, null, 3) {
    override fun onCreate(db: SQLiteDatabase) {
        DDLs.forEach {
            db.execSQL(DDLs.toString())
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 3) {
            db.execSQL("""
            CREATE TABLE ${PostDaoImpl.PostColumns.TABLE}_tmp (
                ${PostDaoImpl.PostColumns.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${PostDaoImpl.PostColumns.COLUMN_AUTHOR} TEXT NOT NULL,
                ${PostDaoImpl.PostColumns.COLUMN_CONTENT} TEXT NOT NULL,
                ${PostDaoImpl.PostColumns.COLUMN_PUBLISHED} TEXT NOT NULL,
                ${PostDaoImpl.PostColumns.COLUMN_LIKED_BY_ME} INTEGER NOT NULL DEFAULT 0,
                ${PostDaoImpl.PostColumns.COLUMN_LIKES} INTEGER NOT NULL DEFAULT 0,
                ${PostDaoImpl.PostColumns.COLUMN_SHARES} INTEGER NOT NULL DEFAULT 0
            )
        """.trimIndent())

            db.execSQL("""
            INSERT INTO ${PostDaoImpl.PostColumns.TABLE}_tmp (
                ${PostDaoImpl.PostColumns.COLUMN_ID},
                ${PostDaoImpl.PostColumns.COLUMN_AUTHOR},
                ${PostDaoImpl.PostColumns.COLUMN_CONTENT},
                ${PostDaoImpl.PostColumns.COLUMN_PUBLISHED},
                ${PostDaoImpl.PostColumns.COLUMN_LIKED_BY_ME},
                ${PostDaoImpl.PostColumns.COLUMN_LIKES}
            )
            SELECT
                ${PostDaoImpl.PostColumns.COLUMN_ID},
                ${PostDaoImpl.PostColumns.COLUMN_AUTHOR},
                ${PostDaoImpl.PostColumns.COLUMN_CONTENT},
                ${PostDaoImpl.PostColumns.COLUMN_PUBLISHED},
                ${PostDaoImpl.PostColumns.COLUMN_LIKED_BY_ME},
                ${PostDaoImpl.PostColumns.COLUMN_LIKES}
            FROM ${PostDaoImpl.PostColumns.TABLE}
        """.trimIndent())

            db.execSQL("DROP TABLE ${PostDaoImpl.PostColumns.TABLE}")

            db.execSQL("ALTER TABLE ${PostDaoImpl.PostColumns.TABLE}_tmp RENAME TO ${PostDaoImpl.PostColumns.TABLE}")

            db.execSQL("DROP TABLE IF EXISTS ${PostDaoImpl.PostColumns.TABLE}")
            db.execSQL(PostDaoImpl.DDL)
        }
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${PostDaoImpl.PostColumns.TABLE}")
        onCreate(db)
    }
}
