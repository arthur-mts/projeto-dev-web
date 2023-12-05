-- Tabela de Apostadores
CREATE TABLE apostadores
(
    id              varchar(36) primary key,
    nome            VARCHAR(50)         NOT NULL,
    email           VARCHAR(100) UNIQUE NOT NULL,
    data_nascimento DATE
);

-- Tabela de Apostas
CREATE TABLE apostas
(
    id              varchar(36) primary key,
    id_apostador    varchar(36),
    numero_apostado INT,
    valor           DECIMAL(10, 2),
    data_aposta     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_apostador) REFERENCES apostadores (id)
);

-- Tabela de Resultados
CREATE TABLE resultados
(
    id              varchar(36) primary key,
    numero_sorteado INT,
    data_sorteio    DATE
);