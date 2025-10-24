package ai.nodeiq.rag.gen

import android.app.Application
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class LlamaCppGeneratorTest {

    @Test
    fun `generateStream returns error when library not loaded`() {
        // Given: LlamaCppGenerator is instantiated (library will fail to load in test environment)
        val generator = LlamaCppGenerator(Application())
        
        // When: generateStream is called
        var endStatus = ""
        var tokenReceived = false
        
        generator.generateStream(
            prompt = "test prompt",
            maxTokens = 100,
            onToken = { tokenReceived = true },
            onEnd = { endStatus = it }
        )
        
        // Then: Error message is returned and no tokens are generated
        assertFalse("Should not receive tokens when library is not loaded", tokenReceived)
        assertEquals("ERROR: Native library not loaded", endStatus)
    }

    @Test
    fun `load returns false when library not loaded`() {
        // Given: LlamaCppGenerator is instantiated (library will fail to load in test environment)
        val generator = LlamaCppGenerator(Application())
        
        // When: load is called
        val result = generator.load("model.gguf", 2048)
        
        // Then: load returns false
        assertFalse("load should return false when library is not loaded", result)
    }
}
