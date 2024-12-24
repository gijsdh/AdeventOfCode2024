fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val line = input.split("").filter(String::isNotEmpty).map(String::toInt)

    var list = mutableListOf<Pair<Int, Int>>()
    var index = 0
    for (number in line.withIndex()) {
        if (number.index % 2 == 0) {
            list.add(Pair(index, number.value))
            index++
        } else {
            list.add(Pair(-1, number.value))
        }
    }


    index = list.size - 1
    var indexS = 1
    val compacted = mutableListOf<Pair<Int, Int>>()
    compacted.add(list.first())
    while (true) {
        val file = list[index]
        if (file.first == -1) continue
        val id = file.first
        var size = file.second

        while (true) {
            if (indexS > index) {
                break
            }
            var space = list[indexS].second
            if (space > size) {
                compacted.add(Pair(id, size))
                list[indexS] = Pair(-1, space - size)
                break
            } else if (space < size) {
                compacted.add(Pair(id, space))
                if (indexS + 1 == index) {
                    compacted.add(Pair(id, size - space))
                    break
                }
                compacted.add(list[indexS + 1])
                size -= space
                indexS += 2
            }

            compacted.add(Pair(id, space))
            if (indexS + 1 == index) {
                break
            }
            compacted.add(list[indexS + 1])
            indexS += 2
            break

        }
        index -= 2
        if (indexS > index) break
    }

    println(compacted)

    var sum = 0L
    var counter = 0
    for (i in compacted) {
        for (j in 0 until i.second) {
            print(i.first)
            sum += i.first * counter++
        }
    }

    println()
    println(sum)


}


