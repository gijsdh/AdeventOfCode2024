import java.util.*

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")

    var lines = input.lines().map { it.split(",").filter { it.isNotBlank() }.map(String::toInt) }
    var set = mutableSetOf<Pair<Int, Int>>()

//    var size = 12
//    var L = 7
//    val end = Pair(6, 6)

    var size = 1024
    var L = 71
    val end = Pair(70, 70)

    for (i in 0 until size) {
        var line = lines[i]
        set.add(Pair(line.last(), line.first()))
    }
    println(findMinimumCostPath(L, L, set, Pair(0, 0), end))

    for (i in size until lines.size) {
        val line = lines[i]
        set.add(Pair(line.last(), line.first()))

        val result = findMinimumCostPath(L, L, set, Pair(0, 0), end)
        if (result == -1L) {
            println(line)
            break
        }
    }
}


private fun findMinimumCostPath(
    LL: Int,
    RR: Int,
    maze: MutableSet<Pair<Int, Int>>,
    start: Pair<Int, Int>,
    end: Pair<Int, Int>
): Long {
    var visted = mutableSetOf<Pair<Int, Int>>()
    val comparByCost: Comparator<CostOne> = compareBy { it.costValue }
    val costQueue = PriorityQueue(comparByCost)

    costQueue.add(CostOne(start.first, start.second, 0))

    while (costQueue.isNotEmpty()) {
        val cell = costQueue.remove()
        val i: Int = cell.index_i
        val j: Int = cell.index_j

        val element = Pair(i, j)
        if (visted.contains(element)) continue
        visted.add(element)

        val ith = intArrayOf(-1, 0, 1, 0)
        val jth = intArrayOf(0, 1, 0, -1)

        for (k in ith.indices) {
            val X = i + ith[k]
            val Y = j + jth[k]
            val newPos = Pair(X, Y)

            if (isValidIndex(X, Y, LL, RR) && !visted.contains(newPos) && !maze.contains(newPos)) {
                costQueue.add(CostOne(X, Y, cell.costValue + 1))
                if (end.first == X && Y == end.second) {
                    return cell.costValue + 1
                }
            }
        }
    }
    return -1
}

class CostOne(val index_i: Int, val index_j: Int, val costValue: Long) {

}

private fun isValidIndex(i: Int, j: Int, l: Int, k: Int): Boolean {
    if (i < 0 || j < 0 || i >= l || j >= k) return false
    return true
}
