package net.volcano.jdautilities.utils

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.isSubclassOf

object ClassUtil {

	@JvmStatic
	fun getGenericType(clazz: Class<*>): Type {
		return (clazz.genericSuperclass as ParameterizedType).actualTypeArguments[0]
	}

	@JvmStatic
	fun stripWildcard(clazz: Class<*>): Class<*> {
		return try {
			(clazz.genericSuperclass as ParameterizedType).rawType as Class<*>
		} catch (e: Exception) {
			clazz
		}
	}

	@JvmStatic
	fun stripWildcard(type: Type): Type {
		return try {
			(type as ParameterizedType).rawType
		} catch (e: Exception) {
			type
		}
	}

	@JvmStatic
	fun dePrimitivize(clazz: Class<*>): Class<*> {
		if (clazz.isPrimitive) {
			if (clazz == java.lang.Byte.TYPE) {
				return java.lang.Byte::class.java
			} else if (clazz == java.lang.Short.TYPE) {
				return java.lang.Short::class.java
			} else if (clazz == Integer.TYPE) {
				return java.lang.Integer::class.java
			} else if (clazz == java.lang.Long.TYPE) {
				return java.lang.Long::class.java
			} else if (clazz == java.lang.Boolean.TYPE) {
				return java.lang.Boolean::class.java
			} else if (clazz == Character.TYPE) {
				return java.lang.Character::class.java
			} else if (clazz == java.lang.Float.TYPE) {
				return java.lang.Float::class.java
			} else if (clazz == java.lang.Double.TYPE) {
				return java.lang.Double::class.java
			}
		}
		return clazz
	}
}

val KType.enumConstants: Array<out Enum<*>>?
	get() {
		return if (this.isEnum) {
			if (this.isArray)
				(this.componentType!!.java as Class<Enum<*>>).enumConstants
			else
				((this.classifier!! as KClass<*>).java as Class<Enum<*>>).enumConstants
		} else {
			return null
		}
	}

val KType.isArray: Boolean
	get() {
		return this.classifier?.let { c ->
			(c as KClass<*>).java.isArray
		} ?: false
	}

/**
 * Inner type, if this is an array
 */
val KType.componentType: KClass<*>?
	get() {
		return if (this.isArray) {
			this.classifier?.let { c ->
				(c as KClass<*>).java.componentType.kotlin
			}
		} else null
	}

val KClass<*>.isEnum: Boolean
	get() = this.isSubclassOf(Enum::class)

val KClass<*>.isPrimitive: Boolean
	get() = this.primitive != null

val KClass<*>.primitive: KClass<*>?
	get() = this.javaPrimitiveType?.kotlin

val KType.kClass: KClass<*>
	get() = this.classifier!! as KClass<*>

val KType.isPrimitive: Boolean
	get() = this.kClass.isPrimitive

val KType.isEnum: Boolean
	get() = this.kClass.isEnum

