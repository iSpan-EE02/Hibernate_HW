package orders.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import orders.bean.ShipBean;
import orders.utils.LAButil;

public class ShipDao {
	
//	public ShipDao() {
//		try {
//			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//		} catch (ClassNotFoundException e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//	}
	
	
	public List <ShipBean> findAll() {
		List<ShipBean> list = new ArrayList<>();
		String shipSql="SELECT * FROM ship";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = LAButil.getConnection();
			ps = conn.prepareStatement(shipSql);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				ShipBean bean = new ShipBean();
				bean.setShipId(rs.getInt("ship_id"));
				bean.setShipName(rs.getString("ship_name"));
				bean.setShipFee(rs.getInt("ship_fee"));
				list.add(bean);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			LAButil.closeRs(conn, ps, rs);
		}
		
		return list;
	}
}
