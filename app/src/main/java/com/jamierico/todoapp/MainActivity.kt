package com.jamierico.todoapp

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.support.v7.widget.LinearLayoutManager
import android.text.Layout
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.ACTION_DOWN
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_comment.*
import java.lang.IllegalArgumentException
import java.util.*

class MainActivity : AppCompatActivity() {



    val db = FirebaseFirestore.getInstance()
    val user = FirebaseAuth.getInstance().currentUser?.uid

    var selectedPriority = "None"
    var dueDate = " "



    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapater = GroupAdapter<ViewHolder>()
        adapater.add(TodoItem())
        adapater.add(TodoItem())
        todo_list_view.adapter = adapater

        getTodos()

        //Set To-Do Priority
        priority_button.setOnClickListener {
            setPriority()
        }

        //Add Comment to To-Do
        comment_button.setOnClickListener {

        }
        //Set Due date for To-DO
        date_picker_button.setOnClickListener {
            setDueDate()
        }

        //Add To-Do to Firebase
        add_button.setOnClickListener {
            Toast.makeText(this, "You selected $selectedPriority and Due date is: $dueDate", Toast.LENGTH_SHORT).show()
            selectedPriority = "None"
            priority_button.setBackgroundColor(getColor(R.color.colorPrimaryDark))
        }

    }

    private fun getTodos() {
       db.collection("users").document(user.toString()).collection("todos")
           .get()
           .addOnSuccessListener { result ->
               for(document in result) {
                   val todoItem = findViewById<TextView>(R.id.todo_item)
                   todoItem.text = document.data.toString()
//                   todoItem.text = document.get("item").toString()
                   Log.d("Test", "${document.id} => ${document.data}")

//                   val stuff = document.get("item")
//                   val todoData = document.toObject(TodoData::class.java)
//                   Log.d("Test", "You got ${stuff}")
               }
           }
           .addOnFailureListener{
               Log.d("Test", "Error getting documents: ")
           }


    }


    class TodoItem: Item<ViewHolder>() {
        override fun bind(viewHolder: ViewHolder, position: Int) {
            //Will be called in our list for each user
        }
        override fun getLayout(): Int {
            return R.layout.todo_row
        }

    }


    //Set Due Date Function
    private fun setDueDate() {
        val now = Calendar.getInstance()
        val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            Toast.makeText(this, "year $year month: $month Day: $dayOfMonth", Toast.LENGTH_LONG).show()
            dueDate = "$year-$month-$dayOfMonth"
        },
            now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
    }


    //Set Priority Function
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


    //DEPRECATED FOR NOW
    //Handle Keyboard on Focus
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
