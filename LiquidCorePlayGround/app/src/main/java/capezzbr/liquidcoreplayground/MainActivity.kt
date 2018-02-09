package capezzbr.liquidcoreplayground

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.jovianware.jv8.V8MappableMethod
import com.jovianware.jv8.V8Runner
import org.liquidplayer.javascript.JSContext
import org.liquidplayer.javascript.JSFunction

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val script = "for (var i=0; i < 1024; i++) { nativeFunction(i + 0.0); }"

        testLiquidCore(script)
        testV8Runner(script)
    }

    fun testLiquidCore(script: String) {
        val context = JSContext()

        var sum = 0.0
        context.property("nativeFunction", object : JSFunction(context, "nativeFunction") {
            fun nativeFunction(index: Double) {
                sum += index
            }
        })

        val startTime = System.currentTimeMillis()
        context.evaluateScript(script, "script.js", 0)
        val endTime = System.currentTimeMillis()

        Log.i("RESULT", "LiquidCore run in " + (endTime - startTime) + "ms")
    }

    fun testV8Runner(script: String) {
        val runner = V8Runner()

        var sum = 0.0
        runner.map("nativeFunction", object : V8MappableMethod() {
            override fun methodToRun(args: Array<out Any>?): Any {
                sum += args!![0] as Double
                return false
            }
        })

        val startTime = System.currentTimeMillis()
        runner.runJS("script.js", script)
        val endTime = System.currentTimeMillis()

        Log.i("RESULT", "V8Runner run in " + (endTime - startTime) + "ms")
    }
}
