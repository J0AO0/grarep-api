package com.grarep.grarep.repository.imp;


import com.grarep.grarep.domain.Tenant;
import com.grarep.grarep.domain.Usuario;
import com.grarep.grarep.repository.filter.UsuarioFilter;
import com.grarep.grarep.repository.query.UsuarioRepositoryQuery;
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
public class UsuarioRepositoryImpl implements UsuarioRepositoryQuery {

    @PersistenceContext
    private EntityManager manager;
    @Autowired
    private Tenantuser tenantUsuario;


    @Override
    public Page<Usuario> filtrar(UsuarioFilter usuarioFilter, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Usuario> criteria = builder.createQuery(Usuario.class);
        Root<Usuario> root = criteria.from(Usuario.class);
        From<?, ?> logJoin = root.join("logs", JoinType.LEFT);
        criteria.distinct(true);
        List<Order> orderList = new ArrayList();
        orderList.add(builder.desc(root.get("id")));
        criteria.orderBy(orderList);


        Predicate[] predicates = criarRestricoes(usuarioFilter, builder, root, logJoin);
        criteria.where(predicates);

        TypedQuery<Usuario> query = manager.createQuery(criteria);
        List<Usuario> listacarga = new ArrayList<>();
        listacarga = query.getResultList();
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, listacarga.size());
    }

    private Predicate[] criarRestricoes(UsuarioFilter usuarioFilter, CriteriaBuilder builder, Root<Usuario> root,
                                        From<?, ?> logJoin) {
        List<Predicate> predicates = new ArrayList<>();
        Tenant t = tenantUsuario.buscarOuFalhar();
        predicates.add(builder.equal(builder.lower(root.get("tenant")), t));
        if (usuarioFilter.getId() != null) {

            try {
                Integer valor = Integer.parseInt(String.valueOf(usuarioFilter.getId()));
                predicates.add(builder.equal(builder.lower(root.get("id")), valor));
            } catch (Exception e) {
                predicates.add(builder.equal(builder.lower(root.get("id")), 0));
            }

        }

        if (usuarioFilter.getEmail() != null) {
            predicates.add(
                    builder.like(builder.lower(root.get("email")), "%" + usuarioFilter.getEmail() + "%"));
        }

        if (usuarioFilter.getLogin() != null) {
            predicates.add(
                    builder.like(builder.lower(root.get("login")), "%" + usuarioFilter.getLogin() + "%"));
        }

        if (usuarioFilter.getTelefone() != null) {
            predicates.add(
                    builder.like(builder.lower(root.get("telefone")), "%" + usuarioFilter.getTelefone() + "%"));
        }

        if (usuarioFilter.getDatagravacaode() != null) {
            Instant instant = usuarioFilter.getDatagravacaode().toInstant();
            ZoneOffset zoneOffset = ZoneId.systemDefault().getRules().getOffset(Instant.now());
            OffsetDateTime offsetDateTime = instant.atOffset(zoneOffset);
            predicates.add(builder.greaterThanOrEqualTo(logJoin.get("datagravacao"), offsetDateTime));
        }
        if (usuarioFilter.getDatagravacaoate() != null) {
            Instant instant = usuarioFilter.getDatagravacaoate().toInstant();
            ZoneOffset zoneOffset = ZoneId.systemDefault().getRules().getOffset(Instant.now());
            OffsetDateTime offsetDateTime = instant.atOffset(zoneOffset);
            predicates.add(builder.lessThanOrEqualTo(logJoin.get("datagravacao"), offsetDateTime));
        }

        if (usuarioFilter.getStatus().equals("Ativos")) {
            predicates.add(builder.equal(root.get("status"), true)); // Retorna usuarios ativas (status = true)
        } else {
            predicates.add(builder.equal(root.get("status"), false)); // Retorna usuarios inativas (status = false)
        }


        return predicates.toArray(new Predicate[predicates.size()]);
    }

    private void adicionarRestricoesDePaginacao(TypedQuery<Usuario> query, Pageable pageable) {

        int paginaAtual = pageable.getPageNumber();

        int totalRegistrosPorPagina = pageable.getPageSize();

        int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;
        query.setFirstResult(primeiroRegistroDaPagina);
        query.setMaxResults(totalRegistrosPorPagina);

    }

    private Long total(UsuarioFilter usuarioFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Usuario> root = criteria.from(Usuario.class);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }

}
