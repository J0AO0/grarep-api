CREATE TABLE produto_pedido (
    produto_id INTEGER NOT NULL,
    pedido_id INTEGER NOT NULL,
    quantidade INTEGER NOT NULL,
    preco_unitario DECIMAL(19,2) NOT NULL,
    PRIMARY KEY (produto_id, pedido_id),
    CONSTRAINT fk_produto FOREIGN KEY (produto_id) REFERENCES produto (id),
    CONSTRAINT fk_pedido FOREIGN KEY (pedido_id) REFERENCES pedido (id)
) ENGINE=InnoDB;
