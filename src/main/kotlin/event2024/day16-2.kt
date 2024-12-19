import java.util.*

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")

    val map = input.lines()
        .map {
            it.split("")
                .filter(String::isNotBlank)

        }.filter { it.isNotEmpty() }
    println(map)

    var pos = Pair(0, 0)
    var end = Pair(0, 0)
    for (i in map.indices) {
        for (j in map[0].indices) {
            if (map[i][j] == "S") {
                pos = Pair(i, j)
            }
            if (map[i][j] == "E") {
                end = Pair(i, j)
            }
        }
    }

    println(pos)
    var start = Triple(pos.first, pos.second, 1)

    val costForward = findMinimumCostPath(map.map { it.toTypedArray() }.toTypedArray(), listOf(start), end)
    val partOne = costForward.first
    println(partOne)

//    println(costMapOneWay.second)

    val starts = (0 until 4).map { Triple(end.first, end.second, it) }.toMutableList()

    val costBackwards = findMinimumCostPath(map.map { it.toTypedArray() }.toTypedArray(), starts, pos)
    val partTwo = costBackwards.first
    println(partTwo)

    var sum = mutableSetOf<Pair<Int, Int>>()
    for (i in map.indices) {
        for (j in map[0].indices) {
            for (k in 0 until 4) {
                val exists = (costForward.second.contains(Triple(i, j, k))
                        && costBackwards.second.contains(Triple(i, j, k)))
                if (exists) {
                    val l = costForward.second[Triple(i, j, k)]!! + costBackwards.second[Triple(i, j, k)]!!
                    if (l == partOne) {
                        sum.add(Pair(i, j))
                    }
                }
            }
        }
    }

    println(sum.size)
}

// 581 to low

private fun printMap(numbers: Array<LongArray>) {
    for (el in numbers) {
        for (num in el) {
            if (num == Long.MAX_VALUE) {
                print("#####,")
            } else {
                print("" + num.toString().padStart(5, '0') + ",")
            }
        }
        println()
    }
    println("--------------------------------------------------------------------------")
}

private fun findMinimumCostPath(
    maze: Array<Array<String>>,
    starts: List<Triple<Int, Int, Int>>,
    end: Pair<Int, Int>
): Pair<Long, MutableMap<Triple<Int, Int, Int>, Long>> {
    var visted = mutableSetOf<Triple<Int, Int, Int>>()
//    var visted = Array(maze.size) { BooleanArray(maze[0].size) { false } }

    var seen = mutableMapOf<Triple<Int, Int, Int>, Long>()

    val comparByCost: Comparator<Cost> = compareBy { it.costValue }
    val costQueue = PriorityQueue<Cost>(comparByCost)


    for (start in starts) {
        costQueue.add(Cost(start.first, start.second, start.third, 0))
//        costMap[start.first][start.second] = 0L
    }

    var best = 0L

    while (costQueue.isNotEmpty()) {
        val cell: Cost = costQueue.remove()
        val i: Int = cell.index_i
        val j: Int = cell.index_j
        val direction: Int = cell.direction


        var costCell = Triple(i, j, direction)
        if (starts.size > 1) {
            costCell = Triple(i, j, (direction + 2) % 4)
        }

        if (costCell !in seen) {
            seen[costCell] = cell.costValue
        }

        val triple = Triple(i, j, direction)
        if (triple in visted) continue
        visted.add(triple)

        val ith = intArrayOf(-1, 0, 1, 0)
        val jth = intArrayOf(0, 1, 0, -1)

        val index_I = i + ith[direction]
        val index_J = j + jth[direction]

        if (isValidIndex(index_I, index_J, maze.size, maze[0].size)
            && Triple(index_I, index_J, direction) !in visted
            && maze[index_I][index_J] != "#"
        ) {
            costQueue.add(Cost(index_I, index_J, direction, cell.costValue + 1))
            if (end.first == index_I && index_J == end.second && best == 0L) {
                best = cell.costValue + 1
            }
        }
        costQueue.add(Cost(i, j, (direction - 1).mod(4), cell.costValue + 1000))
        costQueue.add(Cost(i, j, (direction + 1).mod(4), cell.costValue + 1000))

    }
    return Pair(best, seen)
}

private fun isValidIndex(i: Int, j: Int, l: Int, k: Int): Boolean {
    if (i < 0 || j < 0 || i >= l || j >= k) return false
    return true
}
