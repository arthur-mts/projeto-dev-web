create table concurso
(
    id           varchar(36) primary key,
    nome         varchar(50) not null,
    data_sorteio DATE not null,
    numero_sorteado INT
);

drop table apostas;

CREATE TABLE apostas
(
    id              varchar(36) primary key,
    id_apostador    varchar(36),
    numero_apostado INT,
    valor           DECIMAL(10, 2),
    data_aposta     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_concurso varchar(36),
    FOREIGN KEY (id_apostador) REFERENCES apostadores (id),
    FOREIGN KEY (id_concurso) REFERENCES concurso (id)
);