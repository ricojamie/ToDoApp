package com.jamierico.todoapp

import java.util.*

data class TodoData(var priority: Int = 0,
                    var item: String? = "",
                    var tag: String? = "",
                    var list: String? = "",
                    var isComplete: Boolean = false,
                    var due: String? = "",
                    var docId: String? = "") {

    constructor(): this(0,"","","", false, "", "")
}