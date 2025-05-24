# Guía sobre Peticiones HTTP

## ¿Qué es HTTP?

**HTTP** (HyperText Transfer Protocol) es el protocolo que permite la comunicación entre clientes (como navegadores web) y servidores web. Es el protocolo base para la transferencia de datos en la web.

Cada vez que visitas un sitio web, tu navegador realiza **peticiones HTTP** al servidor del sitio para solicitar información como HTML, imágenes, archivos, datos en JSON, etc.

---

## ¿Qué es una Petición HTTP?

Una **petición HTTP** es un mensaje enviado por el cliente al servidor, solicitando realizar alguna acción. Esta acción puede ser obtener datos, enviar datos, actualizar o eliminar información.

Una petición HTTP se compone de:

- **Método**: Tipo de acción a realizar (GET, POST, etc.)
- **URL**: Recurso al que se accede
- **Cabeceras (Headers)**: Información adicional como tipo de contenido, autenticación, etc.
- **Cuerpo (Body)**: (opcional) Datos enviados al servidor (por ejemplo, en un formulario)

---

## Métodos HTTP más comunes

### 1. `GET`

- **Propósito**: Obtener información del servidor.
- **Ejemplo de uso**: Cargar una página web, obtener datos de una API.
- **Importante**: No debe usarse para enviar datos sensibles; los parámetros se envían en la URL.

```http
GET /usuarios HTTP/1.1
```

### 2. `POST`

- **Propósito**: Enviar datos al servidor para crear un nuevo recurso.
- **Ejemplo de uso**: Enviar un formulario de registro.
- **Importante**: Los datos se envían en el cuerpo de la petición.

```http
POST /usuarios HTTP/1.1
Content-Type: application/json

{
  "nombre": "Martín",
  "correo": "martin@email.com"
}

```