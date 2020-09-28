package nl.hva.fdmci.mad.level4_task2.model

import nl.hva.fdmci.mad.level4_task2.R

enum class Weapon(val resourceId: Int) {
    ROCK(resourceId = R.drawable.rock), PAPER(resourceId = R.drawable.paper), SCISSORS(resourceId = R.drawable.scissors)
}