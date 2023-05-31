package hu.bme.aut.android.susssychat.model

import hu.bme.aut.android.susssychat.ui.model.ThreadUi

class Thread (
    val id: Int = 0,
    val name: String = ""
)

fun Thread.asTodoUi(): ThreadUi = ThreadUi(
    id = id,
    name = name,
)

fun ThreadUi.asTodo(): Thread = Thread(
    id = id,
    name = name,
)