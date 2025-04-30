INSERT INTO tenant
VALUES (1, 'GASOT E MARQUES SERVICO DE RADIOLOGIA LTDA');

INSERT INTO empresa (id, cidade, razaosocial, cpfoucnpj, naturezapessoa, uf, cep, logradouro, numero, bairro, whats,
                     tenant_id, status, editar)
VALUES (1, 'ARAPONGAS', 'GASOT E MARQUES SERVICO DE RADIOLOGIA LTDA', '35502138000173',
        'JURIDICA', 'PR', '86703530', 'PEPIRA DE CRISTA AMARELA', '81',
        'VILA COELHO', '(43)9841-18809', 1, 1, 1);

INSERT INTO usuario (id, email, gtenantativo, login, nome, senha, status, telefone, tenantativo, tenant_id)
VALUES (1, 'admin@admin.com', NULL, 'RAIZ', NULL,
        '$2a$10$ym4FHvnOCErbHb49jq8FzekqTSYTAsqhDX4ntJ00XC0h8Iganq0Nm', 1, '1', 1, 1);

INSERT INTO usuario (id, email, gtenantativo, login, nome, senha, status, telefone, tenantativo) VALUE (
  3, 'controle@gmail.com', NULL, 'Controle', 'Controle', 1, 1, 0, NULL
);

INSERT INTO usuario_empresa (id_usuario, id_empresa, empresapadrao, tenant_id)
VALUES (1, 1, 1, 1);

INSERT INTO classepermissao (id, nome)
VALUES (5, 'Usuario'),
       (7, 'Empresa'),
       (8, 'Pedido'),
       (9, 'Produto');

INSERT INTO permissao (id, descricao, classepermissao_id)
VALUES (21, 'C_USU', 5),
       (22, 'U_USU', 5),
       (23, 'D_USU', 5),
       (24, 'R_USU', 5),
       (25, 'S_USU', 5),
       (32, 'C_EMP', 7),
       (33, 'U_EMP', 7),
       (34, 'D_EMP', 7),
       (35, 'R_EMP', 7),
       (36, 'S_EMP', 7),
       (6, 'C_PED', 8),
       (7, 'U_PED', 8),
       (8, 'D_PED', 8),
       (9, 'R_PED', 8),
       (10, 'S_PED', 8),
       (11, 'C_PROD', 9),
       (12, 'U_PROD', 9),
       (13, 'D_PROD', 9),
       (14, 'R_PROD', 9),
       (15, 'S_PROD', 9);

INSERT INTO usuario_permissao (id_usuario, id_permissao)
VALUES (1, 21),
       (1, 22),
       (1, 23),
       (1, 24),
       (1, 25),
       (1, 32),
       (1, 33),
       (1, 34),
       (1, 35),
       (1, 36),
       (1, 6),
       (1, 7),
       (1, 8),
       (1, 9),
       (1, 10),
       (1, 11),
       (1, 12),
       (1, 13),
       (1, 14),
       (1, 15);

ALTER TABLE log_sistema MODIFY comando VARCHAR (10000);
