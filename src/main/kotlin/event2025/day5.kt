fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputLines = input.split("\n\r\n")
        .map { it.split("\r")
            .filter(String::isNotBlank)
            .map { it.trim() }
        }


    var map : MutableMap<Int, List<Int>> = mutableMapOf();
    for (line in inputLines[0]) {
        var numbers = line.split("|").map { it.toInt() }
        map.merge(numbers.first(), listOf(numbers.last()), List<Int>::plus)
    }

    var sum = 0
    var incorrectOrder = mutableListOf<MutableList<Int>>()
    for (line in inputLines[1]) {
        var numbers = line.split(",").map { it.toInt() }
        var checkValid = true
        for (number in numbers.withIndex()) {
            if (map.contains(number.value)) {
                for (checks in map[number.value]!!) {
                    if (numbers.subList(0, number.index).contains(checks)) {
                        checkValid = false
                        incorrectOrder.add(numbers.toMutableList())
                        break
                    }
                }
                if (!checkValid) break
            }
        }
        if (checkValid) sum += numbers[numbers.size / 2]
    }
    println("Solution part 1 ${sum}")

    var sum2 = 0
    for (numbers in incorrectOrder) {
        var work =numbers
        while (true) {
            var checkValid = true
            var swap = -1
            for (number in work.withIndex()) {
                if (map.contains(number.value)) {
                    for (checks in map[number.value]!!) {
                        if (work.subList(0, number.index).contains(checks)) {
                            swap = checks
                            checkValid=false
                            break
                        }
                    }
                }
                if (!checkValid) {
                    work.remove(swap)
                    work.add(number.index, swap)
                    break
                }
            }
            if (checkValid) break
        }

        sum2 += work[work.size / 2]
    }

    println("Solution part 2 ${sum2}")
}

