package ai.nodeiq.di

import ai.nodeiq.config.AppConfig
import ai.nodeiq.market.MeteringService
import ai.nodeiq.market.PricingService
import ai.nodeiq.market.WalletService
import ai.nodeiq.mqtt.MqttClientManager
import ai.nodeiq.rag.RagEngine
import ai.nodeiq.rag.emb.OnnxEmbedder
import ai.nodeiq.rag.gen.ILlmGenerator
import ai.nodeiq.rag.gen.LlamaCppGenerator
import ai.nodeiq.rag.index.CosineIndex
import ai.nodeiq.rag.index.VectorIndex
import ai.nodeiq.store.NodeIqDatabase
import ai.nodeiq.transport.Lp2pTransport
import ai.nodeiq.transport.MqttTransport
import ai.nodeiq.transport.TransportRegistry
import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

private val Application.settingsDataStore: DataStore<Preferences> by preferencesDataStore("settings")

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePreferences(app: Application): DataStore<Preferences> = app.settingsDataStore

    @Provides
    @Singleton
    fun provideDatabase(app: Application): NodeIqDatabase = NodeIqDatabase.build(app)

    @Provides
    @Singleton
    fun provideVectorIndex(db: NodeIqDatabase): VectorIndex = CosineIndex(db.docDao())

    @Provides
    @Singleton
    fun provideEmbedder(app: Application): OnnxEmbedder = OnnxEmbedder(app)

    @Provides
    @Singleton
    fun provideGenerator(app: Application): ILlmGenerator = LlamaCppGenerator(app, Dispatchers.IO)

    @Provides
    @Singleton
    fun provideRagEngine(embedder: OnnxEmbedder, index: VectorIndex, generator: ILlmGenerator): RagEngine =
        RagEngine(embedder, index, generator)

    @Provides
    @Singleton
    fun provideMqttTransport(app: Application, config: AppConfig, rag: RagEngine, db: NodeIqDatabase): MqttTransport =
        MqttTransport(app, config, rag, db, MqttClientManager(app))

    @Provides
    @Singleton
    fun provideLibp2pTransport(config: AppConfig): Lp2pTransport = Lp2pTransport(config)

    @Provides
    @Singleton
    fun provideTransportRegistry(config: AppConfig, mqtt: MqttTransport, libp2p: Lp2pTransport): TransportRegistry =
        TransportRegistry.fromConfig(config, listOf(mqtt, libp2p))

    @Provides
    @Singleton
    fun provideConfig(app: Application): AppConfig = AppConfig.load(app)

    @Provides
    @Singleton
    fun provideWalletService(db: NodeIqDatabase, config: AppConfig): WalletService = WalletService(db, config)

    @Provides
    @Singleton
    fun providePricingService(db: NodeIqDatabase, config: AppConfig): PricingService = PricingService(db, config)

    @Provides
    @Singleton
    fun provideMeteringService(db: NodeIqDatabase, config: AppConfig): MeteringService = MeteringService(db, config)
}
