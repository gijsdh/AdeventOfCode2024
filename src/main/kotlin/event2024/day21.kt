import java.util.*

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")

    var lines = input.lines()

    var numbers = listOf(
        listOf('7', '8', '9'),
        listOf('4', '5', '6'),
        listOf('1', '2', '3'),
        listOf('#', '0', 'A')
    )


    var directions = listOf(
        listOf('#', '^', 'A'),
        listOf('<', 'v', '>')
    )
    // Today was shit, 5 hours gone.
    // Bug fasted path of Dijkstra and then permutate does not work, as the grid is not symmetric

    extractMap(directions, directionsMap)
    extractMap(numbers, numbersMap)

    compute(lines, 3)
    compute(lines, 26)

}

private fun extractMap(directions: List<List<Char>>, map: MutableMap<Pair<Char, Char>, List<String>>) {
    // Map to list of pairs of indexes in the map.
    var coordinates =
        directions
            .withIndex()
            .map { one -> one.value.indices.map { one.index to it } }.flatten()
    for (start in coordinates) {
        for (end in coordinates) {
            val (i, j) = start
            val (l, k) = end
            if (directions[i][j] != '#' && directions[l][k] != '#') {
                val path = getPath(start, directions, end)
                    .distinct().map { it + "A" }
                map[directions[i][j] to directions[l][k]] = path
            }
        }
    }
}

private fun compute(lines: List<String>, depth: Int) {
    var sum = 0L
    for (code in lines) {
        val solve = solve(code, 0, depth)
        val result = solve * code.substring(0, 3).toLong()
        sum += result
    }
    println(sum)
}


var numbersMap = mutableMapOf<Pair<Char, Char>, List<String>>()
var directionsMap = mutableMapOf<Pair<Char, Char>, List<String>>()
var memo = mutableMapOf<Triple<String, Int, Int>, Long>()

fun solve(code: String, depth: Int, max: Int): Long {
    if (Triple(code, depth, max) in memo) {
        return memo[Triple(code, depth, max)]!!
    }

    if (depth == max) {
        memo[Triple(code, depth, max)] = code.length.toLong()
        return code.length.toLong()
    }

    var map = directionsMap
    if (depth == 0) map = numbersMap
    var min = 0L

    var current = 'A'

    for (letter in code) {
        val sequences = map[current to letter]!!
        val result = mutableSetOf<Long>()
        for (seq in sequences) {
            result.add(solve(seq, depth + 1, max))
        }
        min += result.min()
        current = letter
    }

    memo[Triple(code, depth, max)] = min
    return min
}

// Dijkstra to find path, a bit too much.
private fun getPath(
    start: Pair<Int, Int>, map: List<List<Char>>, end: Pair<Int, Int>
): List<String> {
    if (start == end) return listOf("")

    val comparByCost: Comparator<CosPath> = compareBy { it.costValue }
    val costQueue = PriorityQueue(comparByCost)
    var visted = mutableSetOf<Triple<Int, Int, String>>()
    var paths = mutableListOf<String>()

    costQueue.add(CosPath(start.first, start.second, "", 0))
    var min = Long.MAX_VALUE

    val ith = intArrayOf(-1, 0, 1, 0)
    val jth = intArrayOf(0, 1, 0, -1)
    val dth = arrayOf("^", ">", "v", "<")

    while (costQueue.isNotEmpty()) {
        val cell = costQueue.remove()
        val i: Int = cell.index_i
        val j: Int = cell.index_j

        val element = Triple(i, j, cell.path)
        if (visted.contains(element)) continue
        if (cell.costValue > min) continue
        visted.add(element)

        for (k in ith.indices) {
            val X = i + ith[k]
            val Y = j + jth[k]
            val newPos = Pair(X, Y)
            val triple = Triple(X, Y, cell.path)
            if (isValidIndex(X, Y, map.size, map[0].size) && map[X][Y] != '#' && !visted.contains(triple)) {
                costQueue.add(CosPath(X, Y, cell.path.plus(dth[k]), cell.costValue + 1))

                if (end == newPos) {
                    if (cell.costValue + 1 < min) min = cell.costValue + 1
                    paths.add(cell.path.plus(dth[k]))
                }
            }
        }
    }
    return paths
}


class CosPath(val index_i: Int, val index_j: Int, var path: String, val costValue: Long) {

}
private fun isValidIndex(i: Int, j: Int, l: Int, k: Int): Boolean {
    if (i < 0 || j < 0 || i >= l || j >= k) return false
    return true
}

//https://rosettacode.org/wiki/Permutations
fun <T> permute(input: List<T>): List<List<T>> {
    if (input.size == 1) return listOf(input)
    val perms = mutableListOf<List<T>>()
    val toInsert = input[0]
    for (perm in permute(input.drop(1))) {
        for (i in 0..perm.size) {
            val newPerm = perm.toMutableList()
            newPerm.add(i, toInsert)
            perms.add(newPerm)
        }
    }
    return perms
}
