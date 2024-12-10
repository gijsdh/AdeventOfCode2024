fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val line = input.split("").filter(String::isNotEmpty).map(String::toInt)

    var list = mutableListOf<Pair<Int, Int>>()
    var index = 0
    for (number in line.withIndex()) {
        if (number.index % 2 == 0) {
            list.add(Pair(index, number.value))
            index++
        } else {
            list.add(Pair(-1, number.value))
        }
    }

    var compacted = list.toMutableList()


    for (i in list.size - 1 downTo 0 step 2) {
        val id = list[i].first
        val size = list[i].second

        val current = compacted.indexOf(list[i])

        for (j in 0 until compacted.size) {

            if (compacted[j].first == -1) {
                // Only move memory to the left, not to the right.
                if (current < j) break
                var space = compacted[j].second
                if (space > size) {

                    compacted[current] = Pair(-1, size)
                    compacted[j] = Pair(id, size)

                    // As we are only party fill up memory. Fill up the memory, plus add a free memory segment.
                    compacted = compacted.subList(0, j + 1)
                        .plus(Pair(-1, space - size))
                        .plus(compacted.subList(j + 1, compacted.size - 1)).toMutableList()
                    break
                } else if (space == size) {
                    compacted[current] = Pair(-1, size)
                    compacted[j] = Pair(id, size)
                    break
                }
            }
        }
    }



    println(compacted
        .map { pair ->
            (0 until pair.second)
                .map { if (pair.first == -1) 0 else pair.first }
        }
        .flatten()
        .map { it.toLong() }
        .withIndex()
        .sumOf { it.value * it.index })

}


