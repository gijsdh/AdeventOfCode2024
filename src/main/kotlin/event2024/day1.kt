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

    // zip creates a list of pairs. [a, b, c] zip [1, 2, 3]  -> [(a, 1), (b, 2), (c, 3)]
    println("Solution zip part 1 ${(sorted1 zip sorted2).map { abs(it.second - it.first) }.sum()}")

    var sum2 = 0L
    for (line in sorted1) {
        var count=0
        for (line2 in sorted2) {
            if (line == line2) count++
        }
        sum2+=count*line
    }
    println("Solution part 2 $sum2")

    // grouping by value + counting each occurrence.
    val map = sorted2.groupingBy { it }.eachCount()
    println("Solution part 2, with map (N complexity instead of N^2) ${sorted1.map { it * map.getOrDefault(it, 0)  }.sum()}")

}


fun getResourceAsText(path: String): String {
    return object {}.javaClass.getResource(path).readText()
}

