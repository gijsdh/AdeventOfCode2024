fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputLines = input.split("\n\r\n")
        .map { it.split("\r")
            .filter(String::isNotBlank)
            .map { it.trim() }
        }

    val constraints: MutableMap<Int, List<Int>> = mutableMapOf();
    for (line in inputLines[0]) {
        val numbers = line.split("|").map { it.toInt() }
        constraints.merge(numbers[0], listOf(numbers[1]), List<Int>::plus)
    }

    val incorrectOrder = mutableSetOf<MutableList<Int>>()
    var sum = 0

    for (line in inputLines[1]) {
        // parse list.
        var numbers = line.split(",").map { it.toInt() }
        var checkValid = true

        // So going through the list, we check if there are constraints on that number.
        // If there are constraints we check if those are not before our current number in the list.
        for (number in numbers.withIndex()) {
            for (constraint in constraints[number.value]?: emptyList()) {
                if (numbers.subList(0, number.index).toSet().contains(constraint)) {
                    checkValid = false
                    incorrectOrder.add(numbers.toMutableList())
                    break
                }
            }
        }
        // apparently all list have an uneven size.
        if (checkValid) sum += numbers[numbers.size / 2]
    }
    println("Solution part 1 ${sum}")

    // Underneath we do the same check as above, but if it fails we move the constraint number after the number we are checking.
    // TODO look into topological sorting.
    var sum2 = 0
    for (numbers in incorrectOrder) {
        while (true) {
            var checkValid = true
            var swap = -1
            for (number in numbers.withIndex()) {
                for (constraint in constraints[number.value]?: emptyList()) {
                    if (numbers.subList(0, number.index).contains(constraint)) {
                        swap = constraint
                        checkValid = false
                        break
                    }
                }
                if (!checkValid) {
                    numbers.remove(swap)
                    numbers.add(number.index, swap)
                    break
                }
            }
            if (checkValid) break
        }

        sum2 += numbers[numbers.size / 2]
    }

    println("Solution part 2 ${sum2}")
}
