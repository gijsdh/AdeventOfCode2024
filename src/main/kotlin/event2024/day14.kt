fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")

    var regex = "p=(\\d+),(\\d+) v=(-?\\d+),(-?\\d+)".toRegex()
    val matches = regex.findAll(input, 0)

    var list = mutableListOf<Drone>()
    for (match in matches) {
        val values = match.groupValues
        list.add(Drone(Pair(values[1].toInt(), values[2].toInt()), Pair(values[3].toInt(), values[4].toInt())))
    }

    val maxJ = 101
    val maxI = 103

    // note to self % does not work for negative numbers %$^%## (-3 % 10 = -3), always use mod  (-3 mod 10 = 7).

    for (i in 1 until 10000) {
        val workList = mutableListOf<Drone>()
        for (drone in list) {
            val newPos = Pair(
                (drone.position.first + drone.speed.first).mod(maxJ),
                (drone.position.second + drone.speed.second).mod(maxI)
            )
            val newDrone = Drone(newPos, drone.speed)
            workList.add(newDrone)
        }
        list = workList

        // partOne
        if (i == 100) calculatePartOne(maxJ, maxI, list)
        partTwo(list, i, maxJ, maxI)
    }

}

private fun partTwo(list: MutableList<Drone>, i: Int, maxJ: Int, maxI: Int) {
    // need two set, otherwise we run into concurrentModification issues.
    val set = list.map { it.position }.toMutableSet()
    val setTwo = set.toMutableSet()
    var max = 0

    // Find the maximum cluster of connected drones.
    for (drone in setTwo) {
        var number = dfsConnected(drone.first, drone.second, set)
        if (number > max) max = number
    }

    // we assume the Christmas tree is some connected cluster of drones.
    // If its big it might be the tree
    if (max > 50) {
        println("--------Time ${i}")
        printMap(setTwo, maxJ, maxI)
    }
}

private fun dfsConnected(
    i: Int,
    j: Int,
    neighbours: MutableSet<Pair<Int, Int>>
): Int {
    val ith = intArrayOf(0, 1, -1, 0)
    val jth = intArrayOf(1, 0, 0, -1)
    neighbours.remove(Pair(i, j))
    var sum = 0
    for (k in ith.indices) {
        val newI = i + ith[k]
        val newJ = j + jth[k]
        if (neighbours.contains(Pair(newI, newJ))) {
            sum += 1 + dfsConnected(newI, newJ, neighbours)
        }
    }
    return sum
}

fun printMap(set: MutableSet<Pair<Int, Int>>, maxJ: Int, maxI: Int) {
    for (i in 0 until maxI) {
        for (j in 0 until maxJ ) {
            if (set.contains(Pair(j, i))) {
                print("#")
            } else {
                print(".")
            }
        }
        println()
    }
}


private fun calculatePartOne(maxJ: Int, maxI: Int, list: MutableList<Drone>) {
    val middleJ = maxJ / 2
    val middleI = maxI / 2

    val Q = IntArray(4) { 0 }
    for (drone in list) {
        val position = drone.position
        when {
            position.first < middleJ && position.second < middleI -> Q[0]++
            position.first > middleJ && position.second < middleI -> Q[1]++
            position.first < middleJ && position.second > middleI -> Q[2]++
            position.first > middleJ && position.second > middleI -> Q[3]++
        }
    }
    println("Solution Part one ${Q.reduce { i, j -> i * j }}")
}

class Drone(var position: Pair<Int, Int>, var speed: Pair<Int, Int>) {
    override fun toString(): String {
        return "Setup(position=$position, speed=$speed)"
    }
}
