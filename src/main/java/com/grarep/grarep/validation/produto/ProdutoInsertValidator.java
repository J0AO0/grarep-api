package com.grarep.grarep.validation.produto;

import com.grarep.grarep.domain.Produto;
import com.grarep.grarep.domain.dto.ProdutoNewDTO;
import com.grarep.grarep.repository.ProdutoRepository;
import com.grarep.grarep.resource.exception.FieldMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProdutoInsertValidator implements ConstraintValidator<ProdutoInsert, ProdutoNewDTO> {

    @Autowired
    private ProdutoRepository repo;

    @Override
    public void initialize(ProdutoInsert ann) {

    }

    @Override
    public boolean isValid(ProdutoNewDTO value, ConstraintValidatorContext context) {
        List<FieldMessage> list = new ArrayList<>();

        // Verifica se já existe um produto com o mesmo SKU
        Produto produtoBySku = repo.findBySku(value.getSku());
        if (produtoBySku != null) {
            list.add(new FieldMessage("sku", "SKU já cadastrado!"));
        }

        // Verifica se já existe um produto com o mesmo nome
//        Produto cat = repo.findByNome(value.getNome());
//        if (cat != null) {
//            list.add(new FieldMessage("nome", "Produto já existente!"));
//        }

        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMensagem())
                    .addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }
}
