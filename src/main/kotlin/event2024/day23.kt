package event2024

import getResourceAsText

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")

    var lines = input.lines().map { it.split("-") }


    for (line in lines) {
        var pair = line[0] to line[1]
        map.merge(pair.first, mutableSetOf(pair.second)) { a, b -> a.plus(b).toMutableSet() }
        map.merge(pair.second, mutableSetOf(pair.first)) { a, b -> a.plus(b).toMutableSet() }
    }

    println(map.keys.size)

    var partOne = map.keys
        .fold(setOf(setOf<String>())) { acc, key -> acc.plus(findTriangles(key, map)) }
        .filter { it.any { it[0] == 't' } }
        .size

    println(partOne)

    var allCliques = mutableSetOf(mutableSetOf<String>())
    bronKerbosch(mutableSetOf(), map.keys.toMutableSet(), mutableSetOf(), allCliques)
    println(allCliques.maxBy { it.size }.sorted().joinToString(","))
}

fun findTriangles(
    key: String,
    map: MutableMap<String, MutableSet<String>>,
): MutableSet<MutableSet<String>> {

    // Create possible triangles of vertex with possible edges.
    var pairs = map[key]!!.map { key to it }
    var result = mutableSetOf<MutableSet<String>>()

    // Check the all the edges of a possible triangle.
    // If the edges intersect add all (pair + intersect) to one of the results.
    for (set in pairs) {
        var edgesOne = map[set.first]!!
        var edgesTwo = map[set.second]!!
        var setInt = edgesOne.intersect(edgesTwo)

        for (value in setInt) {
            var local = mutableSetOf<String>()
            local.add(set.first)
            local.add(set.second)
            local.add(value)
            result.add(local)
        }
    }

    return result
}

var map = mutableMapOf<String, MutableSet<String>>()

// https://en.wikipedia.org/wiki/Bron%E2%80%93Kerbosch_algorithm
// An algorithm to find all cliques in a graph.
// A clique is a subset of a graph where all vertexes have edges towards each other. So [a <-> b] [b<-> c] [c <-> a],  [a,b,c] is a clique.
// Every year my graph theory get slightly better.
//
// algorithm BronKerbosch1(R, P, X) is
//    if P and X are both empty then
//        report R as a maximal clique
//    for each vertex v in P do
//        BronKerbosch1(R ⋃ {v}, P ⋂ N(v), X ⋂ N(v))
//        P := P \ {v}
//        X := X ⋃ {v}
//
//   ⋃ -> UNION or [1,2,3] ⋃ [4,5] -> [1,2,3,4,5]
//   ⋂ -> intersect or [1,2,3] ⋃ [1,3] -> [1,3]
//   \ -> remove    [1,2,3] ⋃ [1] -> [2,3]


fun bronKerbosch(
    R: MutableSet<String>,
    P: MutableSet<String>,
    X: MutableSet<String>,
    result: MutableSet<MutableSet<String>>
) {
    if (P.isEmpty() && X.isEmpty()) {
        if (R.size > 10)
            result.add(R)
        return
    }
    var localP = P.toMutableSet()
    var localX = X.toMutableSet()
    for (v in P) {
        val R1 = R.union(mutableSetOf(v)).toMutableSet()
        var P1 = localP.intersect(map[v]!!).toMutableSet()
        var X1 = localX.intersect(map[v]!!).toMutableSet()
        bronKerbosch(R1, P1, X1, result)
        localP.remove(v)
        localX.add(v)
    }
}


