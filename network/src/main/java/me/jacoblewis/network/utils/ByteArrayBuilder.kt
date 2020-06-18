package me.jacoblewis.network.utils

import java.io.ByteArrayOutputStream

class ByteArrayBuilder {
    val os = ByteArrayOutputStream()

    fun append(str: String) {
        os.write(str.toByteArray())
    }

    fun append(arr: ByteArray) {
        os.write(arr)
    }

    fun build(): ByteArray {
        val ba = os.toByteArray()
        os.flush()
        os.close()
        return ba
    }
}