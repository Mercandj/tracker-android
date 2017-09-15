package com.mercandalli.tracker.common

/**
 * Static methods to be called at the Start of your methods to verify correct arguments/states.
 *
 *
 * Do not necessary test all your arguments,
 * but a good practice is to test the arguments of the public API of your classes =)
 */
internal object Preconditions {

    /**
     * A default detail message thrown with the [NullPointerException] in
     * [Preconditions.checkNotNullInternal]
     */
    private val DEFAULT_DETAIL_MESSAGE_FOR_CHECK_NULL = "Object can not be null."

    /**
     * Ensures that an object reference is not null.
     *
     * @param object the [Object] to check.
     */
    fun checkNotNull(`object`: Any) {
        checkNotNullInternal(`object`, DEFAULT_DETAIL_MESSAGE_FOR_CHECK_NULL)
    }

    fun checkNotNullAllCollection(`object`: Collection<*>) {
        checkNotNullInternal(`object`, DEFAULT_DETAIL_MESSAGE_FOR_CHECK_NULL)
        for (o in `object`) {
            checkNotNullInternal(o, DEFAULT_DETAIL_MESSAGE_FOR_CHECK_NULL)
        }
    }

    /**
     * Ensures that an object reference is not null, with an error message.
     *
     * @param object  the [Object] to check.
     * @param message the given detail message.
     */
    fun checkNotNull(`object`: Any, message: String) {
        checkNotNullInternal(`object`, message)
    }

    private fun checkNotNullInternal(`object`: Any?, message: String) {
        if (`object` == null) {
            throw NullPointerException(message)
        }
    }
}// Non-instantiable.
