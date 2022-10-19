package uz.pdp.carpet.ui.main.employee

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.pdp.carpet.R
import uz.pdp.carpet.databinding.ItemEmployeeBinding
import uz.pdp.carpet.model.User
import uz.pdp.carpet.utils.Constants.STR_ADMIN
import uz.pdp.carpet.utils.Constants.STR_CUSTOMER


class UserAdapter : ListAdapter<User, UserAdapter.UserViewHolder>(UserDiffCallBack()) {

    class UserViewHolder(private val binding: ItemEmployeeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(user: User) = binding.apply {
            user.apply {
                tvName.text = "$name $surname"
                tvPhone.text = phoneNumber
                tvRole.text = getRole(role)
            }
        }

        private fun getRole(role: String) = SpannableString(role).apply {
            setSpan(
                ForegroundColorSpan(
                    getColor(
                        binding.root.context, when (role) {
                            STR_ADMIN -> R.color.color_success
                            STR_CUSTOMER -> R.color.color_error
                            else -> R.color.color_warring
                        }
                    )
                ),
                0,
                role.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    fun updateList(list: List<User>) {
        val result = ArrayList<User>()

        if (currentList.isNotEmpty())
            result.addAll(currentList)

        if (list.isNotEmpty())
            result.addAll(list)

        submitList(result)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder =
        UserViewHolder(
            ItemEmployeeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )


    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class UserDiffCallBack : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean = oldItem == newItem

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean = oldItem == newItem
    }
}