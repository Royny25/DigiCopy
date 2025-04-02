# DigiCopy - Sistema de Inventario y Facturación

Sistema de gestión para imprentas y papelerías que permite el control de inventario, facturación, proveedores y cuentas por pagar.

## Requisitos

- Java 17 o superior
- SQLite (incluido como dependencia)

## Funcionalidades

El sistema incluye los siguientes módulos:

- **Gestión de Productos**: Permite agregar, modificar, eliminar y consultar productos del inventario.
- **Gestión de Proveedores**: Administra la información de proveedores (en desarrollo).
- **Control de Inventario**: Monitorea el stock de productos (en desarrollo).
- **Facturación**: Generación de facturas para clientes (en desarrollo).
- **Cuentas por Pagar**: Control de pagos a proveedores (en desarrollo).
- **Reportes**: Generación de reportes de ventas e inventario (en desarrollo).

## Estructura del Proyecto

- `src/`: Código fuente
  - `controller/`: Controladores para la lógica de negocio
  - `model/`: Clases de modelo para representar entidades
  - `view/`: Interfaces gráficas
  - `database/`: Gestión de la conexión a la base de datos
  - `digicopy/`: Clase principal del sistema
- `sql/`: Scripts SQL para la inicialización de la base de datos
- `lib/`: Bibliotecas dependientes (SQLite)

## Configuración y Ejecución

1. Asegúrate de tener Java 17 o superior instalado.
2. Abre el proyecto en NetBeans (preferentemente) o en cualquier IDE compatible con Java.
3. Verifica que la biblioteca SQLite esté correctamente configurada en las propiedades del proyecto:
   - Haz clic derecho en el proyecto > Propiedades > Bibliotecas
   - Añade la biblioteca SQLite si no está presente
4. Ejecuta la clase `Main` en el paquete `digicopy`.

## Base de Datos

El sistema utiliza SQLite como motor de base de datos. La primera vez que se ejecuta el sistema, se crea automáticamente la base de datos `inventory.db` con tablas y datos de muestra.

## Notas de Implementación

- Los errores de paquete en tiempo de desarrollo no afectan la ejecución del sistema.
- La base de datos se inicializa con datos de muestra para productos y proveedores.
- Asegúrese de que el archivo `sql/init_database.sql` esté accesible para la inicialización correcta de la base de datos.

## Solución de Problemas

Si encuentra errores al ejecutar el sistema:

1. Verifique que Java 17 o superior esté instalado y configurado correctamente.
2. Verifique que la biblioteca SQLite esté correctamente configurada.
3. Asegúrese de que todos los archivos estén en la ubicación correcta según la estructura del proyecto.
4. Si aparecen errores de base de datos, elimine el archivo `inventory.db` y reinicie la aplicación para que se cree nuevamente.

## Contacto

Para soporte técnico o consultas, contacte al desarrollador. 