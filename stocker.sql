CREATE TABLE business (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    ticker VARCHAR(255),
    sector VARCHAR(255),
    industry VARCHAR(255),
    description TEXT,
    address VARCHAR(255),
    created_at DATETIME,
    updated_at DATETIME
);

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255),
    business_id BIGINT,
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (business_id) REFERENCES business(id)
);


-- Negocios
INSERT INTO business (id, name, ticker, sector, industry, description, address, created_at, updated_at) VALUES
(1, 'CESDE Tech', 'CES', 'Educación', 'Instituto', 'Institución educativa tecnológica', 'Calle 50 #40-20', NOW(), NOW()),
(2, 'CodeSoft', 'CSFT', 'Software', 'Desarrollo', 'Empresa de desarrollo de software', 'Calle 80 #70-10', NOW(), NOW());

-- Usuarios
INSERT INTO users (id, name, email, password, role, business_id, created_at, updated_at) VALUES
(1, 'Martín', 'martin@cesde.edu.co', '12345678', 'ADMIN', 1, NOW(), NOW()),
(2, 'Ana', 'ana@cesde.edu.co', '12345678', 'USER', 1, NOW(), NOW()),
(3, 'Carlos', 'carlos@codesoft.com', '12345678', 'USER', 2, NOW(), NOW()),
(4, 'Laura', 'laura@codesoft.com', '12345678', 'ADMIN', 2, NOW(), NOW());


-- Campo calculado
SELECT name, role,
       LENGTH(email) * 10 AS peso_virtual
FROM users;

-- Campo con condicional
SELECT name, role,
       CASE
           WHEN role = 'ADMIN' THEN 'Administrador del sistema'
           ELSE 'Usuario normal'
       END AS tipo_usuario
FROM users;


-- 2 Filtros
SELECT * FROM users
WHERE role = 'USER' AND business_id = 1;


-- Inner Join 2 de tablas
SELECT u.name AS usuario, b.name AS empresa
FROM users u
INNER JOIN business b ON u.business_id = b.id;


-- Función agregada
SELECT business_id, COUNT(*) AS cantidad_usuarios
FROM users
GROUP BY business_id;


-- Subconsulta
SELECT * FROM users
WHERE business_id = (
    SELECT id FROM business WHERE name = 'CESDE Tech'
);

-- Procedimiento almacenado
DELIMITER //

CREATE PROCEDURE actualizar_email_usuario(
    IN id_usuario BIGINT,
    IN nuevo_email VARCHAR(255)
)
BEGIN
    UPDATE users SET email = nuevo_email WHERE id = id_usuario;
END //

DELIMITER ;

CALL actualizar_email_usuario(2, 'nuevo@correo.com');
