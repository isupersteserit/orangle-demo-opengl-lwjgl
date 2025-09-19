plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

val lwjglVersion = "3.3.6"
val lwjglNatives = "natives-windows"

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

    // Implementation dependencies for the required LWJGL modules
    implementation("org.lwjgl:lwjgl")
    implementation("org.lwjgl:lwjgl-glfw")
    implementation("org.lwjgl:lwjgl-opengl")


    // For loading textures
    implementation("org.lwjgl:lwjgl-stb")
    runtimeOnly("org.lwjgl:lwjgl-stb::$lwjglNatives")

    // For model importing (if you want .obj/.fbx/.gltf support)
    implementation("org.lwjgl:lwjgl-assimp")
    runtimeOnly("org.lwjgl:lwjgl-assimp::$lwjglNatives")

    // Add JOML
    implementation("org.joml:joml:1.10.5")

    // For sound (OpenAL bindings)
    implementation("org.lwjgl:lwjgl-openal")
    runtimeOnly("org.lwjgl:lwjgl-openal::$lwjglNatives")

    // Runtime-only dependencies for the native libraries
    runtimeOnly("org.lwjgl:lwjgl::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-glfw::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-opengl::$lwjglNatives")
}