package com.example.hackathon.ui.studydetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hackathon.R
import java.text.SimpleDateFormat
import java.util.*

class CommentAdapter(private val comments: MutableList<Comment>) : 
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textAuthor: TextView = itemView.findViewById(R.id.text_comment_author)
        val textContent: TextView = itemView.findViewById(R.id.text_comment_content)
        val textTimestamp: TextView = itemView.findViewById(R.id.text_comment_timestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        holder.textAuthor.text = comment.author
        holder.textContent.text = comment.content
        
        val dateFormat = SimpleDateFormat("MM/dd HH:mm", Locale.getDefault())
        holder.textTimestamp.text = dateFormat.format(Date(comment.timestamp))
    }

    override fun getItemCount(): Int = comments.size
    
    // 댓글 업데이트를 위한 메서드 추가
    fun updateComments() {
        notifyDataSetChanged()
    }
}