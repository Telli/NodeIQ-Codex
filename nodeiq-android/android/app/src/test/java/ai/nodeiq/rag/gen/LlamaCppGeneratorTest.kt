package ai.nodeiq.rag.gen

import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LlamaCppGeneratorTest {
    @Test
    fun `generator handles missing native library gracefully`() {
        // This test verifies that the generator can be instantiated even when
        // the native library is not available (common in debug builds)
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        
        // Should not throw UnsatisfiedLinkError during instantiation
        val generator = LlamaCppGenerator(context, Dispatchers.Unconfined)
        
        // load() should return false when native library is not loaded
        val loadResult = generator.load("dummy_path")
        assertFalse("load should return false when native library is unavailable", loadResult)
        
        // generateStream should still work with stub behavior
        var tokenCalled = false
        var endCalled = false
        generator.generateStream(
            prompt = "test",
            maxTokens = 10,
            onToken = { tokenCalled = true },
            onEnd = { endCalled = true }
        )
        
        assertTrue("onToken should be called even without native library", tokenCalled)
        assertTrue("onEnd should be called even without native library", endCalled)
    }
}
