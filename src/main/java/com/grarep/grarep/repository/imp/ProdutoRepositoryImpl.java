package com.grarep.grarep.repository.imp;


import com.grarep.grarep.domain.Produto;
import com.grarep.grarep.domain.Tenant;
import com.grarep.grarep.repository.filter.ProdutoFilter;
import com.grarep.grarep.repository.query.ProdutoRepositoryQuery;
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
import javax.transaction.Transactional;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;


@Primary
@Component
public class ProdutoRepositoryImpl implements ProdutoRepositoryQuery {

    @PersistenceContext
    private EntityManager manager;
    @Autowired
    private Tenantuser tenantUsuario;


    @Override
    public Page<Produto> filtrar(ProdutoFilter produtoFilter, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteria = builder.createQuery(Produto.class);
        Root<Produto> root = criteria.from(Produto.class);
        From<?, ?> logJoin = root.join("logs", JoinType.LEFT);
        criteria.distinct(true);
        List<Order> orderList = new ArrayList();
        orderList.add(builder.desc(root.get("id")));
        criteria.orderBy(orderList);


        Predicate[] predicates = criarRestricoes(produtoFilter, builder, root, logJoin );
        criteria.where(predicates);

        TypedQuery<Produto> query = manager.createQuery(criteria);
        List<Produto> listacarga = new ArrayList<>();
        listacarga = query.getResultList();
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, listacarga.size());
    }

    private Predicate[] criarRestricoes(ProdutoFilter produtoFilter, CriteriaBuilder builder, Root<Produto> root,
                                        From<?, ?> logJoin) {
        List<Predicate> predicates = new ArrayList<>();
        Tenant t = tenantUsuario.buscarOuFalhar();
        predicates.add(builder.equal(builder.lower(root.get("tenant")), t));
        if (produtoFilter.getId() != null) {

            try {
                Integer valor = Integer.parseInt(String.valueOf(produtoFilter.getId()));
                predicates.add(builder.equal(builder.lower(root.get("id")), valor));
            } catch (Exception e) {
                predicates.add(builder.equal(builder.lower(root.get("id")), 0));
            }

        }

        if (produtoFilter.getNome() != null) {
            predicates.add(
                    builder.like(builder.lower(root.get("nome")), "%" + produtoFilter.getNome() + "%"));
        }

        if (produtoFilter.getSku() != null) {
            predicates.add(
                    builder.like(builder.lower(root.get("sku")), "%" + produtoFilter.getSku() + "%"));
        }

        if (produtoFilter.getDatagravacaode() != null) {
            Instant instant = produtoFilter.getDatagravacaode().toInstant();
            ZoneOffset zoneOffset = ZoneId.systemDefault().getRules().getOffset(Instant.now());
            OffsetDateTime offsetDateTime = instant.atOffset(zoneOffset);
            predicates.add(builder.greaterThanOrEqualTo(logJoin.get("datagravacao"), offsetDateTime));
        }
        if (produtoFilter.getDatagravacaoate() != null) {
            Instant instant = produtoFilter.getDatagravacaoate().toInstant();
            ZoneOffset zoneOffset = ZoneId.systemDefault().getRules().getOffset(Instant.now());
            OffsetDateTime offsetDateTime = instant.atOffset(zoneOffset);
            predicates.add(builder.lessThanOrEqualTo(logJoin.get("datagravacao"), offsetDateTime));
        }

        return predicates.toArray(new Predicate[predicates.size()]);
    }

    private void adicionarRestricoesDePaginacao(TypedQuery<Produto> query, Pageable pageable) {

        int paginaAtual = pageable.getPageNumber();

        int totalRegistrosPorPagina = pageable.getPageSize();

        int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;
        query.setFirstResult(primeiroRegistroDaPagina);
        query.setMaxResults(totalRegistrosPorPagina);

    }

    private Long total(ProdutoFilter produtoFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Produto> root = criteria.from(Produto.class);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}