package com.example.account

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.account.databinding.ItemAccountBinding

class AccountAdapter(
    private var accounts: List<Account>,
    private val onStudentClick: (Account) -> Unit,
    private val onDeleteClick: (Account) -> Unit,
) : RecyclerView.Adapter<AccountAdapter.AccountViewHolder>() {

    inner class AccountViewHolder(private val binding: ItemAccountBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.floatingDeleteBtn.setOnClickListener {
                onDeleteClick(accounts[adapterPosition])
            }

            binding.root.setOnClickListener {
                onStudentClick(accounts[adapterPosition])
            }
        }

        fun bind(account: Account) {
            binding.studentName.text = account.name
            binding.studentSurname.text = account.surname
            Glide.with(binding.root.context)
                .load(account.profilePhotoUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_background)
                .into(binding.imgStudent)

            binding.root.setOnClickListener { onStudentClick(account) }

            binding.floatingDeleteBtn.setOnClickListener { onDeleteClick(account) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val binding = ItemAccountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AccountViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.bind(accounts[position])
    }

    fun updateList(newList: List<Account>) {
        val diffCallback = AccountDiffUtilCallback(accounts, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        accounts = ArrayList(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int = accounts.size

    fun removeStudent(account: Account) {
        val position = accounts.indexOf(account)
        if (position != -1) {
            val updatedList = accounts.toMutableList()
            updatedList.removeAt(position)
            updateList(updatedList)
        }
    }
}