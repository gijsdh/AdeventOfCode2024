package event2024

import getResourceAsText

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")

    var parsed = input.split("\n\r\n")


    var result: MutableMap<String, Int> = mutableMapOf();
    var map = parseInput(parsed)

    println(map)

    var list = mutableListOf<Pair<String, Int>>()
    for (entry in map.keys) {
        list.add(entry to solve(entry, map, result, false))
    }

    val partOne = list.filter { it.first[0] == 'z' }
        .sortedBy { it.first }
        .reversed()
        .map { it.second }
        .joinToString("")
        .toLong(2)
    println(partOne)

    // Part 2

    val resultZ = toDecimal(result, 'z').toString(2)
    val resultCombi = (toDecimal(result, 'x') + toDecimal(result, 'y')).toString(2)
    println("result $resultZ")
    println("sum    $resultCombi")
    val xored = resultZ.toLong(2)
        .xor(resultCombi.toLong(2))
        .toString(2).padStart(resultCombi.length, '0')

    println("Xored $xored")

    for ((i, bit) in resultCombi.withIndex()) {
        if (bit != resultZ[i]) {
            println("bit problem ${resultCombi.length - i - 1}")
        }
    }

    println(map.filter { it.key[0] == 'z' }.filter { it.value.third != "XOR" })
    solve("z08", map, mutableMapOf<String, Int>(), true)


    // Shit problem, following reddit again.
    // All z's are the result off local full adder circuits (this seems to satisfy all inputs), reddit taught me:
    // schematic representation of full adder circuit:
    //
    //    x >--+------>+------+
    //         |       | XOR  |>-+----->+------+
    //    y >--|---+-->+------+  v      | XOR  |>----- z
    //         |   |             |   +->+------+
    //         v   v             |   |
    //         |   +-->+------+  |   |
    //         |       | AND  |>-|---|------------------>+-----+
    //         +------>+------+  |   |                   | OR  |>------ Cout
    //                           +---|--->+------+    +->+-----+
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

    var wrong = mutableListOf<String>()
    for (i in 0 until resultCombi.length) {
        var id = i.toString().padStart(2, '0')

        // All XOR gate with X0i + Y0i
        var xor =
            map.filter { findByValue(it.value, id) }
                .filter { it.value.third == "XOR" }
                .map { it.key to it.value }

        // All AND gate with X0i + Y0i
        var and =
            map.filter { findByValue(it.value, id) }
                .filter { it.value.third == "AND" }
                .map { it.key to it.value }
        var z = map["z$id"]


        if (xor.isEmpty() || and.isEmpty()) continue

        // So all z must be attached to XOR to satisfy the full adder circuit.
        if (z!!.third != "XOR") wrong.add("z$id")

        var or = map.filter { it.value.first == and[0].first || it.value.second == and[0].first }
            .map { it.key to it.value }

        // All x+y AND gates need to be followed with an OR. As y00 + x00 acts a half adder circuit.
        if (or.isNotEmpty() && or[0].second.third != "OR" && i > 0) wrong.add(and[0].first)


        // All x+y XOR gates need to be followed with an XOR or an AND (or !OR).
        var xorAfter = map.filter { it.value.first == xor[0].first || it.value.second == xor[0].first }
            .map { it.key to it.value }

        if (xorAfter.isNotEmpty() && xorAfter[0].second.third == "OR") wrong.add(xor[0].first)

        println("$and $xor $wrong")
    }

    // All XOR gates need either as result z or as input x / y.
    map.filter { it.value.third == "XOR" }
        .filter { it.value.first[0] !in listOf('x', 'y') }
        .filter { it.key[0] != 'z' }
        .map { it.key }
        .forEach { wrong.add(it) }

    // result
    println(wrong.sorted().joinToString(","))
}

fun toDecimal(result: MutableMap<String, Int>, start: Char): Long = result
    .filter { it.key[0] == start }
    .toSortedMap()
    .reversed()
    .map { it.value }
    .joinToString("")
    .toLong(2)

private fun findByValue(
    it: Triple<String, String, String>,
    id: String
) = (it.first == "x$id" && it.second == "y$id") || (it.first == "y$id" && it.second == "x$id")

//drg,gvw,jbp,jgc,qjb,z15,z22,z35


private fun parseInput(parsed: List<String>): HashMap<String, Triple<String, String, String>> {
    val map = hashMapOf<String, Triple<String, String, String>>()
    for (line in parsed[0].lines()) {
        var split = line.split(": ")
        if (split.size == 2) {
            map[split[0]] = Triple(split[1], "-1", "-1")
        }
    }

    for (line in parsed[1].lines()) {
        var split = line.split(Regex(" -> | "))
        if (split.size == 4) {
            map[split[3]] = Triple(split[0], split[2], split[1])
        }
    }
    return map
}


private fun solve(
    name: String,
    map: MutableMap<String, Triple<String, String, String>>,
    result: MutableMap<String, Int>,
    print: Boolean
): Int {
    val value = map[name]!!
    if (print) println("key $name map $value")
    if (value.second == "-1") {
        result[name] = value.first.toInt()
        return value.first.toInt()
    }

    if (result.containsKey(name)) return result[name]!!

    var res = when (value.third) {
        "AND" -> solve(value.first, map, result, print).and(solve(value.second, map, result, print))
        "XOR" -> solve(value.first, map, result, print).xor(solve(value.second, map, result, print))
        "OR" -> solve(value.first, map, result, print).or(solve(value.second, map, result, print))
        else -> throw Exception("should not happen")
    }

    result[name] = res;
    return res
}

