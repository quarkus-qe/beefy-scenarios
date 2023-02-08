package io.quarkus.qe.hibernate.items;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/items")
@Transactional
public class ItemsResource {

    @Inject
    EntityManager em;

    @GET
    @Path("/count")
    public int countOrders() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Item> cq = cb.createQuery(Item.class);
        // Eager fetch the relationship between item and customer
        // Do not remove as this is the actual reproducer for https://github.com/quarkusio/quarkus/issues/14201 and
        // https://github.com/quarkusio/quarkus/issues/14881.
        cq.from(Item.class).fetch("customer");

        TypedQuery<Item> query = em.createQuery(cq);
        return query.getResultList().size();
    }

}
