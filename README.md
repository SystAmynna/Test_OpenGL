# Apprentissage de OpenGL via LWJGL
## [LearnOpenGL](https://learnopengl.com/)

## Statut des Applications
* ✅: *Fonctionnel*
* 🚧: *En cours*
* ❌: *Obsolète*

<u>**Chapitre 1:**</u>  
* App1: ✅
* App2: ✅
* App3: ✅
* App4: ❌ (normal)
* AppCube: ✅
* App5: ✅  

<u>**Chapitre 2:**</u>  
* App6: ✅
* App7: 🚧

## Chapitre 1
<u>**Temps effectif**: *2 ~ 3 semaines*</u>

### Tâches:
* Créer une fenêtre
* Créer un triangle
* Créer un shader
* Implémenter une texture
* Implémenter les transformations
* Système de coordonnées (passage à la 3D)
* Caméra

### Notes:

#### Création d'outils
* **App**: Classe parente des applications (sauf AppCube, je cherchais à régler un bug, le bug ne vient pas de App)
* **Shader**: Classe pour gérer les shaders
* **Objet**: Classe incarnant un objet dans l'environnement d'OpenGL (ayant une forme/model, une/des texture(s) et shader(s))  
⚠️ *Fonctionne mal avec les matrices de transformations*
* **Camera**: Classe pour gérer la caméra (Non testé pour l'instant, mais algorithmiquement en provenance de LearnOpenGL (C++))

#### Remarques

**⌨️ Controls**: dans un Thread différé à celui de rendu, stocke les inputs sous forme de booléens. Récupérés au début de l'UPDATE du jeu depuis le "glPollEvents" de GLFW du Thread de rendu.  
**🛠️ Création d'un nouvel Objet**: devra ajouter son objet graphique à une pile d'initialisation qui sera traité et vidé au début de la boucle de rendu (en dehors du control des FPS si présent).

### Glossaire

* **OpenGL**:
  * **VBO**: Vertex Buffer Object
  * **VAO**: Vertex Array Object
  * **EBO**: Element Buffer Object
  * **GLSL**: OpenGL Shading Language
  * **GLFW**: Graphics Library Framework
  * **GLEW**: OpenGL Extension Wrangler Library
  * **GLM**: OpenGL Mathematics
  * **stb_image**: Library pour charger les images