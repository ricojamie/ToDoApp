package com.jamierico.todoapp

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.text.Layout
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.ACTION_DOWN
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_comment.*
import java.lang.IllegalArgumentException

class MainActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()
    val user = FirebaseAuth.getInstance().currentUser?.uid

    var selectedPriority = "None"
    var comment = ""
    var commentText = findViewById(R.id.comment_edittext) as EditText


    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        priority_button.setOnClickListener {
            setPriority()
        }

        add_button.setOnClickListener {
            Toast.makeText(this, "You selected ${selectedPriority} . Added comment ${comment} ", Toast.LENGTH_SHORT).show()
            selectedPriority = "None"
            priority_button.setBackgroundColor(getColor(R.color.colorPrimaryDark))

        }

        comment_button.setOnClickListener {
            addComment()
        }






    }



    private fun setPriority(){
        val priorities = arrayOf("High", "Medium", "Low", "None")

        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Set Priority")

        builder.setItems(priorities) { _, which ->
            selectedPriority = priorities[which]

            try {
                when {
                    selectedPriority === "High" -> priority_button.setBackgroundColor(Color.RED)
                    selectedPriority === "Medium" -> priority_button.setBackgroundColor(Color.YELLOW)
                    selectedPriority === "Low" -> priority_button.setBackgroundColor(Color.GREEN)
                    selectedPriority === "None" -> priority_button.setBackgroundColor(getColor(R.color.colorPrimaryDark))
                }
            }catch (e:IllegalArgumentException) {
                Toast.makeText(this, "You selected ${selectedPriority.toString()}", Toast.LENGTH_SHORT).show()
            }
        }

        val prioritiesDialog = builder.create()

        prioritiesDialog.show()
    }

    private fun addComment() {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Enter Comment")
        val inflater = layoutInflater

        builder.setView(inflater.inflate(R.layout.dialog_comment, null))
            .setPositiveButton("Add Comment") { dialog, whichButton ->

                
                Toast.makeText(this, "You selected ${comment.toString()}", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel") { dialog, whichButton ->

            }
        val commentDialog = builder.create()
        commentDialog.show()

    }

    fun showKeyboard(context: Context) {
         val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
         imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
     }

    private fun addTodo() {
//        val item = todo_item_edittext.text.toString()
//        val list = todo_list_edittext.text.toString()
//        val severity = todo_severity_edittext.text.toString()
//        val tag = todo_tag_edittext.text.toString()
//
//        val todo = HashMap<String, Any>()
//        todo["item"] = item
//        todo["list"] = list
//        todo["severity"] = severity
//        todo["tag"] = tag
//
//        db.collection("users").document(user.toString()).collection("todos")
//            .add(todo)
//            .addOnSuccessListener { documentReference ->
//                Log.d("Todo", "DocumentSnapshot added with ID")
//            }
//            .addOnFailureListener { e ->
//                Log.d("Todo", "It failed")
//            }


    }
}
