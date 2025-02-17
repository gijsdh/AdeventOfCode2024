package event2024


// https://rosettacode.org/wiki/Cramer%27s_rule
// underneath is copied from rosetta code.

typealias Vector = DoubleArray
typealias Matrix = Array<Vector>

fun johnsonTrotter(n: Int): Pair<List<IntArray>, List<Int>> {
    val p = IntArray(n) { it }  // permutation
    val q = IntArray(n) { it }  // inverse permutation
    val d = IntArray(n) { -1 }  // direction = 1 or -1
    var sign = 1
    val perms = mutableListOf<IntArray>()
    val signs = mutableListOf<Int>()

    fun permute(k: Int) {
        if (k >= n) {
            perms.add(p.copyOf())
            signs.add(sign)
            sign *= -1
            return
        }
        permute(k + 1)
        for (i in 0 until k) {
            val z = p[q[k] + d[k]]
            p[q[k]] = z
            p[q[k] + d[k]] = k
            q[z] = q[k]
            q[k] += d[k]
            permute(k + 1)
        }
        d[k] *= -1
    }

    permute(0)
    return perms to signs
}

fun determinant(m: Matrix): Double {
    val (sigmas, signs) = johnsonTrotter(m.size)
    var sum = 0.0
    for ((i, sigma) in sigmas.withIndex()) {
        var prod = 1.0
        for ((j, s) in sigma.withIndex()) prod *= m[j][s]
        sum += signs[i] * prod
    }
    return sum
}

fun cramer(m: Matrix, d: Vector): Vector {
    val divisor = determinant(m)
    val numerators = Array(m.size) { Matrix(m.size) { m[it].copyOf() } }
    val v = Vector(m.size)
    for (i in 0 until m.size) {
        for (j in 0 until m.size) numerators[i][j][i] = d[j]
    }
    for (i in 0 until m.size) v[i] = determinant(numerators[i]) / divisor
    return v
}