import java.util.*

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")

    val parsed = input.split("\n\r\n")

    val commands = parsed[1].split("")
        .filter(String::isNotBlank)


    val parsedInputMap = parsed[0].lines()
        .map {
            it.split("")
                .filter(String::isNotBlank)
                .toMutableList() // so we can set start to dot

        }.filter { it.isNotEmpty() }

    var pos = Pair(-1, -1)
    val expandedMap = Array(parsedInputMap.size) { Array(parsedInputMap[0].size * 2) { " " } }
    for (i in 0 until parsedInputMap.size) {
        for (j in 0 until parsedInputMap[0].size) {
            if (parsedInputMap[i][j] == "@") {
                pos = Pair(i, j * 2)
                expandedMap[i][j * 2] = "."
                expandedMap[i][j * 2 + 1] = "."
            } else if (parsedInputMap[i][j] == "O") {
                expandedMap[i][j * 2] = "["
                expandedMap[i][j * 2 + 1] = "]"
            } else if (parsedInputMap[i][j] == "#") {
                expandedMap[i][j * 2] = "#"
                expandedMap[i][j * 2 + 1] = "#"
            } else {
                expandedMap[i][j * 2] = "."
                expandedMap[i][j * 2 + 1] = "."
            }
        }
    }

    println(pos)
    println("=====Answer part two=======")
    calculate(commands, pos, expandedMap)

    pos = Pair(-1, -1)
    for (i in parsedInputMap.indices) {
        for (j in parsedInputMap[0].indices) {
            if (parsedInputMap[i][j] == "@") {
                pos = Pair(i, j)
                parsedInputMap[i][j] = "."
            }
        }
    }
    println("=====Answer part one=======")
    calculate(commands, pos, parsedInputMap.map { it.toTypedArray() }.toTypedArray())
}

private val directions: HashMap<String, Pair<Int, Int>> = hashMapOf(
    Pair(">", Pair(0, 1)),
    Pair("<", Pair(0, -1)),
    Pair("^", Pair(-1, 0)),
    Pair("v", Pair(1, 0))
)

private fun calculate(
    commands: List<String>,
    localPos: Pair<Int, Int>,
    grid: Array<Array<String>>
) {
    var pos = localPos
    for (command in commands) {
        val dir = directions[command]!!
        val (i, j) = Pair(pos.first + dir.first, pos.second + dir.second)
        if (grid[i][j] == "#") continue
        if (grid[i][j] == ".") {
            pos = Pair(i, j); continue
        }
        if (grid[i][j] in listOf("[", "]", "O")) {
            val dequeue: LinkedList<Pair<Int, Int>> = LinkedList<Pair<Int, Int>>();
            dequeue.add(pos)
            var canMove = true
            var boxes = mutableSetOf<Pair<Int, Int>>()

            while (dequeue.isNotEmpty()) {
                val loc = dequeue.pollFirst()

                if (boxes.contains(loc)) continue
                boxes.add(loc)

                val (x, y) = Pair(loc.first + dir.first, loc.second + dir.second)

                if (grid[x][y] == "#") {
                    canMove = false
                    break
                } else if (grid[x][y] == "]") {
                    dequeue.add(Pair(x, y))
                    dequeue.add(Pair(x, y - 1))
                } else if (grid[x][y] == "[") {
                    dequeue.add(Pair(x, y))
                    dequeue.add(Pair(x, y + 1))
                } else if (grid[x][y] == "O") {
                    dequeue.add(Pair(x, y))
                }
            }
            if (!canMove) continue

            var workBoxes = boxes.toMutableSet()

            // Underneath complicated way to move all boxes.
            // It will only move a coordinate to place no box is, or if we already moved the box.
            // this guarantees that location are only updated if they have a free place to move to.
            // Easier maybe was just to copy the whole grid + insert new location of boxes in the grid. (+ set old location first to dot)

            while (boxes.isNotEmpty()) {
                for (box in boxes) {
                    val (x, y) = Pair(box.first + dir.first, box.second + dir.second)
                    if (!workBoxes.contains(Pair(x, y))) {
                        grid[x][y] = grid[box.first][box.second]
                        grid[box.first][box.second] = "."
                        workBoxes.remove(Pair(box.first, box.second))
                    }
                }
                boxes = workBoxes.toMutableSet()
            }
            pos = Pair(i, j)
        }
    }
    var sum = 0

    for (i in grid.indices) {
        for (j in grid[0].indices) {
            if (grid[i][j] in listOf("[", "O")) sum += (100 * i) + j
        }
    }

    println(sum)
    printMap(grid, localPos)
}

private fun printMap(
    map: Array<Array<String>>,
    pos: Pair<Int, Int>
) {
    for (i in map.indices) {
        for (j in map[0].indices) {
            when {
                map[i][j] == "#" -> print("#")
                map[i][j] == "[" -> print("[")
                map[i][j] == "]" -> print("]")
                map[i][j] == "O" -> print("O")
                i == pos.first && j == pos.second -> print("@")
                else -> print(".")
            }
        }
        println()
    }
}




