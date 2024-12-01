fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputLines = input.lines().map { it.toInt() }

    // part 1
    println("solution part 1: ${inputLines.sum()}")

    var sum = 0

    var set = mutableSetOf<Int>()
    var i = 0;
    while (true) {
        var line = inputLines[i++ % inputLines.size]
        sum += line
        if (set.contains(sum)) {
            println("solution part 2: ${sum}"); break
        } else set.add(sum)
    }
}
