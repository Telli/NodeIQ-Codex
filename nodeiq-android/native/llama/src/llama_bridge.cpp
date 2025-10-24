#include <jni.h>
#include <android/log.h>

extern "C" JNIEXPORT void JNICALL
Java_ai_nodeiq_rag_gen_LlamaCppGenerator_nativeInit(JNIEnv* env, jobject thiz) {
    __android_log_print(ANDROID_LOG_INFO, "NodeIQ", "llama bridge initialized");
}
