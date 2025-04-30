package com.grarep.grarep.resource;

import com.fasterxml.jackson.annotation.JsonView;
import com.grarep.grarep.domain.Usuario;
import com.grarep.grarep.domain.dto.UsuarioDTO;
import com.grarep.grarep.domain.dto.flat.UsuarioFlat;
import com.grarep.grarep.domain.dto.viewretorno.UsuarioView;
import com.grarep.grarep.repository.UsuarioRepository;
import com.grarep.grarep.repository.filter.UsuarioFilter;
import com.grarep.grarep.security.resource.CheckSecurity;
import com.grarep.grarep.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping(value = "/usuarios")
public class UsuarioResource {

    @Autowired
    private UsuarioService service;

    @Autowired
    private UsuarioRepository usuRepo;

    @CheckSecurity.Usuario.PodeConsultar
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> find(@PathVariable Integer id) {
        UsuarioDTO obj = service.findF(id);
        return ResponseEntity.ok().body(obj);
    }
    @CheckSecurity.Usuario.PodeConsultar
    @JsonView(UsuarioView.Resumo1.class)
    @RequestMapping(value = "/{id}/senha", method = RequestMethod.GET)
    public ResponseEntity<?> findSenha(@PathVariable Integer id) {
        Usuario obj = service.find(id);
        return ResponseEntity.ok().body(obj);
    }
    //    @CheckSecurity.Usuario.PodeConsultar
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ResponseEntity<List<UsuarioFlat>> findAll() {
        List<UsuarioFlat> list = service.findAllSQL();
        return ResponseEntity.ok().body(list);
    }
    @CheckSecurity.Usuario.PodeConsultar
    @RequestMapping(value = "/inativos", method = RequestMethod.GET)
    public ResponseEntity<List<UsuarioFlat>> findAllInativo() {
        //	List<UsuarioDTO> list = service.findAll();
        List<UsuarioFlat> list = service.findAllSqlInativo();
        return ResponseEntity.ok().body(list);
    }
    @CheckSecurity.Usuario.PodeConsultar
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public ResponseEntity<Page<Usuario>> findPage(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                  @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
                                                  @RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
                                                  @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        Page<Usuario> list = service.findPage(page, linesPerPage, orderBy, direction);
        return ResponseEntity.ok().body(list);
    }
    @CheckSecurity.Usuario.PodeExcluir
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
    @CheckSecurity.Usuario.PodeCadastrar
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> insert(@RequestBody UsuarioFlat obj) {
        UsuarioFlat objNovo = service.insert(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().
                path("/{id}").buildAndExpand(objNovo.getId()).toUri();
        return ResponseEntity.created(uri).body(objNovo);

    }



    @CheckSecurity.Usuario.PodeAtualizar
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestBody UsuarioFlat obj, @PathVariable Integer id) {
        obj.setId(id);
        UsuarioFlat atividadeAtualizado = service.from(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().
                path("/{id}").buildAndExpand(atividadeAtualizado.getId()).toUri();
        return ResponseEntity.created(uri).body(atividadeAtualizado);

    }
    @CheckSecurity.Usuario.PodeAtualizar
    @RequestMapping(value = "/{id}/senha", method = RequestMethod.PUT)
    public ResponseEntity<?> updateSenha(@RequestBody UsuarioFlat obj, @PathVariable Integer id) {
        obj.setId(id);
        UsuarioFlat atividadeAtualizado = service.fromSenha(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().
                path("/{id}").buildAndExpand(atividadeAtualizado.getId()).toUri();
        return ResponseEntity.created(uri).body(atividadeAtualizado);

    }


    @RequestMapping(value = "/alterarsenha", method = RequestMethod.PUT)
    public ResponseEntity<?> alterarsenha(@RequestBody String obj) {
        Usuario usu = service.fromSenha(obj);
        usu.setPermissoes(null);
        return ResponseEntity.ok().body(usu);


    }
//	@CheckSecurity.Usuario.PodeConsultar
//	@RequestMapping(value = "/sql", method = RequestMethod.GET)
//	public ResponseEntity<List<Usuario>> findAllSql() {
//		List<Usuario> list = service.findAllSQL();
//		return ResponseEntity.ok().body(list);
//	}


    @CheckSecurity.Usuario.PodeAlterarStatus
    @RequestMapping(value="/{id}/status",method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void inativar(@RequestBody Boolean obj,@PathVariable int id)	{
        service.status(obj,id);


    }

    @RequestMapping(value="/tenant/{idempresa}",method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setTenantAtivo(@PathVariable int idempresa)	{
        service.tenantAtivo(idempresa);


    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<UsuarioFlat> findAllPag(UsuarioFilter pacienteFilter, Pageable pageable) {
        Page<Usuario> cats = usuRepo.filtrar(pacienteFilter, pageable);
        Page<UsuarioFlat> catsflat = service.mudarUsuarioParaFlat(cats);
        return catsflat;
    }


}