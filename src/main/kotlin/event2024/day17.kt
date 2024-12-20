fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")

    val lines = input.lines()

    println(lines)

    var a = lines[0].split("Register A: ").filter { it.isNotEmpty() }[0].toLong()
    var b = lines[1].split("Register B: ").filter { it.isNotEmpty() }[0].toLong()
    var c = lines[2].split("Register C: ").filter { it.isNotEmpty() }[0].toLong()
    var instructions = lines[4].split(Regex("Program: |,")).filter { it.isNotEmpty() }.map { it.toLong() }

    println("A=$a, B=$b, C=$c")
    println(instructions)

    println(runProgram(a, b, c, instructions, false).joinToString(","))

    println("7514272".toLong(8))

    // Used Jonathan Paulson solution approach,
    // From the output (mod 8) operation we know that always the last 3 bits are outputed.
    // The number in the output goes through some transformations after inserted in A.
    // Iteratively by hand try to adjust ${guess} by checking the octal number outputted (that resulted in most output matched input) and rerunning the program.

    var best = 0
    for (i in 0 until 100000000) {

        val guess = "236514272"
        a = (i * Math.pow(8.0, guess.length.toDouble())).toLong() + guess.toLong(8)
//        a= i.toLong()
        var list = runProgram(a, b, c, instructions, true)

        if (list.size > best) {
            println("$a, ${a.toString(8)}, $best, ${instructions.size}")
            best = list.size
        }

        if (list == instructions) {
            println(a)
            break
        }
    }

}


//1,6,3,6,5,6,5,1,7
fun runProgram(a: Long, b: Long, c: Long, instructions: List<Long>, partTwo: Boolean): MutableList<Long> {

    var A = a
    var B = b
    var C = c

    var index = 0

    var result = mutableListOf<Long>()

    while (index < instructions.size) {
        var optCode = instructions[index]
        var operand = instructions[index + 1]

        //bug when I set to small a,b,c
        var combo = when (operand) {
            in 0L..<4L -> operand
            4L -> A
            5L -> B
            6L -> C
            else -> {
                throw IllegalArgumentException("Invalid opcode $optCode")
            }
        }

        when (optCode) {
            0L -> A = (A / Math.pow(2.0, combo.toDouble())).toLong()
            1L -> B = B xor operand
            2L -> B = combo.mod(8L)
            3L -> {
                if (A != 0L) {
                    index = operand.toInt()
                    continue
                }
            }
            4L -> B = B xor C
            5L -> {
                result.add(combo.mod(8L))
                if (partTwo && result.last() != instructions[result.lastIndex]) {
                    return result.dropLast(1).toMutableList()
                }
            }
            6L -> B = (A / Math.pow(2.0, combo.toDouble())).toLong()
            7L -> C = (A / Math.pow(2.0, combo.toDouble())).toLong()
        }

        index += 2
    }

    return result
}
