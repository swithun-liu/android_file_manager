package com.example.test1.main.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.test1.base.model.PathItem
import com.example.test1.databinding.FileItemBinding
import com.example.test1.databinding.FolderItemBinding
import com.example.test1.main.model.FileItem
import com.example.test1.main.model.FolderItem
import android.widget.TextView
import com.example.test1.utils.ThemeUtil


// One Page
class SecondLevelPageRVAdapter : RecyclerView.Adapter<SecondLevelPageRVAdapter.ViewHolder>() {

    private val dataSet: MutableList<PathItem> = mutableListOf()
    private lateinit var itemClickListener: OnItemClickListener
    var selectedPathIndex = -1

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
        fun onItemLongClick(view: View, position: Int)
    }

    companion object {
        const val DIRECTORY_TYPE_FOLDER = 0
        const val DIRECTORY_TYPE_FILE = 1
    }

    inner class ViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = when (viewType) {
            DIRECTORY_TYPE_FOLDER -> FolderItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            DIRECTORY_TYPE_FILE -> FileItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            else -> FileItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        }
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.let { binding ->
            val context = binding.root.context
            var pathName: TextView? = null

            when (binding) {
                // Folder
                is FolderItemBinding -> {
                    pathName = binding.folderName
                    val theme =
                        if (position == selectedPathIndex) ThemeUtil.getFolderSelectedTheme()
                        else ThemeUtil.getFolderTheme()

                    // 1. icon
                    binding.pathIcon.background = context.getDrawable(theme.t1)
                    // 2. name
                    binding.folderName.setTextColor(context.getColor(theme.t2))
                    // 3. bg
                    binding.root.setCardBackgroundColor(context.getColor(theme.t3))
                }
                // File
                is FileItemBinding -> {
                    pathName = binding.fileName
                    val theme = ThemeUtil.getFileTheme()
                    // 1. icon
                    binding.fileIcon.background = context.getDrawable(theme.t1)
                    // 2. name
                    binding.fileName.setTextColor(context.getColor(theme.t2))
                }
                else -> null
            }

            pathName?.let { tv ->
                tv.text = dataSet[position].pathName
                binding.root.apply {
                    setOnClickListener {
                        itemClickListener.onItemClick(it, position)
                    }
                    setOnLongClickListener {
                        itemClickListener.onItemLongClick(it, position)
                        true
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun setDataSet(inputDataSet: List<PathItem>) {
        dataSet.clear()
        dataSet.addAll(inputDataSet)
    }

    override fun getItemViewType(position: Int): Int {
        return when (dataSet[position]) {
            is FolderItem -> DIRECTORY_TYPE_FOLDER
            is FileItem -> DIRECTORY_TYPE_FILE
            else -> DIRECTORY_TYPE_FILE
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }
}