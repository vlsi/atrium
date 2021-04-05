//TODO remove file with 0.18.0
@file:Suppress("DEPRECATION", "DeprecatedCallableAddReplaceWith")

package ch.tutteli.atrium.core.polyfills

import kotlin.reflect.KClass

private val serviceRegistry = mutableMapOf<KClass<*>, HashSet<Any>>()

@Deprecated("Retrieve components via ComponentFactoryContainer; will be removed with 0.18.0")
actual fun <T : Any> loadSingleService(kClass: KClass<T>): T =
    useSingleService(kClass, loadServices(kClass).iterator())

@Deprecated("Retrieve components via ComponentFactoryContainer; will be removed with 0.18.0")
actual fun <T : Any> loadServices(kClass: KClass<T>): Sequence<T> {
    @Suppress("UNCHECKED_CAST" /* we have a homogeneous map but make sure insertions are type safe, thus OK */)
    val set = serviceRegistry[kClass] as Set<() -> T>?
    return set?.asSequence()?.map { it() } ?: emptySequence()
}

/**
 * Registers the given [service] for the service of type [T].
 */
@Deprecated("Retrieve components via ComponentFactoryContainer; will be removed with 0.18.0")
inline fun <reified T : Any> registerService(noinline service: () -> T) = registerService(T::class, service)

/**
 * Registers the given [service] for the given [serviceInterface].
 */
@Deprecated("Retrieve components via ComponentFactoryContainer; will be removed with 0.18.0")
fun <T : Any> registerService(serviceInterface: KClass<T>, service: () -> T) {
    val services = serviceRegistry.getOrPut(serviceInterface) { hashSetOf() }
    services.add(service)
}
