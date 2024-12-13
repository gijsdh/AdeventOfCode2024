import event2024.cramer
import kotlin.math.min

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")


    val m = arrayOf(
        doubleArrayOf(26.0, 67.0),
        doubleArrayOf(66.0, 21.0),
    )
    val d = doubleArrayOf(10000000012748.0, 10000000012176.0)

    val (w, x) = cramer(m, d)


//    Button A: X+94, Y+34
//    Button B: X+22, Y+22
//    Prize: X=10000000008400, Y=10000000005400

//    val m = arrayOf(
//        doubleArrayOf(94.0, 22.0),
//        doubleArrayOf(34.0,  22.0),
//    )
//    val d = doubleArrayOf(10000000008400.0, 10000000005400.0)
//
//    val (w, x,) = cramer(m, d)


    println(w.toLong().toDouble() == w)
    println(x.toLong().toDouble() == x)
    println("w = $w, x = $x")


//    Button A: X+26, Y+66
//    Button B: X+67, Y+21
//    Prize: X=10000000012748, Y=10000000012176

//    Input interpretation
//            solve 26 A + 67 B = 10000000012748
//    66 A + 21 B = 10000000012176 for A, B
//    Result
//    A = 118679050709 and B = 103199174542

    // Nice way to parse input, not yet time efficient.
    val regex =
        "Button A: X\\+(\\d+), Y\\+(\\d+)\\r\\nButton B: X\\+(\\d+), Y\\+(\\d+)\\r\\nPrize: X=(\\d+), Y=(\\d+)".toRegex()
    val findAll = regex.findAll(input, 0)

    var list: MutableList<Setup> = mutableListOf()
    for (match in findAll) {
        val buttonA = Pair(match.groupValues[1].toInt(), match.groupValues[2].toInt())
        val buttonB = Pair(match.groupValues[3].toInt(), match.groupValues[4].toInt())
        val prize = Pair(match.groupValues[5].toInt(), match.groupValues[6].toInt())

        list.add(Setup(buttonA, buttonB, prize))
    }

    var sum = 0
    var sum2 = 0L
    for (setup in list) {
        var map = mutableMapOf<Pair<Int, Int>, Int>()
        val findMinimumCost = findMinimumCost(0, 0, setup, map)
        // max button presses = 100 so cost is max 400
        if (findMinimumCost < 401) sum += findMinimumCost

        // We solve it as a system of linear equations.
        // We need to find underneath. As it's a linear system it only has one solution (except if lines are parallel then no solution exists).
        //    Button A: X+26, Y+66
        //    Button B: X+67, Y+21
        //    Prize: X=10000000012748, Y=10000000012176
        // turns into:
        // 26A + 67B = 10000000012748
        // 66A + 21B = 10000000012176
        // So we solve here for A and B.
        // If A and B are whole/integer numbers there is a solution we are interested in.

        val matrix = arrayOf(
            doubleArrayOf(setup.buttonA.first.toDouble(), setup.buttonB.first.toDouble()),
            doubleArrayOf(setup.buttonA.second.toDouble(), setup.buttonB.second.toDouble()),
        )
        val d =
            doubleArrayOf(setup.prize.first.toDouble() + 10000000000000, setup.prize.second.toDouble() + 10000000000000)
        val (pressA, pressB) = cramer(matrix, d)

        // We need to check if the solution to the linear systems is actual a whole number.
        // As the numbers get kind of big computer precision might be an issue.
        // But apparently underneath just works to check.
        // So converting to Long and then to double should not matter for whole number,
        // but for some decimal number this would lose some information due to rounding.
        if (pressA.toLong().toDouble() == pressA && pressB.toLong().toDouble() == pressB) {
            sum2 += pressA.toLong() * 3 + pressB.toLong()
        }

    }
    println("Solution part one ${sum}")
    println("Solution part Two ${sum2}")
}

// Maybe a bit to fancy, just looping 100 times would have been fine.
private fun findMinimumCost(x: Int, y: Int, setup: Setup, memoization: MutableMap<Pair<Int, Int>, Int>): Int {
    if (Pair(x, y) in memoization) return memoization[Pair(x, y)]!!
    if (x == setup.prize.first && y == setup.prize.second) return 0
    if (x > setup.prize.first || y > setup.prize.second) return 100000

    var min = min(
        3 + findMinimumCost(x + setup.buttonA.first, y + setup.buttonA.second, setup, memoization),
        1 + findMinimumCost(x + setup.buttonB.first, y + setup.buttonB.second, setup, memoization)
    )
    memoization[Pair(x, y)] = min

    return min
}

class Setup(var buttonA: Pair<Int, Int>, var buttonB: Pair<Int, Int>, var prize: Pair<Int, Int>) {
    override fun toString(): String {
        return "Setup(buttonA=$buttonA, buttonB=$buttonB, prize=$prize)"
    }
}

