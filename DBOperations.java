import java.sql.*;
public class DBOperations {

    public static final String url = "jdbc:postgresql://localhost:5432/Bookstore";
    public static final String username = "postgres";
    public static final String password = "123";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public void insertBook(int book_id, String title, int stock, int author_id) {
        String sql = "INSERT INTO books (book_id, title, stock, author_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, book_id);
            pstmt.setString(2, title);
            pstmt.setInt(3, stock);
            pstmt.setInt(4, author_id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("A new book was inserted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void retrieveBooks() {
        String sql = "SELECT * FROM books";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("Book: " + rs.getString("title"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void updateBook(int book_id, String title) {
        String sql = "UPDATE books SET title = ? WHERE book_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setInt(2, book_id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Book was updated successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteBook(int book_id) {
        String sql = "DELETE FROM books WHERE book_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, book_id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Book was deleted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void insertAuthor(int author_id, String author_fname, String author_lname) {
        String sql = "INSERT INTO books (author_id, author_fname, author_lname) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, author_id);
            pstmt.setString(2, author_fname);
            pstmt.setString(3, author_lname);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("A new entry in authors table was inserted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void placeOrder(int order_id, int payment, int customer_id, int book_id) {
        String checkStockSql = "SELECT stock FROM books WHERE id = ?";
        String updateStockSql = "UPDATE books SET stock = stock - 1 WHERE id = ?";
        String insertOrderSql = "INSERT INTO orders (order_id, payment, customer_id, book_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection()) {

            conn.setAutoCommit(false);

            try (PreparedStatement checkStockStmt = conn.prepareStatement(checkStockSql)) {
                checkStockStmt.setInt(1, book_id);
                ResultSet rs = checkStockStmt.executeQuery();
                if (!rs.next() || rs.getInt("stock") < 1) {
                    throw new Exception("Stock for book with id: " + book_id + " is empty.");
                }
            }

            try (PreparedStatement updateStockStmt = conn.prepareStatement(updateStockSql)) {
                updateStockStmt.setInt(1, book_id);
                int affectedRows = updateStockStmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Updating stock failed, no rows affected.");
                }
            }

            try (PreparedStatement insertOrderStmt = conn.prepareStatement(insertOrderSql)) {
                insertOrderStmt.setInt(1, order_id);
                insertOrderStmt.setInt(2, payment);
                insertOrderStmt.setInt(3, customer_id);
                insertOrderStmt.setInt(4, book_id);
                int affectedRows = insertOrderStmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Creating order failed, no rows affected.");
                }
            }

            conn.commit();
            System.out.println("Order placed successfully!");

        } catch (Exception e) {
            System.err.println("Order placement failed: " + e.getMessage());
            try (Connection conn = getConnection()) {
                System.err.println("Transaction is being rolled back");
                conn.rollback();
            } catch (SQLException excep) {
                e.addSuppressed(excep);
            }
        }
    }



}
