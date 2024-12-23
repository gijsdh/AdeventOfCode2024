import java.util.*
import kotlin.math.abs

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val map = input.lines()
        .map {
            it.split("")
                .filter(String::isNotBlank)

        }.filter { it.isNotEmpty() }


    var start = Pair(-1, -1)
    var end = Pair(-1, -1)
    maxI = map.size
    maxJ = map[0].size

    for (i in map.indices) {
        for (j in map[0].indices) {
            if (map[i][j] == "S") start = Pair(i, j)
            if (map[i][j] == "E") end = Pair(i, j)
        }
    }

    val route = getPath(start, map, end)
    println(simulateCheating(route, 2))
    println(simulateCheating(route, 20))
}

// we run through all the nodes of the route, and BFS to all reachable nodes which also lie on the route.
// Then by using the saved distance travelled map, we can calculate how much that cheating route saved.
// The BFS is expensive for 20 depth still runs in 2s, maybe just simulate two ranges (-21,21) on i and j would also work.
private fun simulateCheating(
    route: MutableMap<Pair<Int, Int>, Long>,
    depth: Int
): Int {
    var count = 0
    for (key in route.keys) {
        val set = mutableSetOf<Pair<Int, Int>>()
        bfsReachable(key.first, key.second, depth, set)
        for (cheat in set) {
            if (route.containsKey(cheat)) {
                val saved = route[cheat]!! - route[key]!! - manhattanDistance(cheat, key)
                if (saved >= 100) count++
            }
        }
    }
    return count
}

private var maxI: Int = -1
private var maxJ: Int = -1

// 983905 correct

fun manhattanDistance(a: Pair<Int, Int>, b: Pair<Int, Int>): Int = abs(a.first - b.first) + abs(a.second - b.second)

// BUG DFS with depth.

private fun bfsReachable(
    i: Int,
    j: Int,
    depth: Int,
    set: MutableSet<Pair<Int, Int>>
) {
    val ith = intArrayOf(0, 1, -1, 0)
    val jth = intArrayOf(1, 0, 0, -1)

    val dequeue = LinkedList<Pair<Int, Int>>();
    val start = Pair(i, j)
    dequeue.add(start)

    while (dequeue.isNotEmpty()) {
        var (x, y) = dequeue.pop()

        if (Pair(x, y) in set) continue
        set.add(Pair(x, y))

        for (k in ith.indices) {
            val X = x + ith[k]
            val Y = y + jth[k]

            val new = Pair(X, Y)
            if (manhattanDistance(start, new) <= depth && !set.contains(new) && isValidIndex(X, Y, maxI, maxJ)) {
                dequeue.add(new)
            }
        }
    }
}


// Dijkstra to find path, a bit too much as there is only one path.
private fun getPath(
    start: Pair<Int, Int>,
    map: List<List<String>>,
    end: Pair<Int, Int>
): MutableMap<Pair<Int, Int>, Long> {

    val comparByCost: Comparator<CostOne> = compareBy { it.costValue }
    val costQueue = PriorityQueue(comparByCost)
    var visted = mutableSetOf<Pair<Int, Int>>()

    costQueue.add(CostOne(start.first, start.second, 0))

    var route = mutableMapOf<Pair<Int, Int>, Long>()
    route[start] = 0

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

            if (isValidIndex(X, Y, map.size, map[0].size) && map[X][Y] != "#" && !visted.contains(newPos)) {
                costQueue.add(CostOne(X, Y, cell.costValue + 1))
                route[newPos] = cell.costValue + 1L

                if (end == newPos) {
                    return route
                }
            }
        }
    }
    throw Exception("here")
}

private fun isValidIndex(i: Int, j: Int, l: Int, k: Int): Boolean {
    if (i < 0 || j < 0 || i >= l || j >= k) return false
    return true
}