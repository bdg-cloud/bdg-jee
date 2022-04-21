//package fr.legrain.hibernate.multitenant;
//
//import java.sql.Connection;
//
//import com.mchange.v2.c3p0.AbstractConnectionCustomizer;
//
//public class IsolationLevelConnectionCustomizer extends
//        AbstractConnectionCustomizer {
//
//    @Override
//    public void onCheckOut(Connection c, String parentDataSourceIdentityToken) throws Exception {
//        super.onCheckOut(c, parentDataSourceIdentityToken);
//       // System.out.println("Connection acquired, set autocommit off and repeatable read transaction mode.");
//        c.setAutoCommit(false);
////        c.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
//    }
//}