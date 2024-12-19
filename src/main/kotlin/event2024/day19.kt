fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")

    val parsed = input.lines()
    var towels = parsed[0].split(", ").filter { it.isNotEmpty() }.toMutableSet()
    val toCheck = parsed.drop(2).toMutableList()


    val maxLength = towels.maxOf { it.length }
    val minLength = towels.minOf { it.length }
    var mem = mutableMapOf<String, Long>()

    var sum = 0L
    var sum2 = 0L

    for (check in toCheck) {
        val validCount = isValidCount(check, minLength, maxLength, towels, mem)
        if (isValid(check, minLength, maxLength, towels)) sum++
        sum2 += validCount

    }

    println(sum)
    println(sum2)
}


fun isValidCount(check: String, min: Int, max: Int, set: MutableSet<String>, mem: MutableMap<String, Long>): Long {
    var sum = 0L
    if (mem.containsKey(check)) return mem[check]!!

    for (i in min until max + 1) {
        if (i > check.length) break

        val sub = check.substring(0, i)
        if (set.contains(sub)) {
            if (check.substring(i).isEmpty()) sum++
            sum += isValidCount(check.substring(i), min, max, set, mem)
        }
    }
    mem[check] = sum
    return sum
}

fun isValid(check: String, min: Int, max: Int, set: MutableSet<String>): Boolean {
    var isOk = false
    for (i in min until max + 1) {
        if (i > check.length) break

        val sub = check.substring(0, i)
        if (set.contains(sub)) {
            if (check.substring(i).isEmpty()) return true // This exist early.
            isOk = isOk || isValid(check.substring(i), min, max, set)
        }
    }
    return isOk
}





