fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val map = input.lines().map { it.split("").filter(String::isNotBlank) }

    var position = Pair(0,0)
    for (i in 0 until map.size) {
        for (j in 0 until map[0].size) {
            if (map[i][j] == "^") {
                position = Pair(i,j)
                break
            }
        }
    }

    var vistited = simulate(position, map)

    println("Solution part 1 - ${vistited.size}")
}

private fun simulate(position: Pair<Int, Int>, map: List<List<String>>): MutableSet<Pair<Int, Int>> {
    var position1 = position
    var vistited = mutableSetOf<Pair<Int, Int>>()
    vistited.add(position1)
    var directions = listOf(Pair(-1, 0), Pair(0, 1), Pair(1, 0), Pair(0, -1))

    var direction = 0
    while (true) {

        var newPos =
            Pair(position1.first + directions[direction].first, position1.second + directions[direction].second)
        if (!isValidIndex(newPos, map.size, map[0].size)) break
        if (map[newPos.first][newPos.second] !in listOf(".", "^")) {
            direction = (direction + 1) % 4
            continue
        }
        vistited.add(newPos)
        position1 = newPos
    }
    return vistited
}

private fun isValidIndex(position:Pair<Int, Int>, l: Int, k: Int): Boolean {
    var i = position.first
    var j = position.second
    if (i < 0 || j < 0 || i >= l || j >= k) return false
    return true
}
