package com.example.studentregistration

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studentregistration.databinding.ListItemBinding
import com.example.studentregistration.db.Student

class StudentRecycleViewAdapter(
    private val clickListener:(Student)->Unit
):RecyclerView.Adapter<StudentViewHolder >() {

    private val studentList = ArrayList<Student>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StudentViewHolder(binding )
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(studentList[position], clickListener )
    }

    fun setList(students:List<Student>) {
        studentList.clear()
        studentList.addAll(students)
    }

}

class StudentViewHolder(private val binding: ListItemBinding):RecyclerView.ViewHolder(binding.root) {
    fun bind(student: Student, clickListener:(Student)->Unit) {
        binding.apply {
            tvName.text = student.name
            tvEmail.text = student.email
            root.setOnClickListener {
            clickListener(student)
            }
        }
    }
}