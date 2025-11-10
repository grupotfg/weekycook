CREATE DATABASE IF NOT EXISTS weekycook_bd;
USE weekycook_bd;

CREATE TABLE usuarios (
  id INT AUTO_INCREMENT PRIMARY KEY,
  correo VARCHAR(255) NOT NULL UNIQUE,
  nombre VARCHAR(100) NOT NULL,
  apellido VARCHAR(100), 
  contraseña VARCHAR(255) NOT NULL, 
  es_admin TINYINT(1) NOT NULL DEFAULT 0, -- 0 para usuarios por eso está por defecto y 1 para admin
  fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  descripcion TEXT,                       -- descripción del perfil, esto nos lo podemos ahorrar tb, lo vemos
  num_comensales_defecto INT DEFAULT 2,   -- valor por defecto 2 y si lo hacemos podremos escalar raciones como mejora
  INDEX idx_usuarios_correo (correo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--------------------------------------------------------------------------------
-- TABLA: categorias
--------------------------------------------------------------------------------
-- Para clasificar recetas (ej. 'Postres', 'Ensaladas', 'Italiana', 'vegano').
-- (id, nombre, descripción) para poder filtrar/buscar.
--------------------------------------------------------------------------------

CREATE TABLE categorias (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL UNIQUE,
  descripcion VARCHAR(255), -- a lo mejor esto tb nos lo podemos ahorrar
  INDEX idx_categorias_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--------------------------------------------------------------------------------
-- TABLA: ingredientes
--------------------------------------------------------------------------------
-- Mix de lo que tenemos, por lo que meto valores nutricionales por *unidad_base* para
-- permitir calcular el total de recetas como posible mejora, la inicial solo tenia id y nombre. 
-- **unidad_base** x ejemplo: 'gr', 'ml', 'cucharada' — define los valores.
--------------------------------------------------------------------------------

CREATE TABLE ingredientes (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(150) NOT NULL UNIQUE,
  unidad_base VARCHAR(50) NOT NULL,         -- Añado unidad de referencia
  calorias_por_unidad DECIMAL(10,3) DEFAULT NULL,   -- kcal por unidad_base
  proteinas_por_unidad DECIMAL(10,3) DEFAULT NULL,  -- gr por unidad_base
  grasas_por_unidad DECIMAL(10,3) DEFAULT NULL,     -- gr por unidad_base
  hidratos_por_unidad DECIMAL(10,3) DEFAULT NULL,   -- gr por unidad_base
  INDEX idx_ingredientes_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--------------------------------------------------------------------------------
-- TABLA: recetas
--------------------------------------------------------------------------------
-- Almacena las recetas (título, instrucciones, tiempo, porciones...)
-- Relaciones: categoria_id y creado_por -> usuarios.id 
-- foto_url puede almacenar ruta/URL a imagen, a ver como lo metemos...
--------------------------------------------------------------------------------

CREATE TABLE recetas (
  id INT AUTO_INCREMENT PRIMARY KEY,
  titulo VARCHAR(200) NOT NULL,
  descripcion_corta VARCHAR(500),
  instrucciones TEXT NOT NULL,
  tiempo_preparacion_min INT,
  porciones INT DEFAULT 2,
  foto_url VARCHAR(500),
  categoria_id INT DEFAULT NULL,
  creado_por INT DEFAULT NULL,
  fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (categoria_id) REFERENCES categorias(id) ON DELETE SET NULL ON UPDATE CASCADE,
  FOREIGN KEY (creado_por) REFERENCES usuarios(id) ON DELETE SET NULL ON UPDATE CASCADE,
  INDEX idx_recetas_titulo (titulo),
  INDEX idx_recetas_categoria (categoria_id),
  INDEX idx_recetas_creado_por (creado_por)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--------------------------------------------------------------------------------
-- TABLA: receta_ingrediente
--------------------------------------------------------------------------------
-- Esta es la que va con relación N:M entre recetas e ingredientes.
-- cantidad: valor numérico de la medida (ej. 200)
-- unidad: indica la unidad de esa cantidad (ej. 'gr', 'ml', 'taza')
-- Por tener todo más claro se puede crear una tabla 'unidades' erpo como hemos quitado otras no creo ¿no?

--------------------------------------------------------------------------------
CREATE TABLE receta_ingrediente (
  receta_id INT NOT NULL,
  ingrediente_id INT NOT NULL,
  cantidad DECIMAL(10,3) NOT NULL,
  unidad VARCHAR(50) NOT NULL,
  PRIMARY KEY (receta_id, ingrediente_id),
  FOREIGN KEY (receta_id) REFERENCES recetas(id) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (ingrediente_id) REFERENCES ingredientes(id) ON DELETE RESTRICT ON UPDATE CASCADE,
  INDEX idx_receta_ingrediente_ingrediente (ingrediente_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--------------------------------------------------------------------------------
-- TABLA: receta_valor_nutricional
--------------------------------------------------------------------------------
-- almacena los totales de nutrición para mejora de las calorias de la receta (si lo hacemos al final ya estaría)
-- por receta calcular (x ejemplo al crear/editar la receta recalcula y guarda).
-- Así evitamos evitar recalcular todo el rato
--------------------------------------------------------------------------------
CREATE TABLE receta_valor_nutricional (
  id INT AUTO_INCREMENT PRIMARY KEY,
  receta_id INT NOT NULL UNIQUE,  -- 1:1 con receta
  calorias_totales DECIMAL(12,3),
  proteinas_totales DECIMAL(12,3),
  grasas_totales DECIMAL(12,3),
  hidratos_totales DECIMAL(12,3),
  fecha_calculo DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (receta_id) REFERENCES recetas(id) ON DELETE CASCADE ON UPDATE CASCADE,
  INDEX idx_valor_receta (receta_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--------------------------------------------------------------------------------
-- TABLA: recetas_favoritas
--------------------------------------------------------------------------------
-- permite al usuario marcar recetas como favoritas, ideitas de Marco*****.
-- Luego se podría mostrar primero las favoritas para crear un plan.
--------------------------------------------------------------------------------
CREATE TABLE recetas_favoritas (
  id INT AUTO_INCREMENT PRIMARY KEY,
  usuario_id INT NOT NULL,
  receta_id INT NOT NULL,
  fecha_guardado DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (receta_id) REFERENCES recetas(id) ON DELETE CASCADE ON UPDATE CASCADE,
  UNIQUE KEY uq_usuario_receta (usuario_id, receta_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--------------------------------------------------------------------------------
-- TABLA: plan_semanal
--------------------------------------------------------------------------------
-- contiene los datos del plan (semana, nombre, de quien es...)
-- semana_inicio: fecha que representa el lunes de la semana definido por enum.
-- num_comensales y observaciones añadidos pos megamix de todas las bases que hemos planteado
--------------------------------------------------------------------------------
CREATE TABLE plan_semanal (
  id INT AUTO_INCREMENT PRIMARY KEY,
  usuario_id INT NOT NULL,
  nombre VARCHAR(150),
  semana_inicio DATE NOT NULL,
  num_comensales INT DEFAULT 1,         
  fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  observaciones TEXT,                   
  FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE ON UPDATE CASCADE,
  INDEX idx_plan_usuario (usuario_id),
  INDEX idx_plan_semana (semana_inicio)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--------------------------------------------------------------------------------
-- TABLA: plan_item
--------------------------------------------------------------------------------
--  Eliminación de 'turnos' y 'dias_semana'.
--  Se usan ENUMs para 'dia' y 'turno' que es mas simple y menos tablas.
-- UNIQUE (plan_id, dia, turno) para evitar duplicados.
-- cada fila representa una celda del plan (ej. Lunes - Comida -> receta_id X).
--------------------------------------------------------------------------------
CREATE TABLE plan_item (
  id INT AUTO_INCREMENT PRIMARY KEY,
  plan_id INT NOT NULL,
  receta_id INT NOT NULL,
  dia ENUM('Lunes','Martes','Miércoles','Jueves','Viernes','Sábado','Domingo') NOT NULL,
  turno ENUM('Comida','Cena') NOT NULL, -- Prnsamos solo dos turnos para no hacerlo mu largo
  notas VARCHAR(500),
  FOREIGN KEY (plan_id) REFERENCES plan_semanal(id) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (receta_id) REFERENCES recetas(id) ON DELETE RESTRICT ON UPDATE CASCADE,
  UNIQUE KEY uq_plan_dia_turno (plan_id, dia, turno), -- evita duplicados en el plan
  INDEX idx_plan_item_plan (plan_id),
  INDEX idx_plan_item_receta (receta_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--------------------------------------------------------------------------------
-- POPULATION:
--------------------------------------------------------------------------------

-- --- USUARIOS --- los comensales para todos 2
INSERT INTO usuarios (correo,nombre,apellido,contraseña,es_admin,descripcion,num_comensales_defecto) VALUES
('admin1@example.com','Admin1','Root','{noop}Admin1',1,'Administrador principal',2),
('admin2@example.com','Admin2','Root','{noop}Admin2',1,'Administrador secundario',2),
('admin3@example.com','Admin3','Root','{noop}Admin3',1,'Administrador auxiliar',2),
('patri@example.com','Patri','Humanes','{noop}Patri',0,'Usuaria activa de recetas saludables',2),
('marco@example.com','Marco','Casati','{noop}Marco',0,'Amante de la cocina italiana',2),
('ale@example.com','Ale','Muñoz','{noop}Ale',0,'Le gusta preparar comidas rápidas',2);

-- --- CATEGORIAS ---
INSERT INTO categorias (nombre,descripcion) VALUES
('Desayuno','Recetas para comenzar el día'),
('Ensaladas','Frescas y ligeras'),
('Italiana','Pasta, pizza y cositas'),
('Vegano','Sin productos animales'),
('Postres','Dulces y fáciles'),
('Sopas','Calentitas y reconfortantes'),
('Rápidas','Preparación < 30 min'),
('Cena ligera','Opciones para la noche'),
('Mediterránea','Sabores mediterráneos'),
('Mexicana','Toque picante y aromático');

-- --- INGREDIENTES ---
INSERT INTO ingredientes (nombre,unidad_base,calorias_por_unidad,proteinas_por_unidad,grasas_por_unidad,hidratos_por_unidad) VALUES
('Pollo pechuga','gr',1.65,0.31,0.04,0.0),
('Arroz blanco cocido','gr',1.30,0.026,0.002,0.28),
('Tomate','gr',0.18,0.009,0.002,0.039),
('Lechuga','gr',0.15,0.014,0.002,0.028),
('Aceite de oliva','ml',8.84,0.0,1.0,0.0),
('Sal','gr',0,0,0,0),
('Pimienta','gr',2.55,0.11,0.03,0.64),
('Pasta seca','gr',3.57,0.13,0.014,0.75),
('Queso parmesano','gr',4.31,0.36,0.28,0.03),
('Ajo','gr',1.49,0.06,0.005,0.33),
('Cebolla','gr',0.4,0.011,0.001,0.093),
('Leche','ml',0.64,0.033,0.033,0.05),
('Huevos','ud',72,6.0,5.0,0.36),
('Harina','gr',3.64,0.10,0.01,0.76),
('Azúcar','gr',3.87,0,0,1.0),
('Aguacate','gr',1.6,0.02,0.15,0.009),
('Limón','gr',0.29,0.01,0.0,0.09),
('Yogur natural','gr',0.59,0.05,0.03,0.04),
('Garbanzos cocidos','gr',1.64,0.086,0.027,0.27),
('Pimiento rojo','gr',0.31,0.01,0.003,0.06),
('Calabacín','gr',0.17,0.012,0.001,0.03),
('Pan integral','gr',2.5,0.09,0.03,0.49),
('Chocolate negro 70%','gr',5.46,0.06,0.46,0.46),
('Miel','gr',3.04,0.0,0.0,0.82),
('Albahaca fresca','gr',0.23,0.03,0.004,0.035),
('Perejil','gr',0.36,0.03,0.01,0.06),
('Mozzarella','gr',2.8,0.22,0.2,0.02);

-- --- RECETAS ---
INSERT INTO recetas (titulo,descripcion_corta,instrucciones,tiempo_preparacion_min,porciones,foto_url,categoria_id,creado_por,fecha_creacion) VALUES
('Ensalada de pollo y aguacate','Ensalada rápida con pollo asado y aguacate','1) Cocina la pechuga a la plancha con sal y pimienta.
2) Corta el tomate, lechuga y aguacate.
3) Mezcla todo y aliña con aceite y limón.
4) Sirve.',20,2,NULL,(SELECT id FROM categorias WHERE nombre='Ensaladas'),(SELECT id FROM usuarios WHERE correo='patri@example.com'),NOW()),
('Pasta al ajo y parmesano','Pasta sencilla con ajo y queso parmesano','1) Cuece la pasta según instrucciones del paquete.
2) Saltea ajo en aceite de oliva sin que se queme.
3) Añade la pasta escurrida y mezcla con parmesano rallado.
4) Emplata y añade pimienta.',25,2,NULL,(SELECT id FROM categorias WHERE nombre='Italiana'),(SELECT id FROM usuarios WHERE correo='marco@example.com'),NOW()),
('Garbanzos con verduras','Guiso rápido de garbanzos','1) Saltea cebolla, ajo y pimiento.
2) Añade calabacín y garbanzos cocidos.
3) Cocina 10 min con especias al gusto.
4) Sirve caliente.',30,2,NULL,(SELECT id FROM categorias WHERE nombre='Vegano'),(SELECT id FROM usuarios WHERE correo='ale@example.com'),NOW()),
('Tostada de aguacate y huevo','Tostada rápida para desayuno o cena','1) Tuesta el pan integral.
2) Aplasta el aguacate con limón, sal y pimienta.
3) Haz un huevo poche o frito.
4) Coloca sobre la tostada y sirve.',15,2,NULL,(SELECT id FROM categorias WHERE nombre='Rápidas'),(SELECT id FROM usuarios WHERE correo='patri@example.com'),NOW()),
('Sopa de tomate casera','Sopa fácil y reconfortante','1) Sofríe cebolla y ajo.
2) Añade tomate troceado y agua o caldo.
3) Cocina 15-20 min y tritura.
4) Ajusta sal y sirve.',35,2,NULL,(SELECT id FROM categorias WHERE nombre='Sopas'),(SELECT id FROM usuarios WHERE correo='patri@example.com'),NOW()),
('Bowl Mediterráneo con arroz','Bowl con arroz, verduras y pollo','1) Cocina arroz blanco.
2) Asa pollo y verduras al horno o plancha.
3) Monta el bowl con arroz, pollo, tomate y aliño con aceite y limón.',30,2,NULL,(SELECT id FROM categorias WHERE nombre='Mediterránea'),(SELECT id FROM usuarios WHERE correo='marco@example.com'),NOW()),
('Panqueques rápidos','Panqueques dulces para desayuno','1) Mezcla harina, leche y huevo hasta obtener una masa ligera.
2) Cocina porciones en sartén antiadherente.
3) Sirve con miel o fruta.',20,2,NULL,(SELECT id FROM categorias WHERE nombre='Desayuno'),(SELECT id FROM usuarios WHERE correo='ale@example.com'),NOW()),
('Ensalada de garbanzos y aguacate','Ensalada fresca y saciante','1) Mezcla garbanzos, tomate, pimiento y cebolla.
2) Añade aguacate en cubos y aliña con limón y aceite.',15,2,NULL,(SELECT id FROM categorias WHERE nombre='Ensaladas'),(SELECT id FROM usuarios WHERE correo='patri@example.com'),NOW()),
('Brownie fácil','Brownie de chocolate rápido','1) Mezcla harina, azúcar, huevos y chocolate derretido.
2) Hornea 20-25 min.
3) Deja enfriar y corta.',45,2,NULL,(SELECT id FROM categorias WHERE nombre='Postres'),(SELECT id FROM usuarios WHERE correo='marco@example.com'),NOW()),
('Ensalada caprese','Tomate, mozzarella y albahaca','1) Corta tomate y mozzarella.
2) Monta con albahaca y parmesano.
3) Aliña con aceite y sal.',10,2,NULL,(SELECT id FROM categorias WHERE nombre='Italiana'),(SELECT id FROM usuarios WHERE correo='marco@example.com'),NOW()),
('Tostadas de garbanzos estilo Mediterráneo','Tostadas con hummus rápido','1) Tritura garbanzos cocidos con ajo, limón y aceite para hacer una pasta ligera.
2) Unta sobre pan integral tostado.
3) Añade tomate y perejil por encima.',20,2,NULL,(SELECT id FROM categorias WHERE nombre='Rápidas'),(SELECT id FROM usuarios WHERE correo='ale@example.com'),NOW());

