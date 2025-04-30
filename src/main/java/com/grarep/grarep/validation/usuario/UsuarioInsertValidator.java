package com.grarep.grarep.validation.usuario;

import com.grarep.grarep.domain.Usuario;
import com.grarep.grarep.domain.dto.UsuarioNewDTO;
import com.grarep.grarep.repository.UsuarioRepository;
import com.grarep.grarep.resource.exception.FieldMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;





public class UsuarioInsertValidator implements ConstraintValidator<UsuarioInsert, UsuarioNewDTO> {
    @Autowired
    private UsuarioRepository repo;

    @Override
    public void initialize(UsuarioInsert ann) {
    }

    @Override
    public boolean isValid(UsuarioNewDTO objDto, ConstraintValidatorContext context) {
        List<FieldMessage> list = new ArrayList<>();

        Usuario aux1 = repo.findPorEmail(objDto.getEmail());
        if(aux1 !=null) {
            list.add(new FieldMessage("email"," Email já existente"));
        }

        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMensagem()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return list.isEmpty();

    }
}
