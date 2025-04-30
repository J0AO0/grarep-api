package com.grarep.grarep.validation.usuario;

import com.grarep.grarep.domain.Usuario;
import com.grarep.grarep.domain.dto.UsuarioDTO;
import com.grarep.grarep.repository.UsuarioRepository;
import com.grarep.grarep.resource.exception.FieldMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class UsuarioUpdateValidator implements ConstraintValidator<UsuarioUpdate, UsuarioDTO> {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UsuarioRepository repo;

    @Override
    public void initialize(UsuarioUpdate ann) {
    }

    @Override
    public boolean isValid(UsuarioDTO objDto, ConstraintValidatorContext context) {

        List<FieldMessage> list = new ArrayList<>();
        @SuppressWarnings("unchecked")
        Map<String,String>map = (Map<String,String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Integer uriId = Integer.parseInt(map.get("id"));

        Usuario aux = repo.findPorEmail(objDto.getEmail());
        if(aux !=null && !aux.getId().equals(uriId)) {
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
