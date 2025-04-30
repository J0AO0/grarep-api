CREATE TABLE estoque (
    nf INT NOT NULL,
    produto INT NOT NULL,
    quantidade DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (nf, produto)  -- Garantindo que a combinação seja única
);
