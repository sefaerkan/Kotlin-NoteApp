package com.example.notesapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.databinding.RecyclerRowBinding

class NoteAdapter(private val noteList : ArrayList<Note>) : RecyclerView.Adapter<NoteAdapter.NoteHolder>() {

    class NoteHolder(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NoteHolder(binding)
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        holder.binding.recyclerViewTextView.text = noteList[position].name
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context,AddPage::class.java)
            intent.putExtra("info","old")
            intent.putExtra("id", noteList[position].id)
            holder.itemView.context.startActivity(intent)
        }
    }

    fun deleteItem(position: Int) {
        // Pozisyondaki öğeyi sil ve arayüzü güncelle
        noteList.removeAt(position)
        notifyItemRemoved(position)
    }



}