package com.f4nt45y.ol

object NativeBridge {
    init { System.loadLibrary("ol_engine") }

    @JvmStatic external fun nativeInit()
    @JvmStatic external fun nativeResize(width: Int, height: Int)
    @JvmStatic external fun nativeDraw()
}
