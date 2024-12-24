package event2024

import getResourceAsText

fun main() {
    val input = getResourceAsText("input.txt")

    val parsed = input.split("\n\r\n")
    val result: MutableMap<String, Int> = mutableMapOf();
    val (map: MutableMap<String, Gate>, gates: Set<Gate>) = parseInput(parsed)

    val list = mutableListOf<Pair<String, Int>>()
    for (entry in map.keys) {
        list.add(entry to solve(entry, map, result))
    }

    val partOne = list.filter { it.first[0] == 'z' }
        .sortedBy { it.first }
        .reversed()
        .map { it.second }
        .joinToString("")
        .toLong(2)
    println(partOne)

    // Part 2 =================

    val resultZ = toDecimal(result, 'z').toString(2)
    val resultCombi = (toDecimal(result, 'x') + toDecimal(result, 'y')).toString(2)
    println("result $resultZ")
    println("sum    $resultCombi")
    val xored = resultZ.toLong(2)
        .xor(resultCombi.toLong(2))
        .toString(2).padStart(resultCombi.length, '0')

    println("Xored  $xored")

    for ((i, bit) in resultCombi.withIndex()) {
        if (bit != resultZ[i]) {
            println("bit problem ${resultCombi.length - i - 1}")
        }
    }


//    solve("z08", map, mutableMapOf<String, Int>(), true)


    // Shit problem, following reddit again.
    // All z's are the result off local full adder circuits (this seems to satisfy all inputs), reddit taught me:
    // schematic representation of full adder circuit:
    //
    //    x >--+------>+------+
    //         |       | XOR  |>-+----->+------+
    //    y >--|---+-->+------+  v      | XOR  |>----- z
    //         |   |             |   +->+------+
    //         v   v             |   |            +----->+-----+
    //         |   +-->+------+  |   |            |      | OR  |>------ Cout
    //         |       | AND  |>-|---|------------+   +->+-----+
    //         +------>+------+  |   |                |
    //                           +---|--->+------+    |
    //                               |    | AND  |>---+
    //                               +--->+------+
    //                               ^
    //        Cin>-------------------+
    //
    //
    // we can extract the following rules:
    // 1. we know that all x and y need to be attached to XOR or an AND.
    // 2. And from above we learn that all z needs to be attached to XOR
    // 3. All x+y XOR and x+y AND need to be followed by an AND or an OR port.
    // 4. All XOR need to have the following configuration:
    //          _____
    //     Y---|     |
    //         | XOR |-------Z
    //     X---|_____|
    //
    // so either x or y in front or z as output.
    // if any of the input values does not follow this we, it's a wrong configuration and we report output (as these have been changed).
    // From the input we also learn that x01 + y01, x02 + y02, x03 + y03 start the adders

    val wrong = mutableListOf<String>()
    for (i in resultCombi.indices) {
        val id = i.toString().padStart(2, '0')

        // get Al gates that take that have as input X0i + Y0i || Y0i + X0i
        val xAndYGates = gates.filter { (it.x == "x$id" && it.y == "y$id") || (it.y == "y$id" && it.x == "x$id") }

        // All XOR gate with X0i + Y0i
        val xor = xAndYGates
            .firstOrNull() { it.opr == "XOR" } ?: Gate()


        // All AND gate with X0i + Y0i
        val and = xAndYGates
            .firstOrNull() { it.opr == "AND" } ?: Gate()

        // So all z must be attached to XOR to satisfy the full adder circuit.
        val z = map["z$id"]!!
        if (z.opr != "XOR" && i < resultCombi.length - 1) wrong.add("z$id")

        if (xor.isEmpty() && and.isEmpty()) continue

        // All x+y AND gates need to be followed with an OR. As y00 + x00 acts a half adder circuit.
        val or = gates.firstOrNull { it.x == and.z || it.y == and.z } ?: Gate()
        if (or.isNotEmpty() && or.opr != "OR" && i > 0) wrong.add(and.z)


        // All x+y XOR gates need to be followed with an XOR or an AND (or !OR).
        val xorAfter = gates.firstOrNull { it.x == xor.z || it.y == xor.z } ?: Gate()
        if (xorAfter.isNotEmpty() && xorAfter.opr == "OR") wrong.add(xor.z)

//        println("$and $xor $wrong")
    }

    // All XOR gates need either as result z or as input x or y.
    gates.filter { it.opr == "XOR" }
        .filter { it.x[0] !in listOf('x', 'y') }
        .filter { it.z[0] != 'z' }
        .map { it.z }
        .forEach(wrong::add)

    // result
    val partTwo = wrong.sorted().joinToString(",")
    //
    println("Answer part two: $partTwo")
    if ("drg,gvw,jbp,jgc,qjb,z15,z22,z35" != partTwo) throw Exception("screwed up code")

}

fun toDecimal(result: MutableMap<String, Int>, start: Char): Long = result
    .filter { it.key[0] == start }
    .toSortedMap()
    .reversed()
    .map { it.value }
    .joinToString("")
    .toLong(2)

//drg,gvw,jbp,jgc,qjb,z15,z22,z35


private fun parseInput(parsed: List<String>): Pair<HashMap<String, Gate>, Set<Gate>> {
    val map = hashMapOf<String, Gate>()
    for (line in parsed[0].lines()) {
        val split = line.split(": ")
        if (split.size == 2) {
            map[split[0]] = Gate(split[0], split[1], "-1", "-1")
        }
    }

    val list = mutableSetOf<Gate>()
    for (line in parsed[1].lines()) {
        val split = line.split(Regex(" -> | "))
        if (split.size == 4) {
            map[split[3]] = Gate(split[3], split[0], split[2], split[1])
            list.add(Gate(split[3], split[0], split[2], split[1]))
        }
    }
    return map to list
}

data class Gate(var z: String = "", var x: String = "", var y: String = "", var opr: String = "") {
    fun isEmpty(): Boolean = z == ""
    fun isNotEmpty(): Boolean = z != ""
    fun isValueHolder(): Boolean = opr == "-1"
}


private fun solve(
    name: String,
    map: MutableMap<String, Gate>,
    result: MutableMap<String, Int>
): Int {
    val gate = map[name]!!

    if (gate.isValueHolder()) {
        result[name] = gate.x.toInt()
        return gate.x.toInt()
    }

    if (result.containsKey(name)) return result[name]!!

    val res = when (gate.opr) {
        "AND" -> solve(gate.x, map, result).and(solve(gate.y, map, result))
        "XOR" -> solve(gate.x, map, result).xor(solve(gate.y, map, result))
        "OR" -> solve(gate.x, map, result).or(solve(gate.y, map, result))
        else -> throw Exception("should not happen")
    }

    result[name] = res;
    return res
}

