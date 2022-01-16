package com.gdsctsec.smartt.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.gdsctsec.smartt.data.AppDatabase
import com.gdsctsec.smartt.data.TimeTable
import com.gdsctsec.smartt.data.Weekday
import com.gdsctsec.smartt.data.local.dao.LectureDao
import com.gdsctsec.smartt.model.LectureCount
import com.gdsctsec.smartt.util.CoroutineUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LectureRepository(val context: Context, var weekday: Weekday) {

    private lateinit var db: AppDatabase
    private lateinit var lectureDao: LectureDao
    private lateinit var lectures: LiveData<List<TimeTable>>
    private lateinit var lectureCountList: LiveData<List<LectureCount>>

    init {
        db = AppDatabase.getInstance(context.applicationContext)
        lectureDao = db.lectureDao()
        lectureCountList = lectureDao.getLectureCountPerWeekday()
        lectures = lectureDao.getLecturesByWeekday(weekday)
    }

    fun getLecturesByWeekday(weekday: Weekday): LiveData<List<TimeTable>> {
        return lectures
    }

    suspend fun addLecture(lecture: TimeTable): Long = withContext(Dispatchers.IO) {
        lectureDao.addLecture(lecture)
    }

    fun updateLecture(lecture: TimeTable) {
        CoroutineUtil.io {
            lectureDao.updateLecture(lecture)
        }
    }

    fun deleteLecture(id: Int) {
        CoroutineUtil.io {
            lectureDao.deleteLecture(id)
        }
    }

    fun getLectureCountPerWeekday(): LiveData<List<LectureCount>> {
        return lectureCountList
    }
}