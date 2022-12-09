package com.lugares_v.repository

import androidx.lifecycle.LiveData
import com.lugares_v.data.LugarDao
import com.lugares_v.model.Lugar
class LugarRepository(private val lugarDao: LugarDao) {
    fun saveLugar(lugar: Lugar) {
        //Es un lugar nuevo... se inserta
        lugarDao.saveLugar(lugar)

    }
    fun deleteLugar(lugar: Lugar) {
        lugarDao.deleteLugar(lugar)
    }
    val getLugares : LiveData<List<Lugar>> = lugarDao.getLugares()
}