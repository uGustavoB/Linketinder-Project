-- Tabela de candidatos
CREATE TABLE candidatos (
                            id             SERIAL PRIMARY KEY,
                            nome           VARCHAR(100) NOT NULL,
                            sobrenome      VARCHAR(100) NOT NULL,
                            data_nascimento DATE,
                            email          VARCHAR(255) UNIQUE NOT NULL,
                            cpf            VARCHAR(14) UNIQUE NOT NULL,
                            pais           VARCHAR(100),
                            estado         VARCHAR(100),
                            cep            VARCHAR(10),
                            descricao      TEXT,
                            senha          VARCHAR(255) NOT NULL CHECK (char_length(senha) >= 6),
                            criado_em      TIMESTAMP NOT NULL DEFAULT now()
);

-- Tabela de empresas
CREATE TABLE empresas (
                          id             SERIAL PRIMARY KEY,
                          nome           VARCHAR(150) NOT NULL,
                          cnpj           VARCHAR(18) UNIQUE NOT NULL,
                          email          VARCHAR(255) UNIQUE NOT NULL,
                          descricao      TEXT,
                          pais           VARCHAR(100),
                          cep            VARCHAR(10),
                          senha          VARCHAR(255) NOT NULL CHECK (char_length(senha) >= 6),
                          criado_em      TIMESTAMP NOT NULL DEFAULT now()
);

-- Competências
CREATE TABLE competencias (
                              id             SERIAL PRIMARY KEY,
                              nome           VARCHAR(100) UNIQUE NOT NULL
);

-- Canidato_competencia (N:N entre candidatos e competências)
CREATE TABLE candidato_competencia (
                                       candidato_id   INTEGER NOT NULL REFERENCES candidatos(id) ON DELETE CASCADE,
                                       competencia_id INTEGER NOT NULL REFERENCES competencias(id) ON DELETE CASCADE,
                                       PRIMARY KEY (candidato_id, competencia_id)
);

-- Empresa_competencia (N:N entre empresas e competências)
CREATE TABLE empresa_competencia (
                                       empresa_id   INTEGER NOT NULL REFERENCES empresas(id) ON DELETE CASCADE,
                                       competencia_id INTEGER NOT NULL REFERENCES competencias(id) ON DELETE CASCADE,
                                       PRIMARY KEY (empresa_id, competencia_id)
);

-- Vagas
CREATE TABLE vagas (
                       id             SERIAL PRIMARY KEY,
                       empresa_id     INTEGER NOT NULL REFERENCES empresas(id) ON DELETE CASCADE,
                       nome           VARCHAR(150) NOT NULL,
                       descricao      TEXT,
                       estado         VARCHAR(100),
                       cidade         VARCHAR(100),
                       criado_em      TIMESTAMP NOT NULL DEFAULT now()
);

-- Vaga_competencia (N:N entre vagas e competências)
CREATE TABLE vaga_competencia (
                                  vaga_id        INTEGER NOT NULL REFERENCES vagas(id) ON DELETE CASCADE,
                                  competencia_id INTEGER NOT NULL REFERENCES competencias(id) ON DELETE CASCADE,
                                  PRIMARY KEY (vaga_id, competencia_id)
);

-- Curtidas
CREATE TABLE curtidas (
                          candidato_id        INTEGER NOT NULL REFERENCES candidatos(id) ON DELETE CASCADE,
                          vaga_id             INTEGER NOT NULL REFERENCES vagas(id) ON DELETE CASCADE,
                          candidato_curtiu_em TIMESTAMP,
                          empresa_curtiu_em   TIMESTAMP,
                          PRIMARY KEY (candidato_id, vaga_id)
);

-- Índices auxiliares
CREATE INDEX idx_vagas_empresa_id ON vagas(empresa_id);
CREATE INDEX idx_candidato_competencia_competencia_id ON candidato_competencia(competencia_id);
CREATE INDEX idx_vaga_competencia_competencia_id ON vaga_competencia(competencia_id);
CREATE INDEX idx_curtidas_vaga_id ON curtidas(vaga_id);CREATE INDEX idx_empresa_competencia_competencia_id ON empresa_competencia(competencia_id);
