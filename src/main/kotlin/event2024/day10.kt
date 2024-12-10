fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val map = input.lines().map { it.split("").filter(String::isNotBlank).map(String::toInt) }

    var sum = 0
    var sum2 = 0
    for (i in map.indices) {
        for (j in map[0].indices) {
            if (map[i][j] == 0) {
                val set = mutableSetOf<Pair<Int, Int>>()
                sum2 += findpaths(map, i, j, set)
                sum += set.size
            }
        }
    }

    println("Solution part one ${sum}")
    println("Solution part Two ${sum2}")
}

private fun findpaths(map: List<List<Int>>, i: Int, j: Int, set: MutableSet<Pair<Int, Int>>): Int {
    val ith = intArrayOf(0, 1, -1, 0)
    val jth = intArrayOf(1, 0, 0, -1)

    var sum = 0
    for (k in ith.indices) {
        val index_I = i + ith[k]
        val index_J = j + jth[k]
        if (isValidIndex(index_I, index_J, map.size, map[0].size) && (map[index_I][index_J] - map[i][j]) == 1) {
            if (map[index_I][index_J] == 9) {
                set.add(Pair(index_I, index_J))
                sum++
            } else {
                sum += findpaths(map, index_I, index_J, set)
            }
        }
    }
    return sum
}

private fun isValidIndex(i: Int, j: Int, l: Int, k: Int): Boolean {
    if (i < 0 || j < 0 || i >= l || j >= k) return false
    return true
}