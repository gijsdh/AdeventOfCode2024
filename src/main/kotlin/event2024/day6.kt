fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val map = input.lines().map { it.split("").filter(String::isNotBlank).toMutableList() }.toMutableList()

    var position = Pair(0, 0)
    for (i in 0 until map.size) {
        for (j in 0 until map[0].size) {
            if (map[i][j] == "^") {
                position = Pair(i, j)
                break
            }
        }
    }

    // Improvement to run time is to only place obstacles on initial path of the guard.
    var sum = 0
    val simulatePartOne = simulate_part_one(position, map)
    simulatePartOne.forEach { (i, j) ->
        map[i][j] = "0"
        sum += simulate(position, map)
        map[i][j] = "."
    }
    println("Solution part 1 - ${simulatePartOne.size}")
    println("Solution part 2 - ${sum}")
}

private fun simulate(position: Pair<Int, Int>, map: List<List<String>>): Int {
    var localPosition = position

    val directions = listOf(Pair(-1, 0), Pair(0, 1), Pair(1, 0), Pair(0, -1))
    var direction = 0 // starting direction is up, (-1,0) in our set-up the top left of matrix is 0,0

    // position and direction are enough to detect loops.
    val loopDetection = mutableSetOf<Triple<Int, Int, Int>>()
    loopDetection.add(Triple(position.first, position.second, direction))

    while (true) {
        var newPos =
            Pair(localPosition.first + directions[direction].first, localPosition.second + directions[direction].second)
        if (!isValidIndex(newPos, map.size, map[0].size)) return 0

        if (map[newPos.first][newPos.second] !in listOf(".", "^")) {
            direction = (direction + 1) % 4
            continue // it possible that we need to turn twice.
        }

        val element = Triple(newPos.first, newPos.second, direction)
        if (loopDetection.contains(element)) return 1
        loopDetection.add(element)
        localPosition = newPos
    }
    throw Exception("mistake")
}

private fun simulate_part_one(position: Pair<Int, Int>, map: List<List<String>>): MutableSet<Pair<Int, Int>> {
    var currentPos = position
    val visited = mutableSetOf<Pair<Int, Int>>()
    visited.add(currentPos)
    val directions = listOf(Pair(-1, 0), Pair(0, 1), Pair(1, 0), Pair(0, -1))

    var direction = 0
    while (true) {
        val newPos =
            Pair(currentPos.first + directions[direction].first, currentPos.second + directions[direction].second)

        if (!isValidIndex(newPos, map.size, map[0].size)) break
        if (map[newPos.first][newPos.second] !in listOf(".", "^")) {
            direction = (direction + 1) % 4
            continue
        }
        visited.add(newPos)
        currentPos = newPos
    }
    return visited
}

private fun isValidIndex(position: Pair<Int, Int>, l: Int, k: Int): Boolean {
    val i = position.first
    val j = position.second
    if (i < 0 || j < 0 || i >= l || j >= k) return false
    return true
}
