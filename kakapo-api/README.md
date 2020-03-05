# kakapo-api

Java objects that get serialized to and deserialized from JSON when communicating between the Kakapo server and client(s).

## Adding to your project

If you are implementing a Kakapo client in Java, you may find it easier to declare a dependency on the [kakapo-client](https://github.com/jeffisaak/kakapo-client) project instead.

In order to declare a dependency on kakapo-api, first add jitpack to your repositories:

```
repositories {
    maven { url 'https://jitpack.io' }
}
```

Then add the dependency:

```
dependencies {
    implementation 'com.github.jeffisaak:kakapo-api:0.0.6'
}
```
