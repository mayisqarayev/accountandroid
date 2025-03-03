package com.example.account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.account.databinding.ActivityUpdateBinding

class UpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateBinding
    private var studentId: Int = -1
    private var photoUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        studentId = intent.getIntExtra("id", -1)
        val name = intent.getStringExtra("name") ?: ""
        val surname = intent.getStringExtra("surname") ?: ""
        photoUrl = intent.getStringExtra("photoUrl") ?: ""

        binding.edtStudentName.setText(name)
        binding.edtStudentSurname.setText(surname)

        Glide.with(binding.root.context).load(photoUrl).into(binding.imgStudentEdit)

        binding.btnSave.setOnClickListener {
            val updatedName = binding.edtStudentName.text.toString().trim()
            val updatedSurname = binding.edtStudentSurname.text.toString().trim()

            val resultIntent = Intent().apply {
                putExtra("id", studentId)
                putExtra("name", updatedName)
                putExtra("surname", updatedSurname)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}