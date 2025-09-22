<img width="834" height="176" alt="OldOrangleLogo" src="https://github.com/user-attachments/assets/baaf766b-57a0-4825-8f99-64bcca3e5928" />

#

<img width="834" alt="OldUMLDiagram" src="https://img.plantuml.biz/plantuml/png/PL9BRiGW3Drp2fRjWXkCggbcMNKLQRfsY9jOvHCPQ5gDvku5aamITOVzFduOpY42nsIDq0P2aEz0Jjw4k5cCW-_kafDxUjvARH1S6dFhBXO9EOfPyg2CxFkckEv9nllnuAi7r5zl-DkIwy9-0cERo7HNmcasM2qqM6JOJzAC4F5I-mjtfeO1EkHCLRA9JAwNtyWHVvhQRtnYZHlsoT1RTF60RaEy7SdW31wcJ3cwd_u7-gZz_UrrQh6cAOThZa8QmmQ38mkHlDjxpKsyP4MscgMlhZTOT7NdIhtKzn9rkMknhAc0XIMMNJpgAZsf9RseqLPi2t9IghvPlkmPNLS-o1y0" />

## UML (as Code)

````
@startuml
class Main {

{method} run()
{method} cleanup()
}

class init {
{method} progStart()
}

class loop {

{method} checkKeybinds()
{method} rebuildView()
}



class camera {
{method} createCamera()
}


class shader {
- String vertexShaderSource
- String fragmentShaderSource
{method} compileShader()
}


class window {
{method} createWindow()
}

class cube {
- cubeVertices
}

class animation {
{method} animateCube()
- cubeAngle
}

init --> Main
loop -> Main
camera --> init
shader --> init
init <- window
cube -> init
animation <-- loop


@enduml
````