package io.quarkus.qe.hibernate.transaction;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/transaction-scope")
public class TransactionScopeBeanResource {

    @Inject
    TransactionScopeBean bean;

    @POST
    @Transactional
    @Path("/invoke-bean")
    public int invokeBean() {
        TransactionScopeBean.resetFlags();
        return bean.increment();
    }

    @GET
    @Path("/is-post-construct-invoked")
    public boolean isPostConstructInvoked() {
        return TransactionScopeBean.POST_CONSTRUCT_INVOKED.get();
    }

    @GET
    @Path("/is-pre-destroy-invoked")
    public boolean isPreDestroyInvoked() {
        return TransactionScopeBean.PRE_DESTROY_INVOKED.get();
    }

}
