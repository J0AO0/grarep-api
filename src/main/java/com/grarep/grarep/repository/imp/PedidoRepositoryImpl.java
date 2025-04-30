package com.grarep.grarep.repository.imp;


import com.grarep.grarep.domain.Pedido;
import com.grarep.grarep.domain.Tenant;
import com.grarep.grarep.repository.filter.PedidoFilter;
import com.grarep.grarep.repository.query.PedidoRepositoryQuery;
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
public class PedidoRepositoryImpl implements PedidoRepositoryQuery {

    @PersistenceContext
    private EntityManager manager;
    @Autowired
    private Tenantuser tenantUsuario;


    @Override
    public Page<Pedido> filtrar(PedidoFilter pedidoFilter, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteria = builder.createQuery(Pedido.class);
        Root<Pedido> root = criteria.from(Pedido.class);
        From<?, ?> logJoin = root.join("logs", JoinType.LEFT);
        From<?, ?> pedidoJoin = root.join("pedido", JoinType.INNER);
        criteria.distinct(true);
        List<Order> orderList = new ArrayList();
        orderList.add(builder.desc(root.get("id")));
        criteria.orderBy(orderList);


        Predicate[] predicates = criarRestricoes(pedidoFilter, builder, root, logJoin,pedidoJoin );
        criteria.where(predicates);

        TypedQuery<Pedido> query = manager.createQuery(criteria);
        List<Pedido> listacarga = new ArrayList<>();
        listacarga = query.getResultList();
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, listacarga.size());
    }

    private Predicate[] criarRestricoes(PedidoFilter pedidoFilter, CriteriaBuilder builder, Root<Pedido> root,
                                        From<?, ?> logJoin, From<?, ?> pedidoJoin) {
        List<Predicate> predicates = new ArrayList<>();
        Tenant t = tenantUsuario.buscarOuFalhar();
        predicates.add(builder.equal(builder.lower(root.get("tenant")), t));
        if (pedidoFilter.getId() != null) {

            try {
                Integer valor = Integer.parseInt(String.valueOf(pedidoFilter.getId()));
                predicates.add(builder.equal(builder.lower(root.get("id")), valor));
            } catch (Exception e) {
                predicates.add(builder.equal(builder.lower(root.get("id")), 0));
            }

        }

        if (pedidoFilter.getRepresentante() != null) {
            predicates.add(
                    builder.like(builder.lower(root.get("cliente")), "%" + pedidoFilter.getRepresentante() + "%"));
        }

        if (pedidoFilter.getArquiteto() != null) {
            predicates.add(
                    builder.like(builder.lower(root.get("tipopedido")), "%" + pedidoFilter.getArquiteto() + "%"));
        }

        if (pedidoFilter.getNf() != null) {
            predicates.add(
                    builder.like(builder.lower(root.get("tipofrete")), "%" + pedidoFilter.getNf() + "%"));
        }

        if (pedidoFilter.getDatagravacaode() != null) {
            Instant instant = pedidoFilter.getDatagravacaode().toInstant();
            ZoneOffset zoneOffset = ZoneId.systemDefault().getRules().getOffset(Instant.now());
            OffsetDateTime offsetDateTime = instant.atOffset(zoneOffset);
            predicates.add(builder.greaterThanOrEqualTo(logJoin.get("datagravacao"), offsetDateTime));
        }
        if (pedidoFilter.getDatagravacaoate() != null) {
            Instant instant = pedidoFilter.getDatagravacaoate().toInstant();
            ZoneOffset zoneOffset = ZoneId.systemDefault().getRules().getOffset(Instant.now());
            OffsetDateTime offsetDateTime = instant.atOffset(zoneOffset);
            predicates.add(builder.lessThanOrEqualTo(logJoin.get("datagravacao"), offsetDateTime));
        }


        return predicates.toArray(new Predicate[predicates.size()]);
    }

    private void adicionarRestricoesDePaginacao(TypedQuery<Pedido> query, Pageable pageable) {

        int paginaAtual = pageable.getPageNumber();

        int totalRegistrosPorPagina = pageable.getPageSize();

        int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;
        query.setFirstResult(primeiroRegistroDaPagina);
        query.setMaxResults(totalRegistrosPorPagina);

    }

    private Long total(PedidoFilter pedidoFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Pedido> root = criteria.from(Pedido.class);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}
