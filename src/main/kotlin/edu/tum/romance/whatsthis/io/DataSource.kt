package edu.tum.romance.whatsthis.io

interface DataSource<T>: TextToVec<T> {
    fun load(resource: T): String
}