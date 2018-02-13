package dbtLab3;

import java.sql.*;
import java.util.*;

import javax.swing.DefaultListModel;

/**
 * Database is an interface to the college application database, it uses JDBC to
 * connect to a SQLite3 file.
 */
public class Database {

	/**
	 * The database connection.
	 */
	private Connection conn;

	/**
	 * Creates the database interface object. Connection to the database is
	 * performed later.
	 */
	public Database() {
		conn = null;
	}

	/**
	 * Opens a connection to the database, using the specified filename (if we'd
	 * used a traditional DBMS, such as PostgreSQL or MariaDB, we would have
	 * specified username and password instead).
	 */
	public boolean openConnection(String filename) {
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:" + filename);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Closes the connection to the database.
	 */
	public void closeConnection() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks if the connection to the database has been established
	 * 
	 * @return true if the connection has been established
	 */
	public boolean isConnected() {
		return conn != null;
	}

	/* ================================== */
	/* --- insert your own code below --- */
	/* ===============================*== */
	public boolean loginUser(String u_name) {
		String query = "SELECT u_name " + "FROM users " + "WHERE u_name = ?";
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			Set<String> found = new HashSet<String>();
			ps.setString(1, u_name);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				found.add(rs.getString("u_name"));
			}
			if (found.contains(u_name)) {
				rs.close();
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}
	/*
	 * public List<...> ...(...) { List<...> found = new LinkedList<>(); String
	 * query = "SELECT  ...\n" + "FROM    ...\n" + "...\n"; try (PreparedStatement
	 * ps = conn.prepareStatement(query)) { ps.setString(1, ...); ResultSet rs =
	 * ps.executeQuery(); while (rs.next()) { found.add(new ...(rs)); } } catch
	 * (SQLException e) { e.printStackTrace(); } return found; }
	 */

	public HashSet<String> getMovieNames() {
		HashSet<String> ret = new HashSet<String>();
		String query = "SELECT m_name FROM performances";
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ret.add(rs.getString("m_name"));
			}

			return ret;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public HashSet<String> getPerformanceDates(String m_name) {
		// TODO Auto-generated method stub
		HashSet<String> ret = new HashSet<String>();
		String query = "SELECT date FROM performances WHERE m_name = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, m_name);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ret.add(rs.getString("date"));
			}
			rs.close();
			return ret;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ShowStats getPerformanceData(String m_name, String date) {
		String query = "SELECT m_name, date, t_name,  seat_amount - ("
				+ "														SELECT COUNT()"
				+ "														FROM reservations"
				+ "														JOIN performances"
				+ "														USING(m_name, date)"
				+ "														WHERE m_name = ?"
				+ "														AND date = ?"
				+ "														) AS available_seats"
				+ "		FROM performances" + "		JOIN theaters" + "		USING (t_name)" + "		WHERE m_name = ?"
				+ "		AND date = ?";

		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, m_name);
			ps.setString(2, date);
			ps.setString(3, m_name);
			ps.setString(4, date);
			ResultSet rs = ps.executeQuery();
			return new ShowStats(rs);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
		// TODO Auto-generated method stub

	}

	public boolean bookTicket(String m_name, String date) {
		
		String query = "SELECT seat_amount - (\n" + 
				"                        SELECT COUNT()\n" + 
				"                        FROM reservations\n" + 
				"                        JOIN performances\n" + 
				"                        USING(m_name, date)\n" + 
				"                        WHERE m_name = ?\n" + 
				"                        AND date = ?\n" + 
				"                        ) AS available_seats\n" + 
				"FROM performances\n" + 
				"JOIN theaters\n" + 
				"USING (t_name)\n" + 
				"WHERE m_name = ?\n" + 
				"AND   date = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, m_name);
			ps.setString(2, date);
			ps.setString(3, m_name);
			ps.setString(4, date);
			ResultSet rs = ps.executeQuery();
			if(rs.getInt("available_seats") > 0) {
				String query2 = "INSERT INTO reservations(res_id, date, m_name, u_name)"
					  + "VALUES	(NULL, ?, ?, ?); ";
				ps = conn.prepareStatement(query2);
				ps.setString(1, date);
				ps.setString(2, m_name);
				ps.setString(3, CurrentUser.instance().getCurrentUserId());
				ps.executeUpdate();
				return true;
			}
			ps.close();
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;	
	}
}
