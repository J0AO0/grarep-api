package com.grarep.grarep.service;

import com.grarep.grarep.domain.*;
import com.grarep.grarep.domain.dto.EmpresaRetornUsuario;
import com.grarep.grarep.domain.dto.EmpresaUsu;
import com.grarep.grarep.domain.dto.UsuarioDTO;
import com.grarep.grarep.domain.dto.flat.UsuarioFlat;
import com.grarep.grarep.repository.*;
import com.grarep.grarep.security.MEISecurity;
import com.grarep.grarep.service.exception.DataIntegrityException;
import com.grarep.grarep.service.exception.EntidadeNaoEncontradaExcepition;
import com.grarep.grarep.service.exception.ObjectNotFoundException;
import com.grarep.grarep.service.util.Tenantuser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;




@Service
public class UsuarioService {
    @Autowired
    private LogSistemaService log;
    @Autowired
    private UsuarioEmpresaRepository repoUsuarioEmpresa;
    @Autowired
    private EmpresaRepository repoempresa;
    @Autowired
    private LogSistemaRepository repolog;
    @Autowired
    private PasswordEncoder pe;
    @Autowired
    private PermissaoRepository repoPermissao;
    @Autowired
    private Tenantuser tenantUsuario;
    @Autowired
    private TenantRepository repoTenant;
    @Autowired
    private MEISecurity usutoken;

    @Autowired
    private UsuarioRepository repo;

    public Usuario find(Integer id) {
        Optional<Usuario> obj = repo.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado! Id: " + id + ", Tipo: " + Usuario.class.getName()));
    }

    public UsuarioDTO findF(Integer id) {
        System.out.println("Usuairo do token");
        System.out.println(usutoken.getUsu().getNome());
        Usuario usu = repo.findPorId(id);
        usu.setSenha(null);
        List<Empresa> empresas = repoempresa.findAllAtivas();
        Empresa emp1 = repoempresa.findEmpPorTenantAtivo(usu.getTenantativo());


        UsuarioDTO usuDto = new UsuarioDTO(usu);
        System.out.println(emp1 + " Empresa");
        usuDto.setEmpresaativa(emp1.getRazaosocial());
        usuDto.setIdEmpresaativa(emp1.getId());
        if (usu.getTenantativo() == 1) {
            Tenant t  = tenantUsuario.buscarOuFalhar();
            usuDto.setEmpresaativa(t.getDescricao());
            usuDto.setIdEmpresaativa(t.getId());
        }
        List<EmpresaRetornUsuario> empR = new ArrayList<EmpresaRetornUsuario>();
        usuDto.setPermissoes(usu.getPermissoes());
        for(Empresa emp:empresas) {
            EmpresaRetornUsuario empRetorno = new EmpresaRetornUsuario(emp);
            Boolean empPadrao = false;
            Boolean empUsuario = false;
            Integer empP = repoUsuarioEmpresa.verificaEmpPadrao(emp.getId(), id);
            if(empP == null) {
                empPadrao = false;
                empUsuario = false;

            }  else {
                if(empP == 0) {
                    empPadrao = false;
                    empUsuario = true;
                }else {
                    empPadrao = true;
                    empUsuario = true;

                }

            }




            empRetorno.setEmpresaPadrao(empPadrao);
            empRetorno.setEmpresasUsuario(empUsuario);

            empR.add(empRetorno);
        }
//		List<Empresa> empresasOutras = repoempresa.findAllSqlEmpsNotInGtenantComUsuario(id);
//		for(Empresa emp: empresasOutras ) {
//			EmpresaRetornUsuario empRetornoOutras = new EmpresaRetornUsuario(emp);
//			Boolean empPadrao = false;
//			empRetornoOutras.setEmpresaPadrao(empPadrao);
//			empRetornoOutras.setEmpresasUsuario(empPadrao);
//			empR.add(empRetornoOutras);
//
//		}

        usuDto.setEmpresas(empR);
        return usuDto;
    }

    //    @Transactional
    public void delete(Integer id) {
        find(id);
        try {
            repo.deleteById(id);
            Usuario usu = new Usuario();
            usu.setId(id);
            logUsuario(usu, "excluir");

        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir Usuario");
        }
    }

