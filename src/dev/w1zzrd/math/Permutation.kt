package dev.w1zzrd.math

import kotlin.collections.ArrayList

class Permutation {
    private val orderedElements: CharArray
    private val perms: List<CharArray>

    constructor(vararg permutations: CharArray) {
        orderedElements = permutations.map(CharArray::toSet).reduceRight{ it, acc -> acc.union(it) }.toCharArray()
        perms = permutations.reversed()
    }

    constructor(transpositions: ArrayList<Pair<Char, Char>>) {
        orderedElements = transpositions.map(Pair<Char, *>::first).union(transpositions.map(Pair<*, Char>::second)).toCharArray()
        perms = transpositions.map { charArrayOf(it.first, it.second) }
    }


    /**
     * Combine two permutations
     */
    operator fun times(perm: Permutation)
            = Permutation(this(*perm(*orderedElements)).zip(orderedElements)    // Generate transpositions
        .filterNot{ it.first == it.second }
        .foldRight(ArrayList<Pair<Char, Char>>()){                              // Deduplicate transpositions
                insert, acc ->
            acc.firstOrNull {
                (it.first == insert.first && it.second == insert.second) || (it.first == insert.second && it.second == insert.first)
            } ?: acc.add(insert)
            acc
        })

    operator fun times(perm: CharArray) = this * Permutation(perm)

    operator fun invoke(vararg elements: Char): CharArray {
        // Apply all permutations
        for(perm in perms)
            // Check each element-permutation
            for(itemIndex in elements.indices)
                // Check if permutation matches
                for(index in perm.indices)
                    // If permutation matches, apply it and go to next element
                    if(elements[itemIndex] == perm[index]) {
                        elements[itemIndex] = perm[(index + 1) % perm.size]
                        break
                    }

        return elements
    }
}
