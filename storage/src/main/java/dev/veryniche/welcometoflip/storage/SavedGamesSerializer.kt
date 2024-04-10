package dev.veryniche.welcometoflip.storage

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import dev.veryniche.welcometoflip.storage.models.Savedgames
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class SavedGamesSerializer @Inject constructor() : Serializer<Savedgames> {

    override val defaultValue: Savedgames = Savedgames.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): Savedgames {
        try {
            // readFrom is already called on the data store background thread
            @Suppress("BlockingMethodInNonBlockingContext")
            return Savedgames.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: Savedgames, output: OutputStream) =
        // writeTo is already called on the data store background thread
        @Suppress("BlockingMethodInNonBlockingContext")
        t.writeTo(output)
}
