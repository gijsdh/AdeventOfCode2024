fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val lines = input.lines()
        .map {
            it.split(":")
                .filter(
                    String::isNotBlank
                ).map {
                    it.split(" ")
                        .filter(String::isNotEmpty)
                }
        }


    // TODO make it also work for part one.
    var sum = 0L
    for (line in lines) {
        val answer = line[0][0].toLong()
        val values = line[1].map { it.toLong() }

        if (isValid(answer, values)) {
            sum += answer
        }
    }
    println(lines)
    println("Solution part 1 - ${sum}")
}

fun isValid(answer: Long, values: List<Long>): Boolean {
    if (values.size == 1) {
        return answer == values[0]
    }

    if (isValid(answer, listOf(values[0] * values[1]).plus(values.drop(2)))) {
        return true
    } else if (isValid(answer, listOf(values[0] + values[1]).plus(values.drop(2)))) {
        return true
    } else if (isValid(answer, listOf(("" + values[0] + values[1]).toLong()).plus(values.drop(2)))) {
        return true
    }
    return false
}
