import java.util.*

fun main(args: Array<String>) {
    val input = getResourceAsText("testInput.txt")
    val inputLines = input.split("\n\r\n")
        .map { it.split("\r")
            .filter(String::isNotBlank)
            .map { it.trim() }
        }

    val constraintsAfter: MutableMap<Int, List<Int>> = mutableMapOf();
    val constraintsBefore: MutableMap<Int, List<Int>> = mutableMapOf();


    for (line in inputLines[0]) {
        val numbers = line.split("|").map { it.toInt() }
        constraintsAfter.merge(numbers[0], listOf(numbers[1]), List<Int>::plus)
        constraintsBefore.merge(numbers[1], listOf(numbers[0]), List<Int>::plus)

    }
    println(constraintsAfter)
    val incorrectOrder = mutableSetOf<MutableList<Int>>()
    var sum = 0
    var sum2 = 0

    for (line in inputLines[1]) {
        // parse list.
        var numbers = line.split(",").map { it.toInt() }

        var ok = true
        for ((i, x) in numbers.withIndex()) {
            for ((j, y) in numbers.withIndex()) {
                if (j < i && (constraintsAfter[x]?: emptyList<Int>()).contains(y)) {
                    ok = false
                }
            }
        }
        if (ok) {
            sum += numbers[numbers.size / 2]
        } else {



            // Find the degrees of the nodes.
            val Degree: MutableMap<Int, Int> = mutableMapOf();
            for (number in numbers) {
                Degree[number] = (constraintsBefore[number] ?: emptyList<Int>()).toSet().intersect(numbers.toSet()).size
            }

//           Kahn's algorithm
//            L ← Empty list that will contain the sorted elements
//            S ← Set of all nodes with no incoming edge
//
//            while S is not empty do
//                remove a node n from S
//                        add n to L
//                        for each node m with an edge e from n to m do
//                remove edge e from the graph
//                        if m has no other incoming edges then
//                            insert m into S

            val S: LinkedList<Int> = LinkedList<Int>();
            val L: MutableList<Int> = mutableListOf()

            for (number in numbers) {
                if (Degree[number] == 0) {
                    S.add(number)
                }
            }

            while (S.isNotEmpty()) {
                var current = S.removeFirst()
                L.add(current)
                for (y in (constraintsAfter[current] ?: emptyList())) {
                    if (y in Degree) {
                        Degree[y] = Degree[y]!! - 1
                        if(Degree[y] == 0) {
                            S.add(y)
                        }
                    }
                }
            }
            sum2 += L[L.size/2]

        }
    }

    println(sum)
    println(sum2)
}
