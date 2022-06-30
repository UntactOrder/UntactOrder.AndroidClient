package io.github.untactorder

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}