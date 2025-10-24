package ai.nodeiq.di

import ai.nodeiq.mqtt.MqttClientManager
import ai.nodeiq.store.NodeIqDatabase
import ai.nodeiq.store.PeerDao
import ai.nodeiq.store.MessageDao
import ai.nodeiq.transport.TransportRouter
import android.content.Context
import androidx.room.Room
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NodeIqDatabase =
        Room.databaseBuilder(context, NodeIqDatabase::class.java, "nodeiq.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun providePeerDao(db: NodeIqDatabase): PeerDao = db.peerDao()

    @Provides
    fun provideMessageDao(db: NodeIqDatabase): MessageDao = db.messageDao()

    @Provides
    @Singleton
    fun provideMqttClientManager(@ApplicationContext context: Context): MqttClientManager =
        MqttClientManager(context)

    @Provides
    @Singleton
    fun provideTransportRouter(
        mqttClientManager: MqttClientManager,
        appScope: CoroutineScope
    ): TransportRouter = TransportRouter(mqttClientManager, appScope)

    @Provides
    @Singleton
    fun provideAppScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Provides
    @Singleton
    fun provideFirebaseMessaging(): FirebaseMessaging = FirebaseMessaging.getInstance()
}
