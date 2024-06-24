package com.example.focusmate.session

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.focusmate.databinding.ListItemSessionBinding

class SessionAdapter(
    private val deleteSession: (Session) -> Unit)
    : RecyclerView.Adapter<SessionAdapter.SessionViewHolder>() {

    private var sessions = emptyList<Session>()

    inner class SessionViewHolder(private val binding: ListItemSessionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(session: Session) {
            binding.session = session
            binding.executePendingBindings()
            binding.imgbtnDelete.setOnClickListener {
                deleteSession(session)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        val binding = ListItemSessionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SessionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        val session = sessions[position]
        holder.bind(session)
    }

    override fun getItemCount() = sessions.size

    @SuppressLint("NotifyDataSetChanged")
    fun setSessions(sessions: List<Session>) {
        this.sessions = sessions
        notifyDataSetChanged()
    }
}
