package com.example.account

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.account.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: AccountAdapter
    private var accounts = mutableListOf(
        Account(1, "Ali", "Veli", "https://randomuser.me/api/portraits/women/50.jpg"),
        Account(2, "Ayşe", "Demir", "https://randomuser.me/api/portraits/women/52.jpg"),
        Account(3, "Mehmet", "Kaya", "https://randomuser.me/api/portraits/women/52.jpg")
    )

    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val id = result.data!!.getIntExtra("id", -1)
            val name = result.data!!.getStringExtra("name") ?: ""
            val surname = result.data!!.getStringExtra("surname") ?: ""

            updateStudentInList(id, name, surname)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = AccountAdapter(accounts,
            onStudentClick = { account -> openUpdateScreen(account) },
            onDeleteClick = { account -> removeStudent(account) }
        )

        binding.rcyStudents.layoutManager = LinearLayoutManager(this)
        binding.rcyStudents.adapter = adapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            refresh()
        }
    }

    private fun refresh(id: Int? = null, name: String? = null, surname: String? = null) {
        if (id != null && name != null && surname != null) {
            updateStudentInList(id, name, surname)
        }
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun openUpdateScreen(account: Account) {
        val intent = Intent(this, UpdateActivity::class.java).apply {
            putExtra("id", account.id)
            putExtra("name", account.name)
            putExtra("surname", account.surname)
            putExtra("photoUrl", account.profilePhotoUrl)
        }
        launcher.launch(intent)
    }

    private fun updateStudentInList(id: Int, name: String, surname: String) {
        val updatedList = accounts.map { account ->
            if (account.id == id) {
                account.copy(name = name, surname = surname)
            } else {
                account
            }
        }
        adapter.updateList(updatedList)
    }

    private fun removeStudent(account: Account) {
        val position = accounts.indexOf(account)
        if (position != -1) {
            val updatedList = accounts.toMutableList()
            updatedList.removeAt(position)
            adapter.updateList(updatedList)
        }
    }
}