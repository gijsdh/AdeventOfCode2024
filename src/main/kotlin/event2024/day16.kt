import java.lang.Long.min
import java.util.*

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")

    val map = input.lines()
        .map {
            it.split("")
                .filter(String::isNotBlank)

        }.filter { it.isNotEmpty() }
    println(map)

    var pos = Pair(0,0)
    var end = Pair(0,0)
    for (i in map.indices) {
        for (j in map[0].indices) {
            if (map[i][j] == "S") {
                pos = Pair(i,j)
            }
            if (map[i][j] == "E") {
                end = Pair(i,j)
            }
        }
    }

    println(pos)
    var start = Triple(pos.first,pos.second,1)

    val resultOne = findMinimumCostPath(map.map { it.toTypedArray() }.toTypedArray(), listOf(start), end)
    val costMapOneWay = resultOne.second
    val partOne = costMapOneWay[end.first][end.second]
    println(partOne)

    val starts = (0 until 4).map { Triple(end.first, end.second, it) }.toMutableList()

    val resultTwo = findMinimumCostPath(map.map { it.toTypedArray() }.toTypedArray(), starts, pos)
    val costMapBackwards = resultTwo.second
    val partTwo = costMapBackwards[pos.first][pos.second]
    println(partTwo)


    printMap(costMapOneWay)
    printMap(costMapBackwards)

    var sum = 0
    for (i in map.indices) {
        for (j in map[0].indices) {
            val l = costMapBackwards[i][j] + costMapOneWay[i][j]
            if (l == partOne || l - 1000 == partOne || l + 1000 == partTwo || l + 1 == partTwo || l - 1 == partOne || l + 2 == partTwo || l - 2 == partOne || l + 2000 == partTwo || l - 2000 == partOne) {
//              if (l == partOne){
                sum++
            }
        }
    }

    println(sum)
}

// 581 to low

private fun printMap(numbers: Array<LongArray>) {
    for (el in numbers) {
        for (num in el) {
            if(num == Long.MAX_VALUE) {
                print("#####,")
            } else {
                print(""+num.toString().padStart(5,'0') + ",")
            }
        }
        println()
    }
    println("--------------------------------------------------------------------------")
}

private fun findMinimumCostPath(maze: Array<Array<String>>, starts:List<Triple<Int,Int,Int>>, end:Pair<Int,Int>): Pair<Int,Array<LongArray>> {
//    var visted = mutableSetOf<Triple<Int,Int,Int>>()
    var visted = Array(maze.size) { BooleanArray(maze[0].size) { false } }
    var costMap = Array(maze.size) { LongArray(maze[0].size) { Long.MAX_VALUE } }

    val comparByCost: Comparator<Cost> = compareBy { it.costValue }
    val costQueue = PriorityQueue<Cost>(comparByCost)


    for (start in starts) {
        costQueue.add(Cost(start.first, start.second, start.third, 0))
        costMap[start.first][start.second] = 0L
    }

    var best = 0L

    while (costQueue.isNotEmpty()) {
        val cell: Cost = costQueue.remove()
        val i: Int = cell.index_i
        val j: Int = cell.index_j
        val direction: Int = cell.direction

        if (visted[i][j]) continue
        visted[i][j] = true
//        val triple = Triple(i, j, direction)
//        if (triple in visted) continue
//        visted.add(triple)

        val ith = intArrayOf(-1, 0, 1, 0)
        val jth = intArrayOf(0, 1, 0, -1)

        for (k in ith.indices) {
            val index_I = i + ith[k]
            val index_J = j + jth[k]

//            if(direction == k+2 || direction == k-2) continue

            if (isValidIndex(index_I, index_J, maze.size, maze[0].size)
                && !visted[index_I][index_J]
//                && Triple(index_I, index_J, direction) !in visted
                && maze[index_I][index_J] != "#") {

                var cost = 1
                if(k !=direction) cost+=1000

                costMap[index_I][index_J] = min(costMap[index_I][index_J], costMap[i][j] + cost)
                costQueue.add(Cost(index_I, index_J, k, costMap[index_I][index_J]))

                if (end.first == index_I && index_J == end.second) {
                    costMap[index_I][index_J] =
                        min(costMap[index_I][index_J], costMap[i][j] + cost)
                    return Pair(k,costMap)
                }
            }
        }
    }
    throw Exception()
}

//177520
class Cost(val index_i: Int, val index_j: Int, val direction: Int, val costValue: Long) {

}

private fun isValidIndex(i: Int, j: Int, l: Int, k: Int): Boolean {
    if (i < 0 || j < 0 || i >= l || j >= k) return false
    return true
}
