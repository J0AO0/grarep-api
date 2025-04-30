package com.grarep.grarep.validation.produto;

import com.grarep.grarep.domain.Produto;
import com.grarep.grarep.domain.dto.ProdutoDTO;
import com.grarep.grarep.repository.ProdutoRepository;
import com.grarep.grarep.resource.exception.FieldMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class ProdutoUpdateValidator implements ConstraintValidator<ProdutoUpdate, ProdutoDTO> {
    @Autowired
    private ProdutoRepository repo;
    @Override
    public void initialize(ProdutoUpdate ann){

    }

    @Override
    public boolean isValid(ProdutoDTO value, ConstraintValidatorContext context) {
        List<FieldMessage> list = new ArrayList<>();

        // Verifica se já existe um produto com o mesmo SKU
        Produto produtoBySku = repo.findBySku(value.getSku());
        if (produtoBySku != null) {
            list.add(new FieldMessage("sku", "SKU já cadastrado!"));
        }

//        Produto cat = repo.findByNome(value.getNome());
//        if (cat != null) {
//            list.add(new FieldMessage("name", " Produto já existente "));
//        }

        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMensagem()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }
}
