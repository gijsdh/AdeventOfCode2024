fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val map = input.lines()
        .map {
            it.split("")
                .filter(String::isNotBlank)
        }

    // pad the map as I want to have be able to save the coordinates of neighbouring cell for the fences.
    var paddedMap = Array(map.size + 2) { Array<String>(map[0].size + 2) { "0" } }
    for (i in 0 until map.size) {
        for (j in 0 until map[0].size) {
            paddedMap[i + 1][j + 1] = map[i][j]
        }
    }


    var seen = mutableSetOf<Pair<Int, Int>>()
    println(paddedMap.map { it.toMutableList() }.toMutableList())

    var sum = 0
    var sum2 = 0
    for (i in 1 until paddedMap.size - 1) {
        for (j in 1 until paddedMap[0].size - 1) {
            if (Pair(i, j) !in seen) {
                val area = mutableSetOf<Pair<Int, Int>>()
                val neighbours = mutableSetOf<Triple<Int, Int, Int>>()
                dfsArea(paddedMap, i, j, area, neighbours)
                val side = countSide(neighbours)
                sum += area.size * neighbours.size
                sum2 += area.size * side
                seen.addAll(area)
            }
        }
    }

    println(sum)
    println(sum2)
}

// this DFS side works as the following cells with coordinates (i,j, direction) are saved in the neighbours set.
// So for underneath shape C we have teh set with following neighbours (a1,b1,c1,c2,d1,e1,f1,g1,g2,h1).
//         |d1|
//    --   +-+
//   |c1|  |C| |e1| +|f1|
//   |  |  + +-+  __
//   |c1|  |C C| |g1|
//    --   +-+ + |  |
// |a1|+|b1| |C| |g2|
//           +-+  --
//           |h1|
// So moving through neighbouring cells, do an DFS look up all reachable cells from a single and remove them from the set.
// These cells all share the same side or k. Repeat the DFS for al cells in the neighbours set.
// Then just count unique sides, as you remove cells belonging to one direction you cannot count them double.

fun countSide(neighbours: MutableSet<Triple<Int, Int, Int>>): Int {
    val work = neighbours.toMutableSet()
    var side = 0
    for (triple in neighbours) {
        if (triple in work) {
            side += 1
            dfsSides(triple.first, triple.second, triple.third, work)
        }
    }
    return side;
}


private fun dfsSides(
    i: Int,
    j: Int,
    d: Int,
    neighbours: MutableSet<Triple<Int, Int, Int>>
) {
    val ith = intArrayOf(0, 1, -1, 0)
    val jth = intArrayOf(1, 0, 0, -1)
    neighbours.remove(Triple(i, j, d))
    for (k in ith.indices) {
        val newI = i + ith[k]
        val newJ = j + jth[k]
        if (neighbours.contains(Triple(newI, newJ, d))) {
            dfsSides(newI, newJ, d, neighbours)
        }
    }
}

// We keep track of all cells of in area and of all neighbouring cells.
// Some of the neighbouring cells are counted double but are unique (and have a fence) due to different orientation of the area cell.
//         +--+--+
//         |C1 C2|
//         +--+  +
//  a1 + b1   |C3|  So here a1 is seen from C1 and b1 is seen from C3. Both have a different direction as a1 is seen from above and b1 is seen from the left.
//            +--+
private fun dfsArea(
    map: Array<Array<String>>,
    i: Int,
    j: Int,
    area: MutableSet<Pair<Int, Int>>,
    neighbours: MutableSet<Triple<Int, Int, Int>>
) {
    val ith = intArrayOf(0, 1, -1, 0)
    val jth = intArrayOf(1, 0, 0, -1)
    val direction = intArrayOf(0, 1, 2, 3)

    area.add(Pair(i, j))

    for (k in ith.indices) {
        val index_I = i + ith[k]
        val index_J = j + jth[k]
        val newPair = Pair(index_I, index_J)
        if (map[index_I][index_J] == map[i][j] && newPair !in area) {
            area.add(newPair)
            dfsArea(map, index_I, index_J, area, neighbours)
        } else if (newPair !in area) {
            neighbours.add(Triple(index_I, index_J, direction[k]))
        }
    }
}

private fun isValidIndex(i: Int, j: Int, l: Int, k: Int): Boolean {
    if (i < 0 || j < 0 || i >= l || j >= k) return false
    return true
}