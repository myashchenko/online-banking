package io.github.yashchenkon.banking.infra.database;

import io.github.yashchenkon.banking.domain.exception.DataAccessException;

import java.sql.Connection;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.sql.DataSource;

/**
 * Connection provider is basically created for handling transactions on a service level and to make sure
 * transactional context uses the same connection as repositories do.
 */
public class ConnectionProvider {

    public static ThreadLocal<Connection> CONNECTIONS = new ThreadLocal<>();

    private final DataSource dataSource;

    @Inject
    public ConnectionProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Pulls new connection from connection pool and attaches it to the current thread.
     * If connection is already attached to the current thread, the method returns it as is.
     *
     * @return connection
     */
    public Connection acquire() {
        Connection connection = CONNECTIONS.get();
        if (connection == null) {
            try {
                connection = dataSource.getConnection();
                CONNECTIONS.set(connection);
            } catch (SQLException e) {
                throw new DataAccessException(e);
            }
        }

        return connection;
    }

    /**
     * Commits a connection attached to current thread if connection is not closed and detaches it from the current thread.
     * If connection is closed, just detaches connection from the current thread.
     */
    public void commitAndRelease() {
        Connection connection = CONNECTIONS.get();
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.commit();
                }
            } catch (SQLException e) {
                throw new DataAccessException(e);
            } finally {
                release();
            }
        }
    }

    /**
     * Rollbacks all changes and detaches connection from the current thread.
     */
    public void rollbackAndRelease() {
        Connection connection = CONNECTIONS.get();
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.rollback();
                }
            } catch (SQLException e) {
                throw new DataAccessException(e);
            } finally {
                release();
            }
        }
    }

    /**
     * Detaches connection from the current thread.
     */
    public void release() {
        Connection connection = CONNECTIONS.get();
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new DataAccessException(e);
            } finally {
                CONNECTIONS.set(null);
            }
        }
    }
}
