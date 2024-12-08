fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val lines = input.lines().map { it.split("").filter(String::isNotBlank) }

    // this could just be a list of lists
    var map = mutableMapOf<String, List<Pair<Int, Int>>>()
    for (i in 0 until lines.size) {
        for (j in 0 until lines[0].size) {
            if (lines[i][j] != ".") {
                map.merge(lines[i][j], listOf(Pair(i, j)), List<Pair<Int, Int>>::plus)

            }
        }
    }

    var setTwo = mutableSetOf<Pair<Int, Int>>()
    var setOne = mutableSetOf<Pair<Int, Int>>()

    for (values in map.values) {
        for (i in 0 until values.size) {
            for (j in i + 1 until values.size) {
                val one = values[i]
                val two = values[j]
                val difference = Pair((one.first - two.first), (one.second - two.second))


                // Forgot that the antenna's also count as an antinode now
                setTwo.add(one)
                setTwo.add(two)

                // expand the antinodes upwards from one.
                var up = one
                while (true) {
                    up = Pair(up.first + difference.first, up.second + difference.second)
                    if (isValidIndex(up, lines.size, lines[0].size)) setTwo.add(up)
                    else break
                }

                // expand the antinodes downwards from two.
                var down = two
                while (true) {
                    down = Pair(down.first - difference.first, down.second - difference.second)
                    if (isValidIndex(down, lines.size, lines[0].size) ) setTwo.add(down)
                    else break
                }

                // Part one -- //
                up = Pair(one.first + difference.first, one.second + difference.second)
                down = Pair(two.first - difference.first, two.second - difference.second)
                for (possible in listOf(up, down)) {
                    if (isValidIndex(possible, lines.size, lines[0].size)) setOne.add(possible)
                }
            }
        }
    }

    for (i in 0 until lines.size) {
        for (j in 0 until lines[0].size) {
            if (Pair(i, j) in setTwo) print("#") else print(".")
        }
        println()
    }
    println("Solution part one ${setOne.size}")
    println("Solution part Two ${setTwo.size}")

}

private fun isValidIndex(position: Pair<Int, Int>, l: Int, k: Int): Boolean {
    val i = position.first
    val j = position.second
    if (i < 0 || j < 0 || i >= l || j >= k) return false
    return true
}

