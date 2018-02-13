package dbtLab3;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ShowStats {

	

	    public final String m_name;
	    public final String date;
	    public final String t_name;
	    public final int available_seats;

	    public ShowStats  (ResultSet rs) throws SQLException {
	        this.m_name = rs.getString("m_name");
	        this.date = rs.getString("date");
	        this.available_seats = rs.getInt("available_seats");
	        this.t_name = rs.getString("t_name");
	    
	}
}
