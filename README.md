<div align="center">

<img src="app/src/main/res/drawable-nodpi/pethaven_logo.webp" alt="PetHaven" width="220"/>

# PetHaven App

**Aplicación móvil para dueños — complemento de la plataforma web PetHaven**

![Kotlin](https://img.shields.io/badge/Kotlin-2.2-7F52FF?logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-Material_3-4285F4?logo=jetpackcompose&logoColor=white)
![Ktor](https://img.shields.io/badge/Ktor_Client-3.3-087CFA?logo=ktor&logoColor=white)
![Android](https://img.shields.io/badge/Android-7.0%2B-3DDC84?logo=android&logoColor=white)

</div>

---

## Sobre la app

[PetHaven](https://github.com/JoythDev/pethaven) es una clínica veterinaria especializada en la **hospitalización de perros y gatos**. La plataforma web (Spring Boot + Angular) la usan veterinarios y administradores; esta aplicación móvil es su complemento para el **dueño de la mascota**, que puede seguir desde su teléfono cómo va su compañero hospitalizado.

La app consume la **misma API REST** que la web, así que los datos siempre coinciden con los que registra el veterinario.

### Qué puede hacer el dueño

1. **Iniciar sesión** con su correo y contraseña.
2. **Ver sus mascotas** en una grilla de tarjetas: foto, nombre, especie, raza y si están en la veterinaria o en casa.
3. **Consultar la ficha de cada mascota** al tocarla: edad, peso, diagnóstico actual y el historial de tratamientos (fecha, veterinario, evolución, medicamentos administrados y notas).

---

## Equipo

| Integrante      | GitHub                                              |
|-----------------|-----------------------------------------------------|
| Ángel García    | [DeluxeMovie](https://github.com/DeluxeMovie)       |
| Juliana Pacheco | [JuliiPa](https://github.com/JuliiPa)               |
| Manuel Rincon   | [ManuelRincon90](https://github.com/ManuelRincon90) |
| Nicolás Joya    | [JoythDev](https://github.com/JoythDev)             |

---

## Cómo funciona

```
┌────────────────────┐   HTTP + JSON    ┌──────────────────────┐
│  PetHaven App      │ ───────────────▶ │  API REST PetHaven   │
│  (Compose + Ktor)  │ ◀─────────────── │  (Spring Boot :8080) │
└────────────────────┘                  └──────────────────────┘
```

### Endpoints que consume

| Acción | Endpoint |
|---|---|
| Iniciar sesión | `POST /api/auth/owners/login` |
| Mascotas del dueño | `GET /api/owners/{id}/pets` |
| Datos de una mascota | `GET /api/pets/{id}` |
| Tratamientos de una mascota | `GET /api/pets/{id}/treatments` |
| Foto de una mascota | `GET /api/pets/{id}/photo` |

El contrato completo de la API está en el repositorio del backend: [`docs/api/API.md`](https://github.com/JoythDev/pethaven/blob/main/docs/api/API.md).

> La API no emite tokens: el login devuelve los datos del dueño (`id`, `name`, `email`, `role`) y la app guarda esa sesión en memoria mientras está abierta.

### Arquitectura

Capas simples, con la UI independiente de la red:

```
app/src/main/java/com/example/pethavenapp/
├── data/
│   ├── model/        Modelos de dominio (User, Pet, Treatment…)
│   ├── remote/       Cliente Ktor, APIs y DTOs (JSON → modelos)
│   ├── repository/   Interfaces y su implementación con Ktor
│   └── AppContainer  Punto único que crea las dependencias
├── viewmodels/       Estado de cada pantalla (StateFlow)
├── ui/
│   ├── screens/      Bienvenida, Login, Inicio y Detalle de mascota
│   ├── components/   Componentes reutilizables
│   └── theme/        Paleta de marca, tipografía y fondos
├── navigation/       Rutas y NavHost
└── util/             Validadores y medidas responsive
```

- **UI:** Jetpack Compose con Material 3 y diseño responsive (teléfono, horizontal y tableta).
- **Red:** Ktor Client (OkHttp) con `kotlinx.serialization`.
- **Fotos:** Coil.
- **Estado:** ViewModel + `StateFlow`.

---

## Cómo ejecutarla

La app necesita el **backend corriendo** en el computador. Son dos procesos a la vez.

### Requisitos

- Android Studio reciente, con el SDK de Android.
- JDK 21 para el backend.
- Emulador o un teléfono Android 7.0 (API 24) o superior.
- El repositorio del backend: [JoythDev/pethaven](https://github.com/JoythDev/pethaven).

### Pasos

1. **Levanta el backend** (en la carpeta del backend):
   ```
   ./mvnw spring-boot:run
   ```
   Comprueba que responde: `http://localhost:8080/api/owners/1/pets` debe devolver un JSON.
2. **Abre esta carpeta en Android Studio** y espera a que sincronice Gradle.
3. **Elige dónde correrla** y ajusta `USE_USB_DEVICE` en
   [`NetworkConfig.kt`](app/src/main/java/com/example/pethavenapp/data/remote/NetworkConfig.kt):

   | Dispositivo | `USE_USB_DEVICE` | Extra |
   |---|---|---|
   | Emulador de Android Studio | `false` | Nada más |
   | Teléfono físico por USB | `true` | `adb reverse tcp:8080 tcp:8080` |

4. Pulsa **Run** (variante *debug*) e inicia sesión con el dueño de prueba:

   | Correo | Contraseña |
   |---|---|
   | `john.doe@example.com` | `password123` |

> Para probar la interfaz sin backend, pon `USE_FAKE_BACKEND = true` en `NetworkConfig.kt`.
> Solo simula el login; las mascotas siempre se piden a la API.

### Probar en un teléfono físico

El paso a paso (incluido cómo prepararlo en otro computador y qué hacer si falla) está en
[`docs/PRUEBA_EN_TELEFONO.md`](docs/PRUEBA_EN_TELEFONO.md).

---

## Notas importantes

- **Solo desarrollo local.** El tráfico HTTP sin cifrar solo se permite en la variante *debug* hacia
  `10.0.2.2` y `localhost`. Una versión de producción requeriría un backend con HTTPS.
- **Datos temporales.** El backend recrea su base H2 en cada arranque (`create-drop`); si lo reinicias,
  vuelve a iniciar sesión en la app.
- **Fotos.** El backend descarga las fotos de un host externo, así que el computador necesita internet
  para que se vean.
- **Seguridad.** La API actual no usa tokens, por lo que no está pensada para exponerse a internet.

---

## Pruebas

```
./gradlew test
```
