package dev.chulwoo.hp.test.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.*
import dev.chulwoo.hp.test.databinding.InputItemBinding
import dev.chulwoo.hp.test.databinding.UserCountItemBinding
import dev.chulwoo.hp.test.databinding.UserItemBinding
import dev.chulwoo.hp.test.feature.user.domain.model.User


enum class MainItemViewType {
    INPUT,
    USER_COUNT,
    USER
}

class MainItemAdapter(
    private val onTextChanged: (text: String) -> Unit
) : RecyclerView.Adapter<MainItemHolder>() {

    private val differ by lazy {
        MainItemDiffer(this)
    }

    fun submitList(list: List<User>?) {
        differ.submitList(list)
    }


    private fun getItem(position: Int): User {
        return differ.currentList[position]
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainItemHolder {
        return when (viewType) {
            MainItemViewType.INPUT.ordinal -> MainItemHolder.InputItemHolder.create(parent)
            MainItemViewType.USER_COUNT.ordinal -> MainItemHolder.UserCountItemHolder.create(parent)
            MainItemViewType.USER.ordinal -> MainItemHolder.UserItemHolder.create(parent)
            else -> throw NotImplementedError()
        }
    }

    override fun onBindViewHolder(holder: MainItemHolder, position: Int) {
        when (holder) {
            is MainItemHolder.InputItemHolder -> holder.bind {
                it?.toString()?.let { keyword ->
                    onTextChanged.invoke(keyword)
                }
            }
            is MainItemHolder.UserCountItemHolder -> holder.bind(itemCount - 2)
            is MainItemHolder.UserItemHolder -> holder.bind(getItem(position - 2))
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size + 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> MainItemViewType.INPUT.ordinal
            1 -> MainItemViewType.USER_COUNT.ordinal
            else -> MainItemViewType.USER.ordinal
        }
    }
}


sealed class MainItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    class InputItemHolder private constructor(
        private val binding: InputItemBinding
    ) : MainItemHolder(binding.root) {
        companion object {
            fun create(parent: ViewGroup): InputItemHolder {
                val binding = InputItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                return InputItemHolder(binding)
            }
        }

        fun bind(onTextChanged: (text: CharSequence?) -> Unit) {
            binding.keyword.addTextChangedListener(onTextChanged = { text, _, _, _ ->
                onTextChanged(text)
            })
        }
    }

    class UserCountItemHolder private constructor(
        private val binding: UserCountItemBinding
    ) : MainItemHolder(binding.root) {
        companion object {
            fun create(parent: ViewGroup): UserCountItemHolder {
                val binding = UserCountItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                return UserCountItemHolder(binding)
            }
        }

        fun bind(count: Int) {
            binding.userCount.text = count.toString()
        }
    }

    class UserItemHolder private constructor(
        private val binding: UserItemBinding
    ) : MainItemHolder(binding.root) {
        companion object {
            fun create(parent: ViewGroup): UserItemHolder {
                val binding = UserItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                return UserItemHolder(binding)
            }
        }

        fun bind(user: User) {
            binding.user = user
        }
    }
}

class MainItemDiffer(adapter: RecyclerView.Adapter<*>) : AsyncListDiffer<User>(
    MainItemUpdateCallback(adapter),
    AsyncDifferConfig.Builder(UserDiffCallback()).build()
)

class MainItemUpdateCallback(private val adapter: RecyclerView.Adapter<*>) : ListUpdateCallback {

    private fun itemPosition(position: Int): Int {
        return position + 2
    }

    private fun notifyUserCountChanged() {
        adapter.notifyItemChanged(1)
    }

    override fun onInserted(position: Int, count: Int) {
        notifyUserCountChanged()
        adapter.notifyItemRangeInserted(itemPosition(position), count)
    }

    override fun onRemoved(position: Int, count: Int) {
        notifyUserCountChanged()
        adapter.notifyItemRangeRemoved(itemPosition(position), count)
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
        notifyUserCountChanged()
        adapter.notifyItemMoved(itemPosition(fromPosition), itemPosition(toPosition))
    }

    override fun onChanged(position: Int, count: Int, payload: Any?) {
        notifyUserCountChanged()
        adapter.notifyItemRangeChanged(itemPosition(position), count, payload)
    }
}

class UserDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}
