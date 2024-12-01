import kotlin.math.abs

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputLines = input
        .lines().map {
            it.split("   ")
                .map { it.toLong() }
        }

    val sorted1 = inputLines.map { it[0] }.sorted()
    val sorted2 = inputLines.map { it[1] }.sorted()

    var sum = 0L
    for (line in sorted1.withIndex()) {
        sum += abs(sorted2[line.index] - line.value)
    }
    println("Solution part 1 $sum")

    var sum2 = 0L
    for (line in sorted1) {
        var count=0
        for (line2 in sorted2) {
            if (line == line2) count++
        }
        sum2+=count*line
    }
    println("Solution part 2 $sum2")
}


fun getResourceAsText(path: String): String {
    return object {}.javaClass.getResource(path).readText()
}

