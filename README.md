# 🧙 Al Rescate de los Gnomos

> Juego de plataformas desarrollado en Java — Trabajo Práctico de Programación I · UNGS 2024

---

## 📖 Descripción

**Al Rescate de los Gnomos** es un juego de plataformas en el que el jugador controla a **Pep**, un personaje cuyo objetivo es salvar a los gnomos antes de que caigan al vacío o sean eliminados por las tortugas enemigas.

El escenario consiste en una pirámide de islas, donde los gnomos descienden desde la casa ubicada en la cúspide. Las tortugas caen desde el cielo y se desplazan de lado a lado eliminando todo lo que tocan. El jugador tiene **3 vidas** y un tiempo limitado para rescatar la mayor cantidad de gnomos posible.

---

## 🎮 Mecánicas del juego

| Elemento | Descripción |
|---|---|
| 🧝 **Gnomos** | Salen de la casa en la cúspide y descienden por las islas. Hay que salvarlos antes de que caigan al vacío |
| 🐢 **Tortugas** | Caen del cielo y se mueven de izquierda a derecha en su isla, eliminando gnomos |
| 🦸 **Pep** | Personaje controlado por el jugador. Puede moverse, saltar y disparar |
| 🏝️ **Islas** | Plataformas que forman una pirámide. Los personajes interactúan con ellas mediante colisiones |

### Controles
- **→ / ←** — Mover a Pep
- **↑** — Saltar
- **C** — Disparar

---

## 🏗️ Arquitectura del proyecto

El juego está organizado en las siguientes clases:

### `Casa`
Representa la casa de los gnomos ubicada en la cúspide de la pirámide. Almacena posición, dimensiones y color.

### `Isla`
Plataformas que componen el escenario. Cada isla tiene posición, dimensiones y un método `dibujar()` para renderizarse en el entorno.

### `Gnomo`
Personaje que desciende desde la casa. Gestiona su movimiento, caída, estado de espera y reaparición. Se mueve aleatoriamente al crearse.

Lógica destacada:
- `actualizar(Isla[] islas)` — detecta si está sobre una isla o debe caer
- `iniciarEspera()` / `reiniciarEnCasa()` — ciclo de reaparición

### `Tortuga`
Enemigo que cae del cielo y patrulla su isla de lado a lado. Genera su posición inicial aleatoriamente evitando el área de la casa.

Lógica destacada:
- `caer(Isla[], Casa)` — gestiona la caída y detección de islas
- `colisionConGnomo(Gnomo)` — detecta si elimina a un gnomo

### `Disparo`
Proyectil que dispara Pep. Se mueve horizontalmente según la dirección del jugador a una velocidad de 9 unidades por tick.

Lógica destacada:
- `colisionConTortuga(Tortuga)` — detecta impacto contra enemigo
- `colisionConIsla(Isla)` — detecta impacto contra plataforma

### `Pep`
Personaje principal controlado por el jugador. Implementa salto con gravedad (constante `0.4`), disparo y detección de colisiones con islas y tortugas.

Lógica destacada:
- `detenerSaltoEnIsla()` / `salirDeIsla()` / `caerInmediatamente()` — manejo completo de interacciones con plataformas
- `disparar()` — instancia un nuevo objeto `Disparo`

### `Juego`
Clase principal que implementa `InterfazJuego`. Gestiona el loop del juego (`tick()`), el estado global, la lógica de colisiones entre todos los objetos y el renderizado.

---

## 🚀 Cómo ejecutar

### Requisitos
- Java JDK 8 o superior
- IDE recomendado: Eclipse o IntelliJ IDEA

### Pasos
```bash
# Clonar el repositorio
git clone https://github.com/tu-usuario/al-rescate-de-los-gnomos.git

# Abrir el proyecto en Eclipse o IntelliJ
# Compilar y ejecutar la clase principal:
#   src/juego/Juego.java → método main()
```

---

## 👥 Equipo — Grupo 8

| Nombre | Email |
|---|---|
| Matías Abogado | matiabogado2015@gmail.com |
| Santino Gitto | santino.gitto051@gmail.com |
| Franco Herrera | herrerafranco167@gmail.com |
| Franco Pacheco | francodamianpacheco@gmail.com |

**Comisión:** 05 · Programación I · UNGS · 2024

---

## 💡 Lo que aprendimos

Este proyecto nos permitió aplicar por primera vez conceptos de POO en un entorno real:

- Diseñar objetos con responsabilidades claras y lógica propia
- Manejar colisiones entre múltiples entidades con posiciones y dimensiones distintas
- Pensar en estados (en isla, en espera, saltando, cayendo) y transiciones entre ellos
- Trabajar en equipo dividiendo responsabilidades por clase
- Entender que un cambio mínimo en un objeto puede afectar todo el sistema

> *"Llevar la teoría a la práctica es algo totalmente diferente. Este trabajo nos dejó un gran aprendizaje y una fuerte motivación por seguir aprendiendo."*

---

## 📄 Licencia

Proyecto académico — UNGS 2024. Sin licencia comercial.
