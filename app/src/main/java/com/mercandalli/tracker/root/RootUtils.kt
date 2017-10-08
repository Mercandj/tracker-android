package com.mercandalli.tracker.root

import android.os.Build
import com.mercandalli.tracker.common.Closer
import com.mercandalli.tracker.device_spec.DeviceSpecUtils.isEmulator
import java.io.*

/**
 * A helper class which goal is simply to determine whether the device is rooted.
 */
internal object RootUtils {

    /**
     * su is the binary that is used to grant root access.
     * It being on the phone is usually a good sign that the phone is rooted.
     *
     *
     * Note that this only check that su exists. A more complete check would be to
     * call su for the app, requests permission, and returns true if the app was
     * successfully granted root permissions.
     *
     * @return `true` if su was found.
     */
    internal fun isRooted(): Boolean {
        val isEmulator = isEmulator()
        val buildTags = Build.TAGS
        if (!isEmulator && buildTags != null && buildTags.contains("test-keys")) {
            return true
        }
        var file = File("/system/app/Superuser.apk")
        if (file.exists()) {
            return true
        }
        file = File("/system/xbin/su")
        return !isEmulator && file.exists()
    }

    internal fun sudoForResult(vararg strings: String): String {
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
}
