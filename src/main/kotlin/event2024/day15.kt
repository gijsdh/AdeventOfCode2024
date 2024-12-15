import java.util.*

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")

    val parsed = input.split("\n\r\n")

    val commands = parsed[1].split("")
        .filter(String::isNotBlank)


    val map = parsed[0].lines()
        .map {
            it.split("")
                .filter(String::isNotBlank)

        }.filter { it.isNotEmpty() }
    println(map)

    var pos = Pair(-1, -1)
    var boxes = mutableSetOf<Pair<Int, Int>>()
    for (i in 0 until map.size) {
        for (j in 0 until map[0].size) {
            if (map[i][j] == "@") {
                pos = Pair(i, j)
            } else if (map[i][j] == "O") {
                boxes.add(Pair(i, j))
            }
        }
    }

    val directions: HashMap<String, Pair<Int, Int>> = hashMapOf(
        Pair(">", Pair(0, 1)),
        Pair("<", Pair(0, -1)),
        Pair("^", Pair(-1, 0)),
        Pair("v", Pair(1, 0))
    )

    println(boxes)
    for (command in commands) {
        var dir = directions[command]!!
        var newPos = Pair(pos.first + dir.first, pos.second + dir.second)
        if (map[newPos.first][newPos.second] == "#") continue
        if (boxes.contains(newPos)) {

            var count = 1
            var canmove = false
            var checkBox = newPos
            while (true) {
                checkBox = Pair(checkBox.first + dir.first, checkBox.second + dir.second)
                when {
                    boxes.contains(checkBox) -> count++
                    map[checkBox.first][checkBox.second] == "#" -> break
                    else -> {
                        canmove = true;
                        break
                    }
                }
            }
            if (!canmove) continue
            boxes.remove(newPos)
            boxes.add(Pair(newPos.first + dir.first * count, newPos.second + dir.second * count))
        }
        pos = newPos
    }

    printMap(map, boxes, pos)
    println(boxes.sumOf { it.first * 100 + it.second })
}

private fun printMap(
    map: List<List<String>>,
    boxes: MutableSet<Pair<Int, Int>>,
    pos: Pair<Int, Int>
) {
    for (i in 0 until map.size) {
        for (j in 0 until map[0].size) {
            when {
                map[i][j] == "#" -> print("#")
                boxes.contains(Pair(i, j)) -> print("O")
                i == pos.first && j == pos.second -> print("@")
                else -> print(".")
            }
        }
        println()
    }
}




