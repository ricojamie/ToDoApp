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
import kotlinx.android.synthetic.main.todo_row.*
import kotlinx.android.synthetic.main.todo_row.view.*
import java.lang.IllegalArgumentException
import java.util.*

class MainActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()
    val user = FirebaseAuth.getInstance().currentUser?.uid
    var selectedPriority = "None"
    var due = ""
    var priorityInt: Int = 1
    var isComplete: Boolean = false


    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapater = GroupAdapter<ViewHolder>()
        todo_list_view.adapter = adapater

        //Load all todos
        getActiveTodos()
        //getTodos()

        //Set To-Do Priority
        priority_button.setOnClickListener {
            setPriority()
        }

        //Mark as complete
        checkBox.setOnClickListener {
//            markAsComplete()
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

            if (add_new_task_edittext.text.isBlank())
            {
                Toast.makeText(this, "Please enter a To Do item", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "You selected $selectedPriority and Due date is: $due $priorityInt", Toast.LENGTH_SHORT).show()

                priority_button.setBackgroundColor(getColor(R.color.colorPrimaryDark))

                addTodo()
                due = ""
                selectedPriority = "None"
                add_new_task_edittext.setText("")
            }
        }
    }

//        comment_button.setOnClickListener {
//            addComment()
//        }


    //Get all Todos from database
    private fun getTodos() {
        val adapter = GroupAdapter<ViewHolder>()
        db.collection("users").document(user.toString()).collection("todos")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val todo = document.toObject(TodoData::class.java)
                    adapter.add(TodoItem(todo))
                    todo_list_view.adapter = adapter
                }
            }
            .addOnFailureListener {
                Log.d("Test", "Error getting documents: ")
            }
    }

    private fun getActiveTodos() {
        val adapter = GroupAdapter<ViewHolder>()
        val dbRef = db.collection("users").document(user.toString()).collection("todos")
            dbRef.whereEqualTo("isComplete", false)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val todo = document.toObject(TodoData::class.java)
                    adapter.add(TodoItem(todo))
                    todo_list_view.adapter = adapter
                    Log.d("TodoITEM", "You have ${todo.item} with ${todo.due}")
                }
            }
            .addOnFailureListener {
                Log.d("Test", "Error getting documents: ")
            }
    }

    //Mark to do complete
//    private fun markAsComplete() {
//        val dbRef = db.collection("users").document(user.toString()).collection("todos").document().id
//        Toast.makeText(this, "You chose $dbRef", Toast.LENGTH_SHORT).show()
//
//    }


    //Set the To-Dos into the RecyclerView
    class TodoItem(val todo: TodoData) : Item<ViewHolder>() {
        override fun bind(viewHolder: ViewHolder, position: Int) {
            //Will be called in our list for each user
            viewHolder.itemView.todo_item.text = todo.item.toString()
            viewHolder.itemView.todo_date.text = todo.due.toString()
            Log.d("TodoITEM", "You have ${todo.item} with ${todo.priority}")
        }

        override fun getLayout(): Int {
            return R.layout.todo_row
        }

    }

    //Set Due Date Function
    private fun setDueDate() {
        val now = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                Toast.makeText(this, "year $year month: $month Day: $dayOfMonth", Toast.LENGTH_LONG).show()
                due = "$month-$dayOfMonth-$year"
            },
            now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }


    //Set Priority Function
    private fun setPriority() {
        val priorities = arrayOf("High", "Medium", "Low", "None")

        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Set Priority")

        builder.setItems(priorities) { _, which ->
            selectedPriority = priorities[which]

            when {
                selectedPriority === "High" -> priorityInt = 4
                selectedPriority === "Medium" -> priorityInt = 3
                selectedPriority === "Low" -> priorityInt = 2
                selectedPriority === "None" -> priorityInt = 1
            }

            try {
                when {
                    selectedPriority === "High" -> priority_button.setBackgroundColor(Color.RED)
                    selectedPriority === "Medium" -> priority_button.setBackgroundColor(Color.YELLOW)
                    selectedPriority === "Low" -> priority_button.setBackgroundColor(Color.GREEN)
                    selectedPriority === "None" -> priority_button.setBackgroundColor(getColor(R.color.colorPrimaryDark))
                }
            } catch (e: IllegalArgumentException) {
                Toast.makeText(this, "You selected ${selectedPriority.toString()}", Toast.LENGTH_SHORT).show()
            }
        }

        val prioritiesDialog = builder.create()

        prioritiesDialog.show()
    }


//    private fun addComment() {
//        val builder = AlertDialog.Builder(this@MainActivity)
//        builder.setTitle("Enter Comment")
//        val inflater = layoutInflater
//
//        builder.setView(inflater.inflate(R.layout.dialog_comment, null))
//            .setPositiveButton("Add Comment") { dialog, whichButton ->
//
//
//                Toast.makeText(this, "You selected ${comment.toString()}", Toast.LENGTH_SHORT).show()
//            }
//            .setNegativeButton("Cancel") { dialog, whichButton ->
//
//            }
//        val commentDialog = builder.create()
//        commentDialog.show()
//
//    }


    //DEPRECATED FOR NOW
    //Handle Keyboard on Focus
    fun showKeyboard(context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    private fun addTodo() {
        val item = add_new_task_edittext.text.toString()
        val list = "Testing"
        val priority = priorityInt
        val tag = "Test Tag"
        isComplete = false
        val date = due

        val todo = HashMap<String, Any>()
        todo["item"] = item
        todo["list"] = list
        todo["priority"] = priority
        todo["tag"] = tag
        todo["isComplete"] = false
        todo["due"] = date

        db.collection("users").document(user.toString()).collection("todos")
            .add(todo)
            .addOnSuccessListener { documentReference ->
                Log.d("Todo", "DocumentSnapshot added with ID")
            }
            .addOnFailureListener { e ->
                Log.d("Todo", "It failed")
            }

        getTodos()
    }
}
