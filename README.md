# MiTaller

Plataforma web de búsqueda y reserva de talleres mecánicos, inspirada en el modelo de Top Doctors pero aplicada al sector de la automoción.

Proyecto final del CFGS de Desarrollo de Aplicaciones Web (DAW) — IES El Cañaveral, Móstoles. Curso 2025-2026.

## Descripción

MiTaller nace de una necesidad detectada en el mercado: mientras el sector sanitario cuenta con plataformas consolidadas para buscar profesionales y reservar citas, el sector de talleres mecánicos carece de una solución equivalente. La plataforma permite a los usuarios buscar talleres por localidad y especialidad, consultar fichas detalladas, leer reseñas verificadas y solicitar citas online.

## Funcionalidades

- Registro e inicio de sesión con roles diferenciados (CLIENTE / ADMIN)
- Búsqueda de talleres por localidad, especialidad y nombre
- Fichas de taller con información completa y valoraciones
- Sistema de reseñas verificadas (una reseña por usuario y taller)
- Solicitud de citas online con fecha, hora y tipo de servicio
- Panel de administración para gestión de talleres y citas
- Diseño responsive adaptado a móvil, tablet y escritorio

## Stack tecnológico

- **Backend:** Java 17, Spring Boot 3.2.5
- **Persistencia:** Spring Data JPA, Hibernate
- **Base de datos:** H2 (desarrollo), MySQL 8 (producción)
- **Seguridad:** Spring Security 6 con BCrypt
- **Vistas:** Thymeleaf
- **Frontend:** Bootstrap 5.3, Bootstrap Icons
- **Build:** Apache Maven

## Arquitectura

El proyecto sigue el patrón Modelo-Vista-Controlador (MVC), organizado en paquetes por responsabilidad:

\`\`\`
com.mitaller/
├── config/        Configuración de seguridad y datos iniciales
├── model/         Entidades JPA (Usuario, Taller, Especialidad, Reseña, Cita)
├── repository/    Repositorios Spring Data JPA
├── service/       Lógica de negocio
└── controller/    Controladores REST y de vistas
\`\`\`

## Requisitos

- JDK 17 o superior
- Apache Maven 3.9 o superior
- Navegador web moderno

## Instalación y arranque

Clonar el repositorio:

\`\`\`bash
git clone https://github.com/ManuelMatosA/mitaller.git
cd mitaller
\`\`\`

Arrancar la aplicación:

\`\`\`bash
mvn spring-boot:run
\`\`\`

Acceder desde el navegador a:

\`\`\`
http://localhost:8080
\`\`\`

## Credenciales de prueba

| Usuario | Contraseña | Rol     |
|---------|------------|---------|
| admin   | admin123   | ADMIN   |
| manuel  | 123456     | CLIENTE |
| laura   | 123456     | CLIENTE |

## Perfiles de configuración

El proyecto incluye dos perfiles de Spring:

- **h2** (por defecto): base de datos embebida, sin instalación adicional
- **mysql**: configurado para despliegue en producción

Para cambiar de perfil, modificar la propiedad \`spring.profiles.active\` en \`application.properties\`.

## Autor

**Manuel Matos**
CFGS Desarrollo de Aplicaciones Web
IES El Cañaveral — Móstoles, Madrid
