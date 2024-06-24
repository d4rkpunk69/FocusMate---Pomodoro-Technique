package com.example.studentregistration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentregistration.db.Student
import com.example.studentregistration.db.StudentDao
import kotlinx.coroutines.launch

class StudentViewModel(private val dao: StudentDao) : ViewModel() {

    val students = dao.getAllStudents()

    // Insert a new student
    fun insertStudent(student: Student) = viewModelScope.launch {
        dao.insertStudent(student)
    }

    // Update an existing student
    fun updateStudent(student: Student) = viewModelScope.launch {
        dao.updateStudent(student)
    }

    // Delete a student
    fun deleteStudent(student: Student) = viewModelScope.launch {
        dao.deleteStudent(student)
    }
}
