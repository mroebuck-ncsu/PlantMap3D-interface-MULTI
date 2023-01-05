package edu.ncsu.biomap

import org.junit.Test
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class KodeinUnitTest {

    @Serializable
    data class Data(val a: Int, val b: String)

    private interface Dice
    private class RandomDice(private val from: Int, private val to: Int) : Dice

    private interface DataSource
    private class SqliteDS : DataSource {
        companion object {
            fun open(text: String): SqliteDS {
                return SqliteDS()
            }
        }
    }

    private val di = DI {
        bindProvider<Dice> { RandomDice(0, 5) }
        bindSingleton<DataSource> { SqliteDS.open("path/to/file") }
    }

    @Test
    fun addition_isCorrect() {
        val ds: DataSource by di.instance()

        val json = Json.encodeToString(Data(42, "str"))
        val obj = Json.decodeFromString<Data>("""{"a":42, "b": "str"}""")
    }
}