package event2024

import getResourceAsText

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")

    var lines = input.lines().map { it.toLong() }

    var sum = 0L
    var map = mutableMapOf<State, Long>()
    for (line in lines) {
        var secret = line
        var secrets = mutableListOf<Long>()
        for (i in 0 until 2000) {
            secret = nextSecret(secret)
            secrets.add(secret)
        }
        sum += secrets.last()
        var dif = secrets.map { getLastDigit(it) }
            .windowed(2)
            .map { it[1] - it[0] }

        var localmap = mutableMapOf<State, Long>()
        for (i in 0 until dif.size - 3) {
            var state = State(dif[i], dif[i + 1], dif[i + 2], dif[i + 3])

            if (!localmap.containsKey(state)) {
                localmap[state] = getLastDigit(secrets[i + 4])

            }
        }

        for (entry in localmap) {
            map.merge(entry.key, entry.value, Long::plus)
        }
    }

    println(sum)
    println(map.values.max())
}


// bug toString -> last -> toLong does not work char toLong -> char number.
// better to do % 10 :D
private fun getLastDigit(value: Long): Long =
    value.toString().last().toString().toLong()

// Bug Data classes have hashcode and equals nicely integrated, classes do not.
data class State(val a: Long, val b: Long, val c: Long, val d: Long) {
    override fun toString(): String {
        return "State($a, $b, $c, $d)"
    }
}

fun nextSecret(prev: Long): Long {
    var new = prev
    new = pruneAndMix(new, 64 * new)
    new = pruneAndMix(new, new / 32)
    return pruneAndMix(new, new * 2048)
}

fun pruneAndMix(x: Long, y: Long): Long {
    var local = x xor y
    return local.mod(16777216).toLong()
}


