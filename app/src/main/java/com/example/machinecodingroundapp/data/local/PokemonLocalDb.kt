package com.example.machinecodingroundapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.machinecodingroundapp.data.models.PokemonListItem
import kotlinx.coroutines.InternalCoroutinesApi
import kotlin.concurrent.Volatile

const val DATABASE_NAME = "pokemon_db"

@Database(entities = [PokemonListItem::class], version = 1, exportSchema = false)
abstract class PokemonLocalDb:RoomDatabase() {

    abstract fun pokemonDao():PokemonDao

    companion object{
        @Volatile
        private var instance:PokemonLocalDb? = null

        fun getInstance(context:Context): PokemonLocalDb {
            return instance ?: synchronized(this) {
                instance = Room.databaseBuilder(
                    context = context.applicationContext,
                    klass = PokemonLocalDb::class.java,
                    name = DATABASE_NAME
                ).build()
                instance!!
            }
        }
    }
}