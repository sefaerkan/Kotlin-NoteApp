package com.example.notesapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var noteList : ArrayList<Note>
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var swipeHandler: SwipeToDeleteCallback

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.addButon.setOnClickListener {
            intent = Intent(this,AddPage::class.java)
            intent.putExtra("info","new")
            startActivity(intent)
        }

        noteList = ArrayList()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        noteAdapter = NoteAdapter(noteList)
        binding.recyclerView.adapter = noteAdapter

        try {
            val database = this.openOrCreateDatabase("Notes", MODE_PRIVATE,null)
            val cursor = database.rawQuery("SELECT * FROM notes",null)
            val artNameIx = cursor.getColumnIndex("main")
            val idIx = cursor.getColumnIndex("id")

            while (cursor.moveToNext()) {
                val main = cursor.getString(artNameIx)
                val id = cursor.getInt(idIx)
                val art = Note(main,id)
                noteList.add(art)
            }

            noteAdapter.notifyDataSetChanged() //Adaptere haber ver veriler değişti
            cursor.close()

        } catch (e:Exception) {
            e.printStackTrace()
        }

        //Kaydırarak Silme İşlemi
        swipeHandler = object : SwipeToDeleteCallback(noteAdapter) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deletedArt = noteList[position]
                try {
                    val database = this@MainActivity.openOrCreateDatabase("Notes", MODE_PRIVATE, null)
                    val sqlString = "DELETE FROM notes WHERE id = ?"
                    val statement = database.compileStatement(sqlString)
                    statement.bindLong(1, deletedArt.id.toLong())
                    statement.execute()

                    noteList.removeAt(position)
                    noteAdapter.notifyItemRemoved(position)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

    }
}