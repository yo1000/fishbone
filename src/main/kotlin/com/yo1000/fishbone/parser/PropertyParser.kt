package com.yo1000.fishbone.parser

import kotlin.reflect.full.memberExtensionProperties
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaGetter

/**
 *
 * @author yo1000
 */
class PropertyParser(
        val expression: String
) {
    fun parse(instance: Any): Any? {
        return parse(instance, expression)
    }

    private fun parse(instance: Any, expression: String): Any? {
        val propertyNames: List<String> = expression.split(delimiters = '.', limit = 2)
        val propertyName: String = propertyNames.first()
        val value: Any? = readValue(instance, propertyName)

        if (value == null || propertyNames.size == 1) {
            return value
        }

        return parse(value, propertyNames[1])
    }

    private fun readValue(instance: Any, propertyName: String): Any? {
        if (instance is Map<*, *>) {
            return instance[propertyName];
        }

        instance::class.memberProperties.find { it.name == propertyName }?.javaGetter?.let {
            return it.invoke(instance)
        }

        instance::class.memberExtensionProperties.find { it.name == propertyName }?.javaGetter?.let {
            return it.invoke(instance)
        }

        instance::class.memberFunctions.find { it.name == "get${propertyName.first().toUpperCase()}${propertyName.substring(1)}" }?.let {
            return it.call(instance)
        }

        instance::class.memberFunctions.find { it.name == "is${propertyName.first().toUpperCase()}${propertyName.substring(1)}" }?.let {
            return it.call(instance)
        }

        return null
    }
}
