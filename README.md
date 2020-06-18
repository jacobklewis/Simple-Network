# Simple Network
Currently not on Bintray/Maven, feel free to download directly.
### Basic Usage
```kotlin
// GET
// -----
// Asynchronously execute the network request
Network.get("https://sample-api-jkl.herokuapp.com/users").enqueue({
    Log.i("DONE", "All Users: ${it.bodyAsString}")
})

// POST
// -----
val net = Network.post("https://sample-api-jkl.herokuapp.com/users", FormDataBody(
        mapOf(
            "age" to FormItem.Text("23"),
            "name" to FormItem.Text("Jake")
        )
    )
)
// Asynchronously execute the network request
net.enqueue({
    Log.i("DONE", "Net Complete ${it.bodyAsString}")
})
```

```kotlin
// GET with Lambda
// -----
fun getUsers(onDone: (userData: String?) -> Unit) {
    Network.get("https://sample-api-jkl.herokuapp.com/users").enqueue({
        onDone(it.bodyAsString)
    })
}
```