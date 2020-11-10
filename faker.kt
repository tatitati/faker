package whatever

import net.snowflake.client.jdbc.internal.joda.time.DateTime
import java.security.InvalidParameterException

class Any {
    companion object {
        fun int(min: Int = -100000, max: Int = 100000): Int = (min..max).random()
        fun intPositive(max: Int = 100000, includeZero: Boolean = true): Int {
            if (includeZero) {
                return int(min = 0, max = max)
            }
            return int(min = 1, max = max)
        }

        fun<T> of(items: Collection<T>): T {
            val size = items.size
            if(size == 0){
                throw InvalidParameterException("Faker.anyOf() needs at least an item to do its job")
            }

            val randomindex = intPositive(size - 1)
            return items.elementAt(randomindex)
        }

        fun<T> listOf(builder: () -> T, withMinLength: Int = 1, withMaxLength: Int = 3): List<T> {
            val amountItems = int(min=withMinLength, max=withMaxLength)

            val items = mutableListOf<T>()
            for (i in 1..amountItems) {
                items.add(builder())
            }

            return items.toList()
        }

        fun string(withMaxLength: Int = 30, withMinLength: Int = 3, withCharactersPool: List<Char> ? = null, allowBlank: Boolean = false): String {
            val charactersPool: List<Char> = withCharactersPool ?: ('a'..'z') + ('A'..'Z') + ('0'..'9') + listOf('.', '_', '-', ' ', '#', '!', '/', '\\')
            val randomLength = int(min = withMinLength, max = withMaxLength)

            var randomTextAccumulator = ""
            (0..randomLength).forEach{
                randomTextAccumulator += of(charactersPool)
            }

            if(allowBlank) {
                return of(listOf("", randomTextAccumulator))
            }

            return randomTextAccumulator
        }

        fun word(allowBlank: Boolean = false): String {
            val charactersPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
            return string(withMinLength = 3, withMaxLength = 6, withCharactersPool = charactersPool, allowBlank = allowBlank)
        }

        fun<T> nullOr(builder: () -> T): T? {
            val item: T = builder()

            return of(listOf(null, item))
        }

        fun dateTime(): DateTime {
            return DateTime()
                    .withYear(Any.intPositive(3000))
                    .withMonthOfYear(Any.intPositive(12, includeZero = false))
                    .withDayOfMonth(Any.intPositive(28, includeZero = false))
                    .withHourOfDay(Any.intPositive(23))
                    .withMinuteOfHour(Any.intPositive(59))
                    .withSecondOfMinute(Any.intPositive(59))
        }
    }
}
