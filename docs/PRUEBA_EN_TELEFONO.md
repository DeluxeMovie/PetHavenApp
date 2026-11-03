# Probar la app en un teléfono físico por USB

La app habla con el backend Spring Boot que corre en el computador. Con el cable USB, `adb reverse`
hace que `localhost:8080` del teléfono llegue al `localhost:8080` del computador. No hace falta WiFi,
ni IP, ni abrir el firewall.

## Si usas otro computador (preparación general)

Haz esto con anticipación, no el mismo día. Necesitas en ese computador:

1. **Git** y acceso a los dos repositorios (app y backend). Clónalos:
   ```
   git clone https://github.com/DeluxeMovie/PetHavenApp.git
   git clone <URL-del-repositorio-del-backend> PetHaven
   ```
   Si el backend no está en GitHub, cópialo en una USB o súbelo a un repositorio privado.
   Asegúrate de que el código esté subido (`git push`) antes de cambiar de equipo.
2. **JDK 21** para el backend (el `pom.xml` pide Java 21). Comprueba con `java -version`.
   No hace falta instalar Maven: el proyecto trae `mvnw`.
3. **Android Studio**, con el SDK de Android que pide el proyecto (compileSdk 37). Al abrir la
   carpeta de la app, Android Studio descarga Gradle y las dependencias solo; necesita internet la
   primera vez. Abrir y sincronizar el proyecto genera su propio `local.properties` (no lo copies de
   otro equipo, contiene rutas locales).
4. **Drivers USB del teléfono** (solo Windows, según la marca; Google USB Driver o el del fabricante).
   En Mac y Linux normalmente no hace falta nada.
5. **adb**: viene con Android Studio en la carpeta `platform-tools` del SDK. Su ruta cambia de un
   equipo a otro. En Android Studio: *Settings → Languages & Frameworks → Android SDK* muestra la
   ruta del SDK, y adb está en `<ruta-del-SDK>/platform-tools/adb` (`adb.exe` en Windows).
   En los comandos de esta guía reemplaza la ruta de adb por la de ese equipo.
6. **Prueba completa una vez** en ese computador: backend arriba, `adb devices`, `adb reverse`,
   Run, login y ver las mascotas.

Los pasos siguientes son iguales en cualquier computador.

## Una sola vez (preparación del teléfono)

1. En el teléfono: **Ajustes → Acerca del teléfono** y toca 7 veces **Número de compilación**
   (activa las opciones de desarrollador).
2. **Ajustes → Opciones de desarrollador → Depuración por USB**: activar.
3. Conecta el cable al computador y acepta en el teléfono el aviso
   "¿Permitir depuración por USB?" (marca "Permitir siempre desde este equipo").
4. Usa un cable que transmita datos (algunos solo cargan).

## El día de la sustentación

### 1. Levantar el backend

En `D:\Universidad\PetHaven\pethaven`:

```
./mvnw spring-boot:run
```

Espera a ver `Started PethavenBackendApplication`. Comprueba en el navegador del computador:
`http://localhost:8080/api/owners/1/pets` debe devolver un JSON.

> La base de datos se recrea en cada arranque (`create-drop`), así que los datos de prueba vuelven a
> cargarse solos. Si reinicias el backend con la app abierta, vuelve a iniciar sesión.

### 2. Conectar el teléfono y abrir el túnel

Verifica que el teléfono aparece (debe decir `device`, no `unauthorized`):

```
"D:\Visual studio sdk\Android\platform-tools\adb.exe" devices
```

Abre el túnel:

```
"D:\Visual studio sdk\Android\platform-tools\adb.exe" reverse tcp:8080 tcp:8080
```

Debe responder `8080` (o no mostrar nada). **Hay que repetirlo cada vez que desconectes y vuelvas a
conectar el cable, o reinicies adb.**

### 3. Cambiar la app a modo USB

En `app/src/main/java/com/example/pethavenapp/data/remote/NetworkConfig.kt`:

```kotlin
const val USE_USB_DEVICE = true
```

(Vuelve a `false` si usas el emulador.)

### 4. Instalar y abrir la app

En Android Studio elige el teléfono en la lista de dispositivos y pulsa **Run** (variante *debug*).

Inicia sesión con `john.doe@example.com` / `password123`.

## Si algo falla

| Síntoma | Causa y solución |
|---|---|
| El teléfono no aparece en `adb devices` | Cable sin datos, depuración USB apagada, o falta aceptar el aviso en el teléfono |
| Aparece `unauthorized` | Acepta el aviso de depuración en el teléfono (o desconecta y reconecta) |
| "No pudimos conectarnos" | Backend apagado, o falta el `adb reverse` (se pierde al reconectar el cable), o `USE_USB_DEVICE` sigue en `false` |
| "El correo o la contraseña no son correctos" | El backend sí responde; revisa las credenciales del dueño de prueba en `DataLoader` |
| Lista vacía | Ese dueño no tiene mascotas en los datos de prueba |
| Huellas en lugar de fotos | El computador necesita internet: el backend descarga las fotos de un host externo |
| Puerto 8080 ocupado al levantar el backend | Cierra otra instancia del backend o el proceso que usa el puerto |

## Alternativa sin cable (mismo WiFi)

1. Averigua la IP del computador con `ipconfig` (ej. `192.168.1.20`).
2. Cambia `BASE_URL` a `http://192.168.1.20:8080/api/`.
3. Añade esa IP como `<domain>` en `app/src/debug/res/xml/network_security_config.xml`.
4. Permite el puerto 8080 en el Firewall de Windows y conecta ambos a la misma red.
   Las redes de universidades suelen aislar los dispositivos entre sí, por eso USB es más fiable.
