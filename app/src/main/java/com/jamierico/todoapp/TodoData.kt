package com.jamierico.todoapp

import java.util.*

data class TodoData(var severity: Int = 0,
                    var item: String? = "",
                    var tag: String? = "",
                    var list: String? = "",
                    var isComplete: Boolean = false,
                    var dueDate: String? = "") {

    constructor(): this(0,"","","", false, "")
}