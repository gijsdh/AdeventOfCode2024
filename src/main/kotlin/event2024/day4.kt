fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    var map = input.lines().map { it.split("").filter(String::isNotBlank) }

    var count = 0
    var count2 = 0
    for (i in 0 until map.size) {
        for (j in 0 until map[0].size) {
            count+= findNeighbours(map, Pair(i, j))
            count2+= findNeighbours2(map, Pair(i, j))
        }
    }
    println("Solution part 1 ${count}")
    println("Solution part 2 ${count2}")
}


private fun findNeighbours(map: List<List<String>>, position: Pair<Int, Int>): Int {
    if (map[position.first][position.second] != "X") return 0

    val ith = intArrayOf(0, 1, -1, 0, -1, 1, 1, -1)
    val jth = intArrayOf(1, 0, 0, -1, -1, 1, -1, 1)

    var counter = 0
    for (k in ith.indices) {
        var pos = Pair(position.first, position.second)
        var result = map[pos.first][pos.second]
        for (i in 0 until 3) {
            val index_I = pos.first + ith[k]
            val index_J = pos.second + jth[k]

            if (!isValidIndex(index_I, index_J, map.size, map[0].size)) break
            pos = Pair(index_I, index_J)
            result += map[index_I][index_J]
        }
        if (result == "XMAS") {
            counter++
        }
    }
    return counter
}

private fun isValidIndex(i: Int, j: Int, l: Int, k: Int): Boolean {
    if (i < 0 || j < 0 || i >= l || j >= k) return false
    return true
}

private fun findNeighbours2(map: List<List<String>>, position: Pair<Int, Int>): Int {
    if (map[position.first][position.second] != "A") return 0

    val ith = intArrayOf(-1, 1, 1, -1)
    val jth = intArrayOf(-1, 1, -1, 1)
    var start = listOf(Pair(1, 1), Pair(-1, -1), Pair(-1, 1), Pair(1, -1))

    var counter = 0
    for (k in ith.indices) {
        // start in opposite corner of direction.
        val startI = position.first + start[k].first
        val startJ = position.second + start[k].second
        if (!isValidIndex(startI, startJ, map.size, map[0].size)) continue

        var pos = Pair(startI, startJ)
        var result = map[pos.first][pos.second]

        for (i in 0 until 2) {
            val index_I = pos.first + ith[k]
            val index_J = pos.second + jth[k]

            if (!isValidIndex(index_I, index_J, map.size, map[0].size)) break

            pos = Pair(index_I, index_J)
            result += map[index_I][index_J]
        }

        if (result == "MAS") {
            counter++
            if (counter > 1) return 1
        }
    }
    return 0
}

