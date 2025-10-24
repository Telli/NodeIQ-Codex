package ai.nodeiq.store

import androidx.room.TypeConverter
import java.nio.ByteBuffer

object VectorConverters {
    @TypeConverter
    @JvmStatic
    fun fromBlob(blob: ByteArray?): FloatArray {
        if (blob == null) return floatArrayOf()
        val buffer = ByteBuffer.wrap(blob)
        val floats = FloatArray(blob.size / 4)
        for (i in floats.indices) {
            floats[i] = buffer.float
        }
        return floats
    }

    @TypeConverter
    @JvmStatic
    fun toBlob(vector: FloatArray?): ByteArray {
        if (vector == null) return byteArrayOf()
        val buffer = ByteBuffer.allocate(vector.size * 4)
        vector.forEach { buffer.putFloat(it) }
        return buffer.array()
    }
}
