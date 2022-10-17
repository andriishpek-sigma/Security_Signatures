package data.random

interface RandomGenerator {

    fun nextValue(): Long

    fun nextSequence(size: Int): Array<Long>

}