import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;


public class Connect {
    public static void main(String[] args) throws Exception {
       String sql = "select * from books";
       String url = "jdbc:postgresql://localhost:5432/Bookstore";
       String username="postgres";
       String password = "123";

       Connection con = DriverManager.getConnection(url, username, password);
        Statement st = con.createStatement();
        st.executeQuery(sql);
    //     ResultSet rs = st.executeQuery(sql);
    //     rs.next();
    //     String book_id = rs.getString(coumn);
    // }
    }
}