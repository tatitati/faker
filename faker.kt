package com.simplybusiness.inspector
import net.snowflake.client.jdbc.internal.joda.time.DateTime
import java.security.InvalidParameterException

class Faker {
    companion object {
        fun anyInt(min: Int = -100000, max: Int = 100000): Int = (min..max).random()
        fun around(num: Int): Int = this.anyOf(listOf(num-1, num, num+1))
        fun anyIntPositive(max: Int = 100000, includeZero: Boolean = true): Int {
            if (includeZero) {
                return anyInt(min = 0, max = max)
            }
            return anyInt(min = 1, max = max)
        }

        fun<T> anyOf(items: Collection<T>): T {
            val size = items.size
            if(size == 0){
                throw InvalidParameterException("Faker.anyOf() needs at least an item to do its job")
            }

            val randomindex = anyIntPositive(size - 1)
            return items.elementAt(randomindex)
        }

        fun<T> anyListOf(builder: () -> T, withMinLength: Int = 0, withMaxLength: Int = 10): List<T> {
            val amountItems = anyInt(min=withMinLength, max=withMaxLength)

            val items = mutableListOf<T>()
            for (i in 1..amountItems) {
                items.add(builder())
            }

            return items.toList()
        }

        fun<T> anyListOf(builder: () -> T, withAround: Int=1): List<T> {
            val around = Faker.around(withAround)

            val items = mutableListOf<T>()
            for (i in 1..around) {
                items.add(builder())
            }

            return items.toList()
        }

        fun anyString(withMaxLength: Int = 30, withMinLength: Int = 3, withCharactersPool: List<Char> ? = null, allowEmpty: Boolean = false): String {
            val charactersPool: List<Char> = withCharactersPool ?: ('a'..'z') + ('A'..'Z') + ('0'..'9') + listOf('.', '_', '-', ' ', '#', '!', '/', '\\')
            val randomLength = anyInt(min = withMinLength, max = withMaxLength)

            var randomTextAccumulator = ""
            (0..randomLength).forEach{
                randomTextAccumulator += anyOf(charactersPool)
            }

            if(allowEmpty) {
                return anyOf(listOf("", randomTextAccumulator))
            }

            return randomTextAccumulator
        }

        fun anyWord(allowEmpty: Boolean = false): String {
            val charactersPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
            return anyString(withMinLength = 4, withMaxLength = 30, withCharactersPool = charactersPool, allowEmpty = allowEmpty)
        }

        fun<T> aNullOr(builder: () -> T): T? {
            val item: T = builder()

            return anyOf(listOf(null, item))
        }

        fun anyDateTime(): DateTime {
            return DateTime()
                    .withYear(Faker.anyIntPositive(3000))
                    .withMonthOfYear(Faker.anyIntPositive(12, includeZero = false))
                    .withDayOfMonth(Faker.anyIntPositive(28, includeZero = false))
                    .withHourOfDay(Faker.anyIntPositive(23))
                    .withMinuteOfHour(Faker.anyIntPositive(59))
                    .withSecondOfMinute(Faker.anyIntPositive(59))
        }
    }
}
