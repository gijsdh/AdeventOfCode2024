fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputLines = input.lines()
        .map {
            it.split(" ")
                .map { it.toInt() }
        }

    println(inputLines)
    var count = 0
    for (line in inputLines) {
        if (increasing(line) || decreasing(line)) {
            count++
            continue
        }
    }
    println("Solution part 1 $count")
    var count2 = 0
    for (line in inputLines) {
        for (i in line.indices) {
            val newline = line.toMutableList()
            newline.removeAt(i)
            if (increasing(newline) || decreasing(newline)) {
                count2++
                break
            }
        }
    }
    println("Solution part 2 $count2")
}

private fun decreasing(newline: List<Int>) = newline.windowed(2).all { it[0] - it[1] in 1..3 }
private fun increasing(newline: List<Int>) = newline.windowed(2).all { it[1] - it[0] in 1..3 }
