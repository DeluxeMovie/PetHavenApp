package com.example.pethavenapp.data.remote

object NetworkConfig {
    /** Cambia a true para trabajar sin el servidor Spring Boot (solo simula el login). */
    const val USE_FAKE_BACKEND = false

    /**
     * true: teléfono físico conectado por USB (requiere `adb reverse tcp:8080 tcp:8080`).
     * false: emulador de Android Studio.
     * Paso a paso completo en docs/PRUEBA_EN_TELEFONO.md.
     */
    const val USE_USB_DEVICE = false

    /**
     * true: usa el backend desplegado en Render (HTTPS) y tiene prioridad sobre USE_USB_DEVICE.
     * El plan gratuito de Render duerme el servicio: la primera petición puede tardar cerca de un minuto.
     */
    const val USE_RENDER_BACKEND = true

    const val RENDER_BASE_URL = "https://pethaven-backend-bl6h.onrender.com/api/"

    /**
     * Emulador: 10.0.2.2 es el localhost de tu computador visto desde el emulador.
     * USB: `adb reverse` hace que localhost del teléfono llegue al localhost del computador.
     * Debe terminar en "/" para que las rutas relativas de las APIs se resuelvan bien.
     */
    val BASE_URL = when {
        USE_RENDER_BACKEND -> RENDER_BASE_URL
        USE_USB_DEVICE -> "http://localhost:8080/api/"
        else -> "http://10.0.2.2:8080/api/"
    }

    const val CONNECT_TIMEOUT_MILLIS = 10_000L

    /** Con Render se espera hasta 90 s para cubrir el arranque en frío del servicio. */
    val REQUEST_TIMEOUT_MILLIS = if (USE_RENDER_BACKEND) 90_000L else 15_000L
}
