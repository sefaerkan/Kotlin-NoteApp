package com.example.notesapp

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.example.notesapp.databinding.ActivityAddPageBinding


class AddPage : AppCompatActivity() {

    private lateinit var binding: ActivityAddPageBinding
    private lateinit var database : SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        database = this.openOrCreateDatabase("Notes", MODE_PRIVATE,null)
        database.execSQL("CREATE TABLE IF NOT EXISTS notes (id INTEGER PRIMARY KEY, main VARCHAR, details VARCHAR)")

        val intent = intent
        val info = intent.getStringExtra("info")
        if(info.equals("new")) {
            binding.mainText.setText("")
            binding.desText.setText("")
            binding.uptadeButton.visibility = View.INVISIBLE
        } else {
            binding.addButton.visibility = View.INVISIBLE
            val selectedId = intent.getIntExtra("id",1)

            val cursor = database.rawQuery("SELECT * FROM notes WHERE id = ?", arrayOf(selectedId.toString()))

            val artNameIx = cursor.getColumnIndex("main")
            val artArtistName = cursor.getColumnIndex("details")

            while (cursor.moveToNext()) {
                binding.mainText.setText(cursor.getString(artNameIx))
                binding.desText.setText(cursor.getString(artArtistName))
            }
            cursor.close()
        }



    }

    fun saveButton(view: View) {
        val main = binding.mainText.text.toString()
        val details = binding.desText.text.toString()

        try {
            val sqlString = "INSERT INTO notes(main, details) VALUES (?,?)"
            val statment = database.compileStatement(sqlString)
            statment.bindString(1,main)
            statment.bindString(2,details)
            statment.execute()

        }  catch (e: Exception) {
            e.printStackTrace()
        }

        val intent = Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) //Benden önce kaç tane sayfa varsa kapat
        startActivity(intent)
    }

    fun uptadeButton(view: View) {
        val selectedId = intent.getIntExtra("id", 0)
        val main = binding.mainText.text.toString()
        val details = binding.desText.text.toString()

        try {
            val sqlString =
                "UPDATE notes SET main = ?, details = ? WHERE id = $selectedId"
            val statement = database.compileStatement(sqlString)
            statement.bindString(1, main)
            statement.bindString(2, details)
            statement.execute()
            Toast.makeText(this, "Güncelleme başarılı!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("GüncellemeHata", "Güncelleme hatası: ${e.message}")
            Toast.makeText(this, "Güncelleme başarısız oldu!", Toast.LENGTH_SHORT).show()
        }

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

}