-- --- receta_ingrediente ---
-- Ensalada de pollo y aguacate
INSERT INTO receta_ingrediente (receta_id,ingrediente_id,cantidad,unidad) VALUES
((SELECT id FROM recetas WHERE titulo='Ensalada de pollo y aguacate'),(SELECT id FROM ingredientes WHERE nombre='Pollo pechuga'),200,'gr'),
((SELECT id FROM recetas WHERE titulo='Ensalada de pollo y aguacate'),(SELECT id FROM ingredientes WHERE nombre='Aguacate'),100,'gr'),
((SELECT id FROM recetas WHERE titulo='Ensalada de pollo y aguacate'),(SELECT id FROM ingredientes WHERE nombre='Lechuga'),100,'gr'),
((SELECT id FROM recetas WHERE titulo='Ensalada de pollo y aguacate'),(SELECT id FROM ingredientes WHERE nombre='Tomate'),80,'gr'),
((SELECT id FROM recetas WHERE titulo='Ensalada de pollo y aguacate'),(SELECT id FROM ingredientes WHERE nombre='Aceite de oliva'),15,'ml');

-- Pasta al ajo y parmesano
INSERT INTO receta_ingrediente (receta_id,ingrediente_id,cantidad,unidad) VALUES
((SELECT id FROM recetas WHERE titulo='Pasta al ajo y parmesano'),(SELECT id FROM ingredientes WHERE nombre='Pasta seca'),160,'gr'),
((SELECT id FROM recetas WHERE titulo='Pasta al ajo y parmesano'),(SELECT id FROM ingredientes WHERE nombre='Ajo'),6,'gr'),
((SELECT id FROM recetas WHERE titulo='Pasta al ajo y parmesano'),(SELECT id FROM ingredientes WHERE nombre='Aceite de oliva'),15,'ml'),
((SELECT id FROM recetas WHERE titulo='Pasta al ajo y parmesano'),(SELECT id FROM ingredientes WHERE nombre='Queso parmesano'),20,'gr');

-- Garbanzos con verduras
INSERT INTO receta_ingrediente (receta_id,ingrediente_id,cantidad,unidad) VALUES
((SELECT id FROM recetas WHERE titulo='Garbanzos con verduras'),(SELECT id FROM ingredientes WHERE nombre='Garbanzos cocidos'),200,'gr'),
((SELECT id FROM recetas WHERE titulo='Garbanzos con verduras'),(SELECT id FROM ingredientes WHERE nombre='Cebolla'),60,'gr'),
((SELECT id FROM recetas WHERE titulo='Garbanzos con verduras'),(SELECT id FROM ingredientes WHERE nombre='Pimiento rojo'),80,'gr'),
((SELECT id FROM recetas WHERE titulo='Garbanzos con verduras'),(SELECT id FROM ingredientes WHERE nombre='Calabacín'),100,'gr');

-- Tostada de aguacate y huevo
INSERT INTO receta_ingrediente (receta_id,ingrediente_id,cantidad,unidad) VALUES
((SELECT id FROM recetas WHERE titulo='Tostada de aguacate y huevo'),(SELECT id FROM ingredientes WHERE nombre='Pan integral'),80,'gr'),
((SELECT id FROM recetas WHERE titulo='Tostada de aguacate y huevo'),(SELECT id FROM ingredientes WHERE nombre='Aguacate'),80,'gr'),
((SELECT id FROM recetas WHERE titulo='Tostada de aguacate y huevo'),(SELECT id FROM ingredientes WHERE nombre='Huevos'),1,'ud');

-- Sopa de tomate casera
INSERT INTO receta_ingrediente (receta_id,ingrediente_id,cantidad,unidad) VALUES
((SELECT id FROM recetas WHERE titulo='Sopa de tomate casera'),(SELECT id FROM ingredientes WHERE nombre='Tomate'),600,'gr'),
((SELECT id FROM recetas WHERE titulo='Sopa de tomate casera'),(SELECT id FROM ingredientes WHERE nombre='Cebolla'),80,'gr'),
((SELECT id FROM recetas WHERE titulo='Sopa de tomate casera'),(SELECT id FROM ingredientes WHERE nombre='Ajo'),6,'gr');

-- Bowl Mediterráneo con arroz
INSERT INTO receta_ingrediente (receta_id,ingrediente_id,cantidad,unidad) VALUES
((SELECT id FROM recetas WHERE titulo='Bowl Mediterráneo con arroz'),(SELECT id FROM ingredientes WHERE nombre='Arroz blanco cocido'),180,'gr'),
((SELECT id FROM recetas WHERE titulo='Bowl Mediterráneo con arroz'),(SELECT id FROM ingredientes WHERE nombre='Pollo pechuga'),150,'gr'),
((SELECT id FROM recetas WHERE titulo='Bowl Mediterráneo con arroz'),(SELECT id FROM ingredientes WHERE nombre='Tomate'),80,'gr'),
((SELECT id FROM recetas WHERE titulo='Bowl Mediterráneo con arroz'),(SELECT id FROM ingredientes WHERE nombre='Aceite de oliva'),10,'ml');

-- Panqueques rápidos
INSERT INTO receta_ingrediente (receta_id,ingrediente_id,cantidad,unidad) VALUES
((SELECT id FROM recetas WHERE titulo='Panqueques rápidos'),(SELECT id FROM ingredientes WHERE nombre='Harina'),120,'gr'),
((SELECT id FROM recetas WHERE titulo='Panqueques rápidos'),(SELECT id FROM ingredientes WHERE nombre='Leche'),200,'ml'),
((SELECT id FROM recetas WHERE titulo='Panqueques rápidos'),(SELECT id FROM ingredientes WHERE nombre='Huevos'),1,'ud');

-- Ensalada de garbanzos y aguacate
INSERT INTO receta_ingrediente (receta_id,ingrediente_id,cantidad,unidad) VALUES
((SELECT id FROM recetas WHERE titulo='Ensalada de garbanzos y aguacate'),(SELECT id FROM ingredientes WHERE nombre='Garbanzos cocidos'),150,'gr'),
((SELECT id FROM recetas WHERE titulo='Ensalada de garbanzos y aguacate'),(SELECT id FROM ingredientes WHERE nombre='Tomate'),80,'gr'),
((SELECT id FROM recetas WHERE titulo='Ensalada de garbanzos y aguacate'),(SELECT id FROM ingredientes WHERE nombre='Aguacate'),70,'gr');

-- Brownie fácil
INSERT INTO receta_ingrediente (receta_id,ingrediente_id,cantidad,unidad) VALUES
((SELECT id FROM recetas WHERE titulo='Brownie fácil'),(SELECT id FROM ingredientes WHERE nombre='Harina'),100,'gr'),
((SELECT id FROM recetas WHERE titulo='Brownie fácil'),(SELECT id FROM ingredientes WHERE nombre='Azúcar'),120,'gr'),
((SELECT id FROM recetas WHERE titulo='Brownie fácil'),(SELECT id FROM ingredientes WHERE nombre='Huevos'),2,'ud'),
((SELECT id FROM recetas WHERE titulo='Brownie fácil'),(SELECT id FROM ingredientes WHERE nombre='Chocolate negro 70%'),120,'gr');

-- Ensalada caprese
INSERT INTO receta_ingrediente (receta_id,ingrediente_id,cantidad,unidad) VALUES
((SELECT id FROM recetas WHERE titulo='Ensalada caprese'),(SELECT id FROM ingredientes WHERE nombre='Tomate'),200,'gr'),
((SELECT id FROM recetas WHERE titulo='Ensalada caprese'),(SELECT id FROM ingredientes WHERE nombre='Mozzarella'),30,'gr'),
((SELECT id FROM recetas WHERE titulo='Ensalada caprese'),(SELECT id FROM ingredientes WHERE nombre='Albahaca fresca'),10,'gr'),
((SELECT id FROM recetas WHERE titulo='Ensalada caprese'),(SELECT id FROM ingredientes WHERE nombre='Aceite de oliva'),10,'ml');

-- Tostadas de garbanzos estilo Mediterráneo
INSERT INTO receta_ingrediente (receta_id,ingrediente_id,cantidad,unidad) VALUES
((SELECT id FROM recetas WHERE titulo='Tostadas de garbanzos estilo Mediterráneo'),(SELECT id FROM ingredientes WHERE nombre='Garbanzos cocidos'),200,'gr'),
((SELECT id FROM recetas WHERE titulo='Tostadas de garbanzos estilo Mediterráneo'),(SELECT id FROM ingredientes WHERE nombre='Ajo'),5,'gr'),
((SELECT id FROM recetas WHERE titulo='Tostadas de garbanzos estilo Mediterráneo'),(SELECT id FROM ingredientes WHERE nombre='Limón'),10,'gr'),
((SELECT id FROM recetas WHERE titulo='Tostadas de garbanzos estilo Mediterráneo'),(SELECT id FROM ingredientes WHERE nombre='Pan integral'),120,'gr');

-- --- receta_valor_nutricional (valores aproximados para todas las recetas, ahí va¡¡¡ con imaginación) ---
INSERT INTO receta_valor_nutricional (receta_id,calorias_totales,proteinas_totales,grasas_totales,hidratos_totales,fecha_calculo) VALUES
((SELECT id FROM recetas WHERE titulo='Ensalada de pollo y aguacate'), 652.00, 66.12, 38.36, 6.82, NOW()),
((SELECT id FROM recetas WHERE titulo='Pasta al ajo y parmesano'), 798.94, 28.36, 22.87, 122.58, NOW()),
((SELECT id FROM recetas WHERE titulo='Garbanzos con verduras'), 393.80, 19.86, 5.80, 67.38, NOW()),
((SELECT id FROM recetas WHERE titulo='Tostada de aguacate y huevo'), 400.00, 14.80, 19.40, 40.28, NOW()),
((SELECT id FROM recetas WHERE titulo='Sopa de tomate casera'), 444.00, 6.66, 2.74, 40.50, NOW()),
((SELECT id FROM recetas WHERE titulo='Bowl Mediterráneo con arroz'), 592.00, 52.31, 16.52, 53.52, NOW()),
((SELECT id FROM recetas WHERE titulo='Panqueques rápidos'), 636.80, 24.60, 12.80, 101.56, NOW()),
((SELECT id FROM recetas WHERE titulo='Ensalada de garbanzos y aguacate'), 372.40, 15.02, 14.71, 44.25, NOW()),
((SELECT id FROM recetas WHERE titulo='Brownie fácil'), 1627.60, 29.20, 66.20, 251.92, NOW()),
((SELECT id FROM recetas WHERE titulo='Ensalada caprese'), 210.70, 8.70, 16.44, 8.75, NOW()),
((SELECT id FROM recetas WHERE titulo='Tostadas de garbanzos estilo Mediterráneo'), 638.35, 28.40, 9.03, 115.35, NOW());

-- --- recetas_favoritas (ejemplos) ---
INSERT INTO recetas_favoritas (usuario_id,receta_id,fecha_guardado) VALUES
((SELECT id FROM usuarios WHERE correo='patri@example.com'),(SELECT id FROM recetas WHERE titulo='Pasta al ajo y parmesano'),NOW()),
((SELECT id FROM usuarios WHERE correo='marco@example.com'),(SELECT id FROM recetas WHERE titulo='Ensalada de pollo y aguacate'),NOW()),
((SELECT id FROM usuarios WHERE correo='ale@example.com'),(SELECT id FROM recetas WHERE titulo='Garbanzos con verduras'),NOW());

-- --- plan_semanal y plan_item (plan semanal completo: Lunes-Domingo comida y cena para Patri) ---
INSERT INTO plan_semanal (usuario_id,nombre,semana_inicio,num_comensales,observaciones,fecha_creacion) VALUES
((SELECT id FROM usuarios WHERE correo='patri@example.com'),'Semana completa',(DATE_SUB(CURDATE(), INTERVAL (WEEKDAY(CURDATE())) DAY)),2,'Plan ejemplo completo (Lun-Dom, comida y cena)',NOW());

INSERT INTO plan_item (plan_id,receta_id,dia,turno,notas) VALUES
((SELECT id FROM plan_semanal WHERE usuario_id=(SELECT id FROM usuarios WHERE correo='patri@example.com') LIMIT 1),(SELECT id FROM recetas WHERE titulo='Ensalada de pollo y aguacate'),'Lunes','Comida',''),
((SELECT id FROM plan_semanal WHERE usuario_id=(SELECT id FROM usuarios WHERE correo='patri@example.com') LIMIT 1),(SELECT id FROM recetas WHERE titulo='Pasta al ajo y parmesano'),'Lunes','Cena',''),
((SELECT id FROM plan_semanal WHERE usuario_id=(SELECT id FROM usuarios WHERE correo='patri@example.com') LIMIT 1),(SELECT id FROM recetas WHERE titulo='Garbanzos con verduras'),'Martes','Comida',''),
((SELECT id FROM plan_semanal WHERE usuario_id=(SELECT id FROM usuarios WHERE correo='patri@example.com') LIMIT 1),(SELECT id FROM recetas WHERE titulo='Tostada de aguacate y huevo'),'Martes','Cena',''),
((SELECT id FROM plan_semanal WHERE usuario_id=(SELECT id FROM usuarios WHERE correo='patri@example.com') LIMIT 1),(SELECT id FROM recetas WHERE titulo='Sopa de tomate casera'),'Miércoles','Comida',''),
((SELECT id FROM plan_semanal WHERE usuario_id=(SELECT id FROM usuarios WHERE correo='patri@example.com') LIMIT 1),(SELECT id FROM recetas WHERE titulo='Bowl Mediterráneo con arroz'),'Miércoles','Cena',''),
((SELECT id FROM plan_semanal WHERE usuario_id=(SELECT id FROM usuarios WHERE correo='patri@example.com') LIMIT 1),(SELECT id FROM recetas WHERE titulo='Panqueques rápidos'),'Jueves','Comida',''),
((SELECT id FROM plan_semanal WHERE usuario_id=(SELECT id FROM usuarios WHERE correo='patri@example.com') LIMIT 1),(SELECT id FROM recetas WHERE titulo='Ensalada de garbanzos y aguacate'),'Jueves','Cena',''),
((SELECT id FROM plan_semanal WHERE usuario_id=(SELECT id FROM usuarios WHERE correo='patri@example.com') LIMIT 1),(SELECT id FROM recetas WHERE titulo='Brownie fácil'),'Viernes','Comida','(postre para compartir)'), 
((SELECT id FROM plan_semanal WHERE usuario_id=(SELECT id FROM usuarios WHERE correo='patri@example.com') LIMIT 1),(SELECT id FROM recetas WHERE titulo='Ensalada caprese'),'Viernes','Cena',''),
((SELECT id FROM plan_semanal WHERE usuario_id=(SELECT id FROM usuarios WHERE correo='patri@example.com') LIMIT 1),(SELECT id FROM recetas WHERE titulo='Tostadas de garbanzos estilo Mediterráneo'),'Sábado','Comida',''),
((SELECT id FROM plan_semanal WHERE usuario_id=(SELECT id FROM usuarios WHERE correo='patri@example.com') LIMIT 1),(SELECT id FROM recetas WHERE titulo='Pasta al ajo y parmesano'),'Sábado','Cena',''),
((SELECT id FROM plan_semanal WHERE usuario_id=(SELECT id FROM usuarios WHERE correo='patri@example.com') LIMIT 1),(SELECT id FROM recetas WHERE titulo='Ensalada de pollo y aguacate'),'Domingo','Comida',''),
((SELECT id FROM plan_semanal WHERE usuario_id=(SELECT id FROM usuarios WHERE correo='patri@example.com') LIMIT 1),(SELECT id FROM recetas WHERE titulo='Sopa de tomate casera'),'Domingo','Cena','');