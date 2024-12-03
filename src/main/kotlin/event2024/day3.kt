fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")

    val regex = "mul\\(\\d{0,3},\\d{0,3}\\)".toRegex()
    val matches = regex.findAll(input, 0)
    println("Solution part 1 ${calculateSum(matches)}")


    val regexPart2 = "mul\\(\\d{0,3},\\d{0,3}\\)|do\\(\\)|don't\\(\\)".toRegex()
    val matchesPart2 = regexPart2.findAll(input, 0)
    println("Solution part 2 ${calculateSum(matchesPart2)}")
}

private fun calculateSum(matchesPart2: Sequence<MatchResult>): Int {
    var stop = false
    var sum2 = 0
    for (match in matchesPart2) {
        val value = match.value
        if (value == "do()") {
            stop = false
            continue
        }
        if (value == "don't()" || stop) {
            stop = true
            continue
        }
        val split = value.split("mul(", ",", ")")
        sum2 += split[1].toInt() * split[2].toInt()
    }
    return sum2
}


