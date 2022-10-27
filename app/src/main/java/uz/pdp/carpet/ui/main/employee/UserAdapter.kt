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
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import uz.pdp.carpet.R
import uz.pdp.carpet.databinding.ItemEmployeeBinding
import uz.pdp.carpet.model.User
import uz.pdp.carpet.utils.Constants
import uz.pdp.carpet.utils.Constants.STR_ADMIN
import uz.pdp.carpet.utils.Constants.STR_CUSTOMER
import uz.pdp.carpet.utils.Extensions.click


class UserAdapter :
    ListAdapter<User, UserAdapter.UserViewHolder>(UserDiffCallBack()) {

    var itemClickListener: ItemClickListener? = null

    inner class UserViewHolder(private val binding: ItemEmployeeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(user: User) = binding.apply {
            user.apply {
                tvName.text = "$name $surname"
                tvPhone.text = phoneNumber
                tvRole.text = getRole(role)
                Glide.with(root.context)
                    .load(
                        if (url != null) Constants.BASE_URL + url.substring(22)
                        else R.drawable.img_man
                    )
                    .into(ivProfile)
            }

            root.click {
                itemClickListener?.itemClick(user.id)
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
                ), 0, role.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
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

    fun updateList(list: MutableList<User>) {
        val newList = currentList.toMutableList()
        if (newList != list) newList.addAll(list)
        submitList(newList)
    }

    interface ItemClickListener {
        fun itemClick(userId: Int)
    }
}