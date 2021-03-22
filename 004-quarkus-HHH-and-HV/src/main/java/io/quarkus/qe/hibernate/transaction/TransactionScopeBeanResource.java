package io.quarkus.qe.hibernate.transaction;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

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
