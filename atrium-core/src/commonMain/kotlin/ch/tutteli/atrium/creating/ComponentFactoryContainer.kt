package ch.tutteli.atrium.creating

import ch.tutteli.atrium.core.polyfills.fullName
import ch.tutteli.atrium.creating.impl.ComponentFactoryContainerImpl
import kotlin.reflect.KClass
import ch.tutteli.atrium.reporting.Reporter

@Suppress("DEPRECATION" /* RequiresOptIn is only available since 1.3.70 which we cannot use if we want to support 1.2 */)
@RequiresOptIn
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS, AnnotationTarget.PROPERTY, AnnotationTarget.LOCAL_VARIABLE)
annotation class ExperimentalComponentFactoryContainer

/**
 * Manages the factories to create the different components of Atrium.
 * It takes basically the responsibility of a dependency injection facility, tailored for Atrium.
 */
@ExperimentalComponentFactoryContainer
interface ComponentFactoryContainer {

    /**
     * Returns the component of type [I] using a corresponding factory or `null` in case no factory was found
     * which is able to build a component of the given type.
     *
     * @throws ClassCastException in case the factory returns an illegal type.
     */
    fun <I : Any> buildOrNull(kClass: KClass<I>): I?

    /**
     * Returns a chain of components of type [I] using a corresponding chain of factories or `null`
     * in case no chain was found which is able to build a chain of components of the given type.
     *
     * @throws ClassCastException in case one of factories returns an illegal type.
     */
    fun <I : Any> buildChainedOrNull(kClass: KClass<I>): Sequence<I>?

    /**
     * Returns a factory which is able to build a component for the given [kClass]
     * or `null` in case no factory was found which is able to build a component of the given type.
     */
    fun getFactoryOrNull(kClass: KClass<*>): ComponentFactory?

    /**
     * Returns a chain of factories which shall build a chain of components of the specified [kClass]
     * or `null` in case no chain was found which is able to build a chain of components of the given type.
     */
    fun getFactoryForChainedOrNull(kClass: KClass<*>): Sequence<ComponentFactory>?


    /**
     * Merges the given [componentFactoryContainer] (if not `null`) with `this` [ComponentFactoryContainer]
     * creating a new [ComponentFactoryContainer] where defined dependencies in [componentFactoryContainer]
     * will have precedence over dependencies defined in this instance.
     *
     * For instance, this object has defined a [Reporter] and
     * the given [componentFactoryContainer] as well, then the resulting [ComponentFactoryContainer] will return the [Reporter]
     * of [componentFactoryContainer] when asked for it.
     */
    fun merge(componentFactoryContainer: ComponentFactoryContainer?): ComponentFactoryContainer

    companion object {
        fun createIfNotEmpty(
            components: Map<KClass<*>, ComponentFactory>,
            chainedComponents: Map<KClass<*>, Sequence<ComponentFactory>>
        ): ComponentFactoryContainer? =
            if (components.isEmpty() && chainedComponents.isEmpty()) null
            else ComponentFactoryContainerImpl(
                if (components.isEmpty()) emptyMap() else HashMap(components),
                if (chainedComponents.isEmpty()) emptyMap() else HashMap(chainedComponents)
            )

    }
}

/**
 * Provides a [build] lambda which produces the component and specifies via [producesSingleton] whether this
 * component should be treated as singleton or not.
 */
@ExperimentalComponentFactoryContainer
data class ComponentFactory(val build: (ComponentFactoryContainer) -> Any, val producesSingleton: Boolean)

/**
 * Returns the component of type [I] using a corresponding factory or throws an [IllegalStateException]
 * in case no factory was found which is able to build a component of the given type.
 *
 * @throws IllegalStateException in case [ComponentFactoryContainer.buildOrNull] returns `null`
 *   because not suitable factory was found.
 * @throws ClassCastException in case the factory returns an illegal type.
 */
@ExperimentalComponentFactoryContainer
inline fun <reified I : Any> ComponentFactoryContainer.build(): I = buildOrNull(I::class)
    ?: throw IllegalStateException("No factory is registered in this ComponentContainer which is able to build a ${I::class.fullName}")


/**
 * Returns a chain of components of type [I] using a corresponding factory or throws an [IllegalStateException]
 * in case no factory was found which is able to build a chain of components of the given type.

 * @throws IllegalStateException in case [ComponentFactoryContainer.buildChainedOrNull] returns `null`
 *   because no suitable factory was found.
 * @throws ClassCastException in case one of factories returns an illegal type.
 */
@ExperimentalComponentFactoryContainer
inline fun <reified I : Any> ComponentFactoryContainer.buildChained(): Sequence<I> = buildChainedOrNull(I::class)
    ?: throw IllegalStateException("No factory is registered in this ComponentContainer which is able to build a chain of ${I::class.fullName}")
