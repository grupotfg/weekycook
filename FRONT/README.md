# WeekyCook - Frontend

Este directorio contiene el código frontend de la aplicación WeekyCook.

## Estructura

- `index.html` - Página principal con la sección de Favoritos
- `styles.css` - Estilos genéricos y específicos de componentes

## Características

### Página de Favoritos
La página muestra las recetas favoritas del usuario en un diseño de tarjetas (cards) responsive.

#### Componente Recipe Card
Cada tarjeta incluye:
- Imagen de la receta
- Botón de favorito (corazón) correctamente posicionado en la esquina superior derecha
- Título de la receta
- Descripción breve
- Metadatos (tiempo de preparación, dificultad)

#### Estilos
Los estilos están organizados en dos niveles:
1. **Estilos genéricos** (`styles.css`): Variables CSS, reset, estilos de layout y componentes base
2. **Estilos específicos del componente** (en `styles.css`): Estilos particulares de las tarjetas de recetas

##### Botón de Favorito (Corazón)
El botón de favorito está correctamente alineado con las siguientes especificaciones:
- Posición: Absoluta, esquina superior derecha
- Distancia desde el borde: 12px (top y right)
- Tamaño: 40px x 40px (botón circular)
- Ícono: 24px x 24px
- Color activo: Rosa (#E91E63)
- Fondo: Blanco semi-transparente con sombra
- Animación: Efecto de "heartBeat" al hacer clic

## Cómo visualizar

Para ver la página localmente:

```bash
cd FRONT
python3 -m http.server 8080
```

Luego abre `http://localhost:8080/index.html` en tu navegador.

## Responsive Design

La página se adapta a diferentes tamaños de pantalla:
- Desktop: Grid de múltiples columnas
- Tablet: Grid adaptativo
- Mobile: Una columna

## Tecnologías

- HTML5
- CSS3 con variables CSS
- Diseño responsive con CSS Grid
- SVG para iconos
