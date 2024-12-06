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

    var sum = 0
    for (i in 0 until map.size) {
        for (j in 0 until map[0].size) {
            if (map[i][j] == ".") {
                map[i][j] = "0"
                sum += simulate(position, map)
                map[i][j] = "."
            }
        }
    }

    println("Solution part 2 - ${sum}")
}

private fun simulate(position: Pair<Int, Int>, map: List<List<String>>): Int {
    var localPosition = position
    var vistited = mutableSetOf<Pair<Int, Int>>()
    vistited.add(localPosition)

    var directions = listOf(Pair(-1, 0), Pair(0, 1), Pair(1, 0), Pair(0, -1))

    var loopdetection = mutableSetOf<Triple<Int, Int, Int>>()
    var direction = 0

    // position and direction are enough to detect loops.
    loopdetection.add(Triple(position.first, position.second, direction))

    while (true) {

        var newPos =
            Pair(localPosition.first + directions[direction].first, localPosition.second + directions[direction].second)
        if (!isValidIndex(newPos, map.size, map[0].size)) return 0

        if (map[newPos.first][newPos.second] !in listOf(".", "^")) {
            direction = (direction + 1) % 4
            continue
        }
        vistited.add(newPos)

        val element = Triple(newPos.first, newPos.second, direction)
        if (loopdetection.contains(element)) return 1
        loopdetection.add(element)
        localPosition = newPos
    }
    throw Exception("mistake")
}

private fun isValidIndex(position: Pair<Int, Int>, l: Int, k: Int): Boolean {
    val i = position.first
    val j = position.second
    if (i < 0 || j < 0 || i >= l || j >= k) return false
    return true
}
