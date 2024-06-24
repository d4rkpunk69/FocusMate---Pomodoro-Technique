package com.example.studentregistration

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentregistration.databinding.ActivityMainBinding
import com.example.studentregistration.db.Student
import com.example.studentregistration.db.StudentDatabase

class MainActivity : AppCompatActivity() {
    // no need to use this
//    private lateinit var nameInput: EditText
//    private lateinit var emailInput: EditText
//    private lateinit var saveClick: Button
//    private lateinit var clearClick: Button
//    private lateinit var studentRecyclerView: RecyclerView
    private lateinit var adapter: StudentRecycleViewAdapter
    private lateinit var viewModel: StudentViewModel
    private lateinit var selectedStudent: Student
    private var isListItemClicked = false
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//         NO NEED TO USE THIS
//        nameInput = findViewById(R.id.etName)
//        emailInput = findViewById(R.id.etEmail)
//        saveClick = findViewById(R.id.btnSubmit)
//        clearClick = findViewById(R.id.btnClear)
//        studentRecyclerView = findViewById(R.id.rvStudent)

        val dao = StudentDatabase.getInstance(application).studentDao()
        val factory = StudentViewModelFactory(dao)
        viewModel = ViewModelProvider(this, factory).get(StudentViewModel::class.java)

        // Disable submit button initially
        binding.btnSubmit.isEnabled = false

        // Add TextWatchers to EditText fields
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.btnSubmit.isEnabled = binding.etName.text.isNotEmpty() && binding.etEmail.text.isNotEmpty()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        binding.etName.addTextChangedListener(textWatcher)
        binding.etEmail.addTextChangedListener(textWatcher)

        binding.btnSubmit.setOnClickListener {
            if(isListItemClicked){
                updateStudentData()
                clearInput()
            }else{
                saveStudentData()
                clearInput()
            }
        }

        binding.btnClear.setOnClickListener {
            if(isListItemClicked){
                deleteStudentData()
                clearInput()
            }else{
                clearInput()
            }
        }
        initRecyclerView()
    }

    private fun saveStudentData() {
        viewModel.insertStudent(
            Student(
                0,
                binding.etName.text.toString(),
                binding.etEmail.text.toString()
            )
        )
    }

    @SuppressLint("SetTextI18n")
    private fun updateStudentData() {
        viewModel.updateStudent(
            Student(
                selectedStudent.id,
                binding.etName.text.toString(),
                binding.etEmail.text.toString()
            )
        )
        binding.btnSubmit.text = "Save"
        binding.btnClear.text = "Clear"
        isListItemClicked = false
    }

    @SuppressLint("SetTextI18n")
    private fun deleteStudentData() {
        viewModel.deleteStudent(
            Student(
                selectedStudent.id,
                binding.etName.text.toString(),
                binding.etEmail.text.toString()
            )
        )
        binding.btnSubmit.text = "Save"
        binding.btnClear.text = "Clear"
        isListItemClicked = false
    }

    private fun clearInput() {
        binding.etName.setText("")
        binding.etEmail.setText("")
    }

    private fun initRecyclerView() {
        binding.rvStudent.layoutManager = LinearLayoutManager(this)
        adapter = StudentRecycleViewAdapter{
            selectedItem: Student -> listItemClicked(selectedItem)
        }
        binding.rvStudent.adapter = adapter

        displayStudentList(adapter)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun displayStudentList(adapter: StudentRecycleViewAdapter) {
        viewModel.students.observe(
            this,
        ) {
            this.adapter.setList(it)
            this.adapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun listItemClicked(student: Student) {
        Toast.makeText(
            this,
            "Student name is ${student.name} with email : ${student.email}",
            Toast.LENGTH_LONG
        ).show()

        selectedStudent = student
        binding.btnSubmit.text = "Update"
        binding.btnClear.text = "Delete"
        isListItemClicked = true
        binding.etName.setText(selectedStudent.name)
        binding.etEmail.setText(selectedStudent.email)
    }
}
