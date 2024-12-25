package event2024

import getResourceAsText

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    var lines = input.lines().map { it.split("-") }

    for (line in lines) {
        var pair = line[0] to line[1]
        map.merge(pair.first, mutableSetOf(pair.second)) { a, b -> a.plus(b) }
        map.merge(pair.second, mutableSetOf(pair.first)) { a, b -> a.plus(b) }
    }

    val partOne = map.keys
        .fold(setOf(setOf<String>())) { acc, key -> acc.plus(findTriangles(key, map)) }
        .filter { it.any { it[0] == 't' } }
        .size

    println(partOne)

    val allCliques = mutableSetOf(setOf<String>())
    bronKerbosch(mutableSetOf(), map.keys.toMutableSet(), mutableSetOf(), allCliques)
    val partTwo = allCliques
        .maxBy { it.size }
        .sorted()
        .joinToString(",")
    println(partTwo)
    if ("di,gs,jw,kz,md,nc,qp,rp,sa,ss,uk,xk,yn" != partTwo) throw Exception("shit")
}

fun findTriangles(
    key: String,
    map: MutableMap<String, Set<String>>,
): MutableSet<MutableSet<String>> {

    // Create possible triangles of vertex with possible edges.
    var pairs = map[key]!!.map { key to it }
    var result = mutableSetOf<MutableSet<String>>()

    // Check the all the edges of a possible triangle.
    // If the edges intersect add all (pair + intersect) to one of the results.
    for (set in pairs) {
        val edgesOne = map[set.first]!!
        val edgesTwo = map[set.second]!!
        val setInt = edgesOne.intersect(edgesTwo)

        for (value in setInt) {
            val local = mutableSetOf<String>()
            local.add(set.first)
            local.add(set.second)
            local.add(value)
            result.add(local)
        }
    }

    return result
}

private var map = mutableMapOf<String, Set<String>>()

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
//   \ -> remove    [1,2,3] \ [1] -> [2,3]


fun bronKerbosch(
    R: Set<String>,
    P: Set<String>,
    X: Set<String>,
    result: MutableSet<Set<String>>
) {
    if (P.isEmpty() && X.isEmpty()) {
        if (R.size > 10)
            result.add(R)
        return
    }
    val localP = P.toMutableSet()
    val localX = X.toMutableSet()

    P.forEach {
        val R1 = R.union(mutableSetOf(it))
        val P1 = localP.intersect(map[it]!!)
        val X1 = localX.intersect(map[it]!!)
        bronKerbosch(R1, P1, X1, result)
        localP.remove(it)
        localX.add(it)
    }
}


