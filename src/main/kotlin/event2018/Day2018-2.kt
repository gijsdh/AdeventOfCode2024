fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputLines = input.lines();

    println(inputLines)

    var counter3 =0
    var counter4 =0

    for (line in inputLines) {
        var map :MutableMap<Char, Int> = mutableMapOf()
        for (char in line) {
            map.merge(char, 1, Int::plus)
        }
        var count2 =true
        var count3 =true
        for (value in map.values) {
            if (value == 2 && count2) {
                count2 = false
                counter3 ++
            }
            if (value == 3 && count3) {
                count3 = false
                counter4 ++
            }
        }
    }

    println("solution part 1: ${counter4*counter3}")


    for (line in inputLines) {
        for (line2 in inputLines) {
            if (line2 == line) continue
            var oneMistake = false
            var succes = true
            var index = -1
            for (char in line.withIndex()) {
                if (char.value != line2[char.index]) {
                    if (oneMistake) {
                        succes = false;
                        break
                    }
                    index = char.index
                    oneMistake = true
                }
            }
            if (succes) throw Exception("solution part 2 ${line.substring(0, index) + line.substring(index + 1, line.length)}")
        }
    }
}


