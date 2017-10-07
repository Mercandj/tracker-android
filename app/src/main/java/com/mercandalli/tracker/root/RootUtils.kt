package com.mercandalli.tracker.root

import com.mercandalli.tracker.common.Closer
import java.io.*

/**
 * A helper class which goal is simply to determine whether the device is rooted.
 */
object RootUtils {

    // get from build info
    // check if /system/app/Superuser.apk is present
    // ignore
    // try executing commands
    internal val isRooted: Boolean
        get() {
            val buildTags = android.os.Build.TAGS
            if (buildTags != null && buildTags.contains("test-keys")) {
                return true
            }
            try {
                val file = File("/system/app/Superuser.apk")
                if (file.exists()) {
                    return true
                }
            } catch (e1: Exception) {
            }

            return (canExecuteCommand("/system/xbin/which su")
                    || canExecuteCommand("/system/bin/which su") || canExecuteCommand("which su"))
        }

    // executes a command on the system
    private fun canExecuteCommand(command: String): Boolean {
        var executedSuccesfully: Boolean
        try {
            Runtime.getRuntime().exec(command)
            executedSuccesfully = true
        } catch (e: Exception) {
            executedSuccesfully = false
        }

        return executedSuccesfully
    }

    fun sudoForResult(vararg strings: String): String {
        var res = ""
        var outputStream: DataOutputStream? = null
        var response: InputStream? = null
        try {
            val su = Runtime.getRuntime().exec("su")
            outputStream = DataOutputStream(su.outputStream)
            response = su.inputStream

            for (s in strings) {
                outputStream.writeBytes(s + "\n")
                outputStream.flush()
            }

            outputStream.writeBytes("exit\n")
            outputStream.flush()
            try {
                su.waitFor()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            res = readFully(response)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            Closer.closeSilently(outputStream, response)
        }
        return res
    }

    @Throws(IOException::class)
    private fun readFully(inputStream: InputStream): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var length: Int = inputStream.read(buffer)
        while ((length) != -1) {
            byteArrayOutputStream.write(buffer, 0, length)
            length = inputStream.read(buffer)
        }
        return byteArrayOutputStream.toString("UTF-8")
    }
}// no-op
