# Clone de Spotify (Fullstack) Spring boot, PostgreSQL, Auth0 (Backend)

Spring boot backend of the spotify clone

### Key Features :
- 🔐 Security with Auth0 (OAuth2)
- 🎶➕ Music Upload
- 🎧 Audio Streaming
- 🔍 Search Function
- ❤️ User Favorites
- 📱💻 Responsive UI with Bootstrap
- 🅰️ Latest Angular Features: Signal, Standalone Component, New Control Flow Syntax

## Usage
### Prerequisites
- [JDK 21](https://adoptium.net/temurin/releases/)
- IDE (VS code, IntelliJ)
- [PostgreSQL](https://www.postgresql.org/download/)

### Clone the repository
``git clone https://github.com/C0de-cake/spotify-clone-back``

### Setup .env file
Create a .env file at the root of the project
````
POSTGRES_USERNAME= 
POSTGRES_PASSWORD=
POSTGRES_URL=
POSTGRES_DB=

AUTH0_CLIENT_ID=
AUTH0_CLIENT_SECRET=
````

### Fetch the dependencies
``./mvnw install -Dmaven.test.skip=true``

### Launch
Go in IntelliJ add .env file to the environment variables and then run it

# TP Ingeniería y Calidad – Git y GitFlow

Este repositorio contiene un proyecto Java de ejemplo utilizado para practicar control de versiones con **GitHub** y el flujo de trabajo **GitFlow**.

---

## ¿Cómo podemos documentar con Git?

Git permite documentar de varias maneras:

- **Commits**: cada cambio queda registrado con autor, fecha y mensaje descriptivo.  
- **Tags**: sirven para marcar versiones estables (ej. `v1.0.0`).  
- **Branches**: ayudan a separar funcionalidades, releases y correcciones.  
- **Pull Requests (PRs)**: permiten discutir cambios antes de integrarlos.  
- **README.md** y otros archivos de documentación: se versionan igual que el código, de modo que la historia del proyecto incluye la evolución de la documentación.

De esta forma, no solo el código sino también la **historia del proyecto** queda documentada en el repositorio.

---

## Contenido del README

En un proyecto como este, el `README.md` debería incluir:

1. **Descripción del proyecto**: qué hace, en qué lenguaje está hecho.  
2. **Requisitos**: versiones de Java, dependencias, frameworks.  
3. **Instrucciones de instalación y ejecución**.  
4. **Flujo de ramas (GitFlow)**: explicación de `main`, `develop`, `feature/*`, `release/*`, `hotfix/*`.  
5. **Convenciones de commits** (ej. Conventional Commits).  
6. **Cómo contribuir**: lineamientos para Pull Requests.  
7. **Licencia y autores** (si aplica).  
8. **Historial de versiones** (CHANGELOG o sección con tags).  

---

## Lineamientos para Pull Requests (PR)

Si un colaborador externo realiza una modificación, necesitamos que complete la siguiente información:

- **Título claro** del PR (ej. `fix: corrige bug en StatusNotification`).  
- **Descripción detallada**: qué cambia, por qué y cómo se probó.  
- **Issue relacionado** (ej. `Closes #10`).  
- **Checklist** de validaciones:
  - Compila correctamente.  
  - Tests pasan o se agregaron nuevos.  
  - No se subieron archivos locales/secretos.  
  - Documentación actualizada (README/CHANGELOG).  
- **Evidencia**: capturas de pantalla o logs si aplica.  
- **Riesgos y plan de rollback** en caso de fallo.  

---

## ¿Qué nos ofrece GitHub para ayudarnos con esto?

GitHub incluye varias herramientas que mejoran el proceso:

- **Pull Request Templates**: formularios predefinidos para que todos los PRs incluyan la misma información.  
- **Issue Templates**: para reportar bugs o pedir features de forma estandarizada.  
- **Branch Protection Rules**: se pueden proteger ramas como `main` o `develop`, exigiendo revisiones y checks automáticos antes de hacer merge.  
- **Reviews de código**: los revisores pueden dejar comentarios y aprobar cambios.  
- **GitHub Actions**: integración continua para ejecutar tests automáticos en cada PR.  
- **Draft PRs**: permiten abrir un PR en borrador para feedback temprano.  

Estas funciones garantizan que cada aporte externo esté bien documentado, revisado y probado antes de integrarse.

---

## Flujo GitFlow resumido

- `main`: versión productiva.  
- `develop`: integración de funcionalidades.  
- `feature/*`: ramas para nuevas funciones.  
- `release/*`: ramas de estabilización antes de producción.  
- `hotfix/*`: ramas de corrección urgente en producción.
<img width="917" height="520" alt="image" src="https://github.com/user-attachments/assets/403211b7-50fa-46d0-ad6c-b03d88b16649" />

---

## Versionado del README

Este mismo archivo (`README.md`) está versionado en el repositorio.  
Cualquier cambio en la documentación se realiza con commit y queda en el historial, lo que permite rastrear cómo evolucionó la documentación junto al código.
