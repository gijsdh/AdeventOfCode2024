fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val splitted = input.split(" ")

    var map = mutableMapOf<String, Long>()
    splitted.forEach { map[it] = 1 }

    println(calculateExpansion(map, 25).values.sum())
    println(calculateExpansion(map, 75).values.sum())
}

private fun calculateExpansion(map: MutableMap<String, Long>, runs: Int): MutableMap<String, Long> {
    var workMap = map
    for (i in 0 until runs) {
        var newMap = mutableMapOf<String, Long>()
        for ((key, value) in workMap) {
            if (key == "0") {
                newMap.merge("1", value, Long::plus)
            } else if (key.length % 2 == 0) {
                newMap.merge(key.substring(0, key.length / 2), value, Long::plus)

                // the toLong toString is remove preceding zero's in a number.
                val substring = key.substring(key.length / 2).toLong().toString()
                newMap.merge(substring, value, Long::plus)
            } else {
                newMap.merge((key.toLong() * 2024L).toString(), value, Long::plus)
            }
        }
        workMap = newMap.toMutableMap()
    }
    return workMap
}