    @Transactional
    public UsuarioFlat insert(UsuarioFlat obj) {

        Usuario usuAtual = new Usuario();

        usuAtual.setId(null);
        Usuario atualizado = new Usuario(usuAtual, obj, tenantUsuario.buscarOuFalhar());
        atualizado.setStatus(true);
        atualizado.setTenant(tenantUsuario.buscarOuFalhar());
        atualizado.setTenantativo(tenantUsuario.buscarOuFalhar().getId());
        atualizado.setGtenantativo(1);
        atualizado.setSenha(pe.encode(obj.getSenha()));
        repoPermissao.saveAll(atualizado.getPermissoes());
        repo.save(atualizado);
        inserirEmpUsu(obj, atualizado);
        logUsuario(atualizado, "insert");
        return obj;
    }
    private void inserirEmpUsu(UsuarioFlat obj, Usuario atualizado) {


        for (EmpresaUsu emp : obj.getEmpresas()) {
            if(emp.getEmpresasusuario()==true) {
                UsuarioEmpresa usuemp = new UsuarioEmpresa();
                UsuarioEmpresaPK chave = new UsuarioEmpresaPK();
                chave.setUsuario(atualizado);
                Empresa empresa = repoempresa.findPorId(emp.getId());
                chave.setEmrpesa(empresa);
                usuemp.setId(chave);
                usuemp.setTenantId(empresa.getTenant().getId());
                usuemp.setEmpresapadrao(emp.getEmpresapadrao());
                if(usuemp.getEmpresapadrao()) {
                    Tenant t = repoTenant.findPorId(usuemp.getTenantId());
                    atualizado.setTenant(t);
                }
                repoUsuarioEmpresa.save(usuemp);
            }

        }
    }



    private void logUsuario(Usuario obj, String string) {
        LogSistema logsistema = log.insert(obj, string);
        logsistema.setUsuario(obj);
        repolog.save(logsistema);

    }


    public UsuarioFlat from(UsuarioFlat obj) {

        Usuario usuAtual = buscarOuFalhar(obj.getId());
        Usuario atualizado = new Usuario(usuAtual, obj);
        repoPermissao.deletaPorUsuario(obj.getId());
        repoPermissao.saveAll(atualizado.getPermissoes());
        atualizado.setTenant(tenantUsuario.buscarOuFalhar());
        atualizado.setStatus(obj.getStatus());
        atualizado.setTenantativo(tenantUsuario.buscarOuFalhar().getId());
        repoUsuarioEmpresa.deleteEmpPorUsuario(obj.getId());
        inserirEmpUsu(obj, atualizado);
        logUsuario(atualizado, "alterar");

        repo.save(atualizado);

        return obj;

    }

    public UsuarioFlat fromSenha(UsuarioFlat obj) {
        Usuario usuAtual = repo.buscarUsuarioId(obj.getId());
        usuAtual.setSenha(pe.encode(obj.getSenha()));
        repo.saveSenha(usuAtual.getSenha(), obj.getId());

        return obj;

    }

    public Usuario fromSenha(String obj) {
        Usuario usuAtual = repo.buscarUsuario(usutoken.getUsuario());
        usuAtual.setSenha(pe.encode(obj));
        repo.saveSenha(usuAtual.getSenha(), usuAtual.getId());

        return usuAtual;

    }


    public List<UsuarioFlat> findAllSQL() {
        List<UsuarioFlat>ususFlat = new ArrayList<UsuarioFlat>();
        List<Usuario>usuFlat = repo.findAllSql();
        for(Usuario usu :usuFlat ) {
            UsuarioFlat usuflat = new UsuarioFlat(usu);
            ususFlat.add(usuflat);
        }

        return ususFlat;

    }
    public List<UsuarioFlat> findAllSqlInativo() {

        List<UsuarioFlat>ususFlat = new ArrayList<UsuarioFlat>();
        List<Usuario>usuFlat = repo.findAllSqlInativo(tenantUsuario.buscarOuFalharInt());
        for(Usuario usu :usuFlat ) {
            UsuarioFlat usuflat = new UsuarioFlat(usu);
            ususFlat.add(usuflat);
        }

        return ususFlat;
    }


    public Page<Usuario> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
        return repo.findAll(pageRequest);
    }

    public Usuario buscarOuFalhar(int id) {
        return repo.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaExcepition(String.format("Usuario não encontrada", id)));
    }



    @Transactional
    public void status(Boolean obj, int id) {
        Usuario usu = buscarOuFalhar(id);
        usu.setStatus(obj);

        logUsuario(usu, "status");
        repo.save(usu);
    }

    public void tenantAtivo(int idempresa) {
        if (idempresa != 0) {
            Integer tenant = repoempresa.buscarTenant(idempresa);
            repo.settenantAtivo(tenant, tenantUsuario.buscarUsuario().getId());
            repo.setGtenantAtivo(0, tenantUsuario.buscarUsuario().getId());

        }

    }

    public Page<UsuarioFlat> mudarUsuarioParaFlat(Page<Usuario> pacs) {
        List<UsuarioFlat> cFlats = new ArrayList<UsuarioFlat>();
        for (Usuario c : pacs.getContent()) {
            UsuarioFlat cFlat = new UsuarioFlat(c);
            cFlats.add(cFlat);

        }
        Page<UsuarioFlat> page = new PageImpl<>(cFlats, pacs.getPageable(),
                pacs.getTotalElements());

        return page;
    }

}