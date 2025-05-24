# Stocker Backend

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)

Repositorio para el backend de [Stocker](http://www.github.com/martin-amaro/stocker)

## Tecnologías utilizadas

Este proyecto está siendo desarrollado con las siguientes tecnologías:

- **SpringBoot**
- **Java**
- **MySQL**

## **Instalación**

Para instalar y ejecutar el proyecto en tu máquina local, sigue estos pasos:

1. **Clona el repositorio**:
    ```bash
    git clone https://github.com/martin-amaro/stocker-backend.git
    ```

2. **Renombra el archivo `application.properties.example` por `application.properties`y modifica las credenciales de la base de datos en MySQL**:
    ```bash
    spring.datasource.url=jdbc:mysql://
    spring.datasource.username=
    spring.datasource.password=
    ```

    Puedes obtener credenciales gratuitas en [Aiven](https://aiven.io/).

3. **Inicia el proyecto**:
    Abre y ejecuta StockerApplication.java.
