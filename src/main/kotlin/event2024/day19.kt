fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val parsed = input.lines()
    var towels = parsed[0].split(", ")
        .filter { it.isNotEmpty() }
        .toSet()
    val toCheck = parsed.drop(2)

    val maxLength = towels.maxOf { it.length }
    val minLength = towels.minOf { it.length }
    var mem = mutableMapOf<String, Long>()

    var sum = 0L
    var sum2 = 0L

    for (check in toCheck) {
        if (isValid(check, minLength, maxLength, towels)) sum++
        sum2 += isValidCount(check, minLength, maxLength, towels, mem)
    }

    println(sum)
    println(sum2)
}


fun isValidCount(check: String, min: Int, max: Int, set: Set<String>, mem: MutableMap<String, Long>): Long {
    var sum = 0L
    if (mem.containsKey(check)) return mem[check]!!

    for (i in min until max + 1) {
        if (i > check.length) break

        val sub = check.substring(0, i)
        if (set.contains(sub)) {
            val remainder = check.substring(i)
            if (remainder.isEmpty()) sum++ // Bug If return 1 as it does not count everything.
            sum += isValidCount(remainder, min, max, set, mem)
        }
    }
    mem[check] = sum
    return sum
}

fun isValid(check: String, min: Int, max: Int, set: Set<String>): Boolean {
    var isOk = false
    for (i in min until max + 1) {
        if (i > check.length) break

        val sub = check.substring(0, i)
        if (set.contains(sub)) {
            val remainder = check.substring(i)
            if (remainder.isEmpty()) return true // This exists early, as the first possible result is returned.
            isOk = isOk || isValid(remainder, min, max, set)
        }
    }
    return isOk
}





