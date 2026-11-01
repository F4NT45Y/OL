package com.f4nt45y.ol

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.ResolveInfo
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.ListView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MainActivity : Activity() {

    private lateinit var glView: GLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        glView = GLSurfaceView(this).apply {
            setEGLContextClientVersion(2)
            setRenderer(object : GLSurfaceView.Renderer {
                override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) = NativeBridge.nativeInit()
                override fun onSurfaceChanged(gl: GL10?, w: Int, h: Int) = NativeBridge.nativeResize(w, h)
                override fun onDrawFrame(gl: GL10?) = NativeBridge.nativeDraw()
            })
        }

        val apps = loadApps()
        val list = ListView(this).apply {
            setBackgroundColor(0x99000000.toInt())
            adapter = ArrayAdapter(
                this@MainActivity,
                android.R.layout.simple_list_item_1,
                apps.map { it.loadLabel(packageManager).toString() }
            )
            setOnItemClickListener { _, _, position, _ -> launch(apps[position]) }
        }

        val root = FrameLayout(this)
        root.addView(glView)
        root.addView(
            list,
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                resources.displayMetrics.heightPixels / 2,
                Gravity.BOTTOM
            )
        )
        setContentView(root)
    }

    private fun loadApps(): List<ResolveInfo> {
        val intent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        return packageManager.queryIntentActivities(intent, 0)
            .filter { it.activityInfo.packageName != packageName }
            .sortedBy { it.loadLabel(packageManager).toString().lowercase() }
    }

    private fun launch(app: ResolveInfo) {
        val info = app.activityInfo
        startActivity(
            Intent(Intent.ACTION_MAIN)
                .addCategory(Intent.CATEGORY_LAUNCHER)
                .setComponent(ComponentName(info.packageName, info.name))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() { }

    override fun onResume() { super.onResume(); glView.onResume() }
    override fun onPause() { glView.onPause(); super.onPause() }
}
