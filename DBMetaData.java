import java.sql.*;

public class DBMetaData {

    private static final String url = "jdbc:postgresql://localhost:5432/Bookstore";
    private static final String username = "postgres";
    private static final String password = "123";

    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            DatabaseMetaData metadata = connection.getMetaData();

            System.out.println("Tables in the database:");
            getTables(metadata);

            System.out.println("\nColumns of the table 'books':");
            getColumns(metadata, "books");

            getPrimaryKeys(metadata, "books");

            System.out.println("\nForeign keys of the table 'orders':");
            getForeignKeys(metadata, "orders");

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getTables(DatabaseMetaData metadata) throws SQLException {
        try (ResultSet tables = metadata.getTables(null, null, "%", new String[]{"TABLE"})) {
            while (tables.next()) {
                System.out.println("Table: " + tables.getString("TABLE_NAME"));
            }
        }
    }

    public static void getColumns(DatabaseMetaData metadata, String tableName) throws SQLException {
        try (ResultSet columns = metadata.getColumns(null, null, tableName, "%")) {
            while (columns.next()) {
                System.out.println("Column: " + columns.getString("COLUMN_NAME")
                        + ", Type: " + columns.getString("TYPE_NAME")
                        + ", Size: " + columns.getInt("COLUMN_SIZE"));
            }
        }
    }

    public static void getPrimaryKeys(DatabaseMetaData metadata, String tableName) throws SQLException {
        try (ResultSet primaryKeys = metadata.getPrimaryKeys(null, null, tableName)) {
            while (primaryKeys.next()) {
                System.out.println("Primary Key: " + primaryKeys.getString("COLUMN_NAME"));
            }
        }
    }

    public static void getForeignKeys(DatabaseMetaData metadata, String tableName) throws SQLException {
        try (ResultSet foreignKeys = metadata.getImportedKeys(null, null, tableName)) {
            while (foreignKeys.next()) {
                System.out.println("Foreign Key: " + foreignKeys.getString("FKCOLUMN_NAME")
                        + ", in Table: " + foreignKeys.getString("PKTABLE_NAME"));
            }
        }
    }
}
