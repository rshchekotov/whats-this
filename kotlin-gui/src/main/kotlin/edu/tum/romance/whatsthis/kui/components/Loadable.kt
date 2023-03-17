package edu.tum.romance.whatsthis.kui.components

/**
 * Interface for components that can be loaded and unloaded.
 * This is useful for components that need to be initialized
 * before they can be used.
 */
interface Loadable {
    /**
     * Called when the component is loaded.
     */
    fun onLoad()

    /**
     * Called when the component is unloaded.
     */
    fun onUnload()
}