# kakapo-crypto

This repository encapsulates the cryptographic functions shared between the Kakapo server and clients. It is mostly a thin layer over top of the [Bouncy-GPG](https://github.com/neuhalje/bouncy-gpg) project.

## Adding to your project

If you are implementing a Kakapo client, you may find it easier to declare a dependency on the [kakapo-client](https://github.com/jeffisaak/kakapo-client) project instead.

In order to declare a dependency on kakapo-crypto, first add jitpack to your repositories:

```
repositories {
    maven { url 'https://jitpack.io' }
}
```

Then add the dependency:

```
dependencies {
    implementation 'com.github.jeffisaak:kakapo-crypto:0.0.2'
}
```
