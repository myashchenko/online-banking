package io.github.yashchenkon.banking.infra.database;

import io.github.yashchenkon.banking.domain.exception.DataAccessException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Callable;

import javax.inject.Inject;

/**
 * This abstraction is used to perform som unit of work in DB transactional context.
 * <p>
 * The assumption is that all operations will be done via this executor and no one will close connection manually.
 */
public class TransactionalExecutor {

    private final ConnectionProvider connectionProvider;

    @Inject
    public TransactionalExecutor(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    /**
     * Executes a unit of work in DB transaction.
     * If the unit of work throws exception, it rollbacks a transaction, otherwise - it commits the transaction.
     *
     * @param runnable - unit of work
     */
    public void execute(Runnable runnable) {
        Connection connection = connectionProvider.acquire();
        try {
            connection.setAutoCommit(false);

            try {
                runnable.run();
                connectionProvider.commitAndRelease();
            } catch (Exception e) {
                connectionProvider.rollbackAndRelease();
                sneakyThrow(e);
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    /**
     * Retrieves data from DB in read only mode.
     * Needed for proper connection management.
     *
     * @param callable - unit of work retrieving some information from DB
     * @param <T> - type of data to return
     * @return result of unit of work
     */
    public <T> T executeReadOnly(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            sneakyThrow(e);
            return null;
        } finally {
            connectionProvider.release();
        }
    }

    @SuppressWarnings("unchecked")
    public static <E extends Throwable> void sneakyThrow(Throwable e) throws E {
        throw (E) e;
    }
}
