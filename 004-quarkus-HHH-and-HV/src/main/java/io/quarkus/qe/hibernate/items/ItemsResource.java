package io.quarkus.qe.hibernate.items;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

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
