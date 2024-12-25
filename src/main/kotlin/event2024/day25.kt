package event2024

import getResourceAsText

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val grids = input.split("\n\r\n").map { it.lines().filter(String::isNotBlank) }.filter { it.isNotEmpty() }

    val (keys, locks) = parseInput(grids)

    var sum = 0
    for (key in keys) {
        for (lock in locks) {
            sum += fits(key, lock)
        }
    }
    println(sum)
}

private fun parseInput(grids: List<List<String>>): Pair<MutableSet<List<Int>>, MutableSet<List<Int>>> {
    val keys = mutableSetOf<List<Int>>()
    val locks = mutableSetOf<List<Int>>()

    for (grid in grids) {
        val list = mutableListOf<Int>()

        val (range, isKey) = when (grid[0][0]) {
            '#' -> grid.indices to true
            '.' -> grid.size - 1 downTo 0 to false
            else -> throw Exception("shit")
        }

        for (i in grid[0].indices) {
            var counter = 0
            for (j in range) {
                if (grid[j][i] == '#') counter++
            }
            list.add(counter)
        }

        if (isKey) {
            keys.add(list)
        } else {
            locks.add(list)
        }
    }
    return Pair(keys, locks)
}


fun fits(key: List<Int>, lock: List<Int>): Int = key.indices
    .any { i -> lock[i] + key[i] > 7 }
    .let { if (it) 0 else 1 }
