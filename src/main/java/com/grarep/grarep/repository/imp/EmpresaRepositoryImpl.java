package com.grarep.grarep.repository.imp;


import com.grarep.grarep.domain.Empresa;
import com.grarep.grarep.domain.Tenant;
import com.grarep.grarep.repository.filter.EmpresaFilter;
import com.grarep.grarep.repository.query.EmpresaRepositoryQuery;
import com.grarep.grarep.service.util.Tenantuser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Primary
@Component
public class EmpresaRepositoryImpl implements EmpresaRepositoryQuery {
    @PersistenceContext
    private EntityManager manager;
    @Autowired
    private Tenantuser tenantUsuario;


    @Override
    public Page<Empresa> filtrar(EmpresaFilter empresaFilter, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Empresa> criteria = builder.createQuery(Empresa.class);
        Root<Empresa> root = criteria.from(Empresa.class);
        From<?, ?> logJoin = root.join("logs", JoinType.LEFT);
        criteria.distinct(true);
        List<Order> orderList = new ArrayList();
        orderList.add(builder.desc(root.get("id")));
        criteria.orderBy(orderList);


        Predicate[] predicates = criarRestricoes(empresaFilter, builder, root, logJoin);
        criteria.where(predicates);

        TypedQuery<Empresa> query = manager.createQuery(criteria);
        List<Empresa> listacarga = new ArrayList<>();
        listacarga = query.getResultList();
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, listacarga.size());
    }

    private Predicate[] criarRestricoes(EmpresaFilter empresaFilter, CriteriaBuilder builder, Root<Empresa> root,
                                        From<?, ?> logJoin) {
        List<Predicate> predicates = new ArrayList<>();
        Tenant t = tenantUsuario.buscarOuFalhar();
        predicates.add(builder.equal(builder.lower(root.get("tenant")), t));
        if (empresaFilter.getId() != null) {

            try {
                Integer valor = Integer.parseInt(String.valueOf(empresaFilter.getId()));
                predicates.add(builder.equal(builder.lower(root.get("id")), valor));
            } catch (Exception e) {
                predicates.add(builder.equal(builder.lower(root.get("id")), 0));
            }

        }

        if (empresaFilter.getBairro() != null) {
            predicates.add(
                    builder.like(builder.lower(root.get("bairro")), "%" + empresaFilter.getBairro() + "%"));
        }

        if (empresaFilter.getCep() != null) {
            predicates.add(
                    builder.like(builder.lower(root.get("cep")), "%" + empresaFilter.getCep() + "%"));
        }

        if (empresaFilter.getCidade() != null) {
            predicates.add(
                    builder.like(builder.lower(root.get("cidade")), "%" + empresaFilter.getCidade() + "%"));
        }

        if (empresaFilter.getComplemento() != null) {
            predicates.add(
                    builder.like(builder.lower(root.get("complemento")), "%" + empresaFilter.getComplemento() + "%"));
        }

        if (empresaFilter.getCpfoucnpj() != null) {
            predicates.add(
                    builder.like(builder.lower(root.get("cpfoucnpj")), "%" + empresaFilter.getCpfoucnpj() + "%"));
        }

        if (empresaFilter.getEmail() != null) {
            predicates.add(
                    builder.like(builder.lower(root.get("email")), "%" + empresaFilter.getEmail() + "%"));
        }

        if (empresaFilter.getLogradouro() != null) {
            predicates.add(
                    builder.like(builder.lower(root.get("logradouro")), "%" + empresaFilter.getLogradouro() + "%"));
        }

        if (empresaFilter.getNaturezapessoa() != null) {
            predicates.add(
                    builder.like(builder.lower(root.get("naturezapessoa")), "%" + empresaFilter.getNaturezapessoa() + "%"));
        }

        if (empresaFilter.getNumero() != null) {
            predicates.add(
                    builder.like(builder.lower(root.get("numero")), "%" + empresaFilter.getNumero() + "%"));
        }

        if (empresaFilter.getRazaosocial() != null) {
            predicates.add(
                    builder.like(builder.lower(root.get("razaosocial")), "%" + empresaFilter.getRazaosocial() + "%"));
        }

        if (empresaFilter.getTelefone() != null) {
            predicates.add(
                    builder.like(builder.lower(root.get("telefone")), "%" + empresaFilter.getTelefone() + "%"));
        }

        if (empresaFilter.getUf() != null) {
            predicates.add(
                    builder.like(builder.lower(root.get("uf")), "%" + empresaFilter.getRazaosocial() + "%"));
        }

        if (empresaFilter.getValor() != null) {
            predicates.add(
                    builder.equal(root.get("valor"), empresaFilter.getValor())
            );
        }

        if (empresaFilter.getWhats() != null) {
            predicates.add(
                    builder.like(builder.lower(root.get("whats")), "%" + empresaFilter.getWhats() + "%"));
        }


        if (empresaFilter.getDatagravacaode() != null) {
            Instant instant = empresaFilter.getDatagravacaode().toInstant();
            ZoneOffset zoneOffset = ZoneId.systemDefault().getRules().getOffset(Instant.now());
            OffsetDateTime offsetDateTime = instant.atOffset(zoneOffset);
            predicates.add(builder.greaterThanOrEqualTo(logJoin.get("datagravacao"), offsetDateTime));
        }
        if (empresaFilter.getDatagravacaoate() != null) {
            Instant instant = empresaFilter.getDatagravacaoate().toInstant();
            ZoneOffset zoneOffset = ZoneId.systemDefault().getRules().getOffset(Instant.now());
            OffsetDateTime offsetDateTime = instant.atOffset(zoneOffset);
            predicates.add(builder.lessThanOrEqualTo(logJoin.get("datagravacao"), offsetDateTime));
        }

        if (empresaFilter.getStatus().equals("Ativos")) {
            predicates.add(builder.equal(root.get("status"), true)); // Retorna empresas ativas (status = true)
        } else {
            predicates.add(builder.equal(root.get("status"), false)); // Retorna empresas inativas (status = false)
        }

        return predicates.toArray(new Predicate[predicates.size()]);
    }

    private void adicionarRestricoesDePaginacao(TypedQuery<Empresa> query, Pageable pageable) {

        int paginaAtual = pageable.getPageNumber();

        int totalRegistrosPorPagina = pageable.getPageSize();

        int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;
        query.setFirstResult(primeiroRegistroDaPagina);
        query.setMaxResults(totalRegistrosPorPagina);

    }

    private Long total(EmpresaFilter empresaFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Empresa> root = criteria.from(Empresa.class);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}
