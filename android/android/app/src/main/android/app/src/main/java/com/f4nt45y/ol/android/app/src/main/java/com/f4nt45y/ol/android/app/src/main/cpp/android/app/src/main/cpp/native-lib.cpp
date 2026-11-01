#include <jni.h>
#include <GLES2/gl2.h>
#include <cmath>

static float g_t = 0.f;

extern "C" {

JNIEXPORT void JNICALL
Java_com_f4nt45y_ol_NativeBridge_nativeInit(JNIEnv*, jclass) {
    glEnable(GL_DEPTH_TEST);
}

JNIEXPORT void JNICALL
Java_com_f4nt45y_ol_NativeBridge_nativeResize(JNIEnv*, jclass, jint w, jint h) {
    glViewport(0, 0, w, h);
}

JNIEXPORT void JNICALL
Java_com_f4nt45y_ol_NativeBridge_nativeDraw(JNIEnv*, jclass) {
    g_t += 0.01f;
    glClearColor(0.02f, 0.05f + 0.03f * std::sin(g_t), 0.12f, 1.f);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
}

}
