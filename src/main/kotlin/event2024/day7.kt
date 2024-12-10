fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val lines = input.lines()
        .map {
            it.split(":")
                .filter(
                    String::isNotBlank
                )
                .map {
                    it.split(" ")
                        .filter(String::isNotEmpty)
                        .map { it.toLong() }
                }
        }
    
    var sum = 0L
    var sum2 = 0L
    for (line in lines) {
        val answer = line[0][0]
        val values = line[1]
        if (isValid(answer, values, false)) {
            sum2 += answer
        }
        if (isValid(answer, values, true)) {
            sum += answer
        }
    }
    println("Solution part 1 - ${sum}")
    println("Solution part 2 - ${sum2}")
}

fun isValid(answer: Long, values: List<Long>, isPartOne: Boolean): Boolean {
    if (values.size == 1) return answer == values[0]

    if (isValid(answer, plus(values), isPartOne)) return true
    if (isValid(answer, multiply(values), isPartOne)) return true
    if (!isPartOne && isValid(answer, concatenate(values), false)) return true

    return false
}

private fun concatenate(values: List<Long>) =
    listOf(
        StringBuilder()
            .append(values[0])
            .append(values[1])
            .toString().toLong()
    ).plus(values.drop(2))

private fun multiply(values: List<Long>) =
    listOf(values[0] + values[1])
        .plus(values.drop(2))

private fun plus(values: List<Long>) =
    listOf(values[0] * values[1])
        .plus(values.drop(2))

