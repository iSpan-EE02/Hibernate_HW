package orders.dao;

import java.security.interfaces.RSAKey;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.tribes.transport.RxTaskPool;

import orders.bean.OrdersBean;
import orders.bean.OrdersItemBean;
import orders.utils.LAButil;

public class OrdersDao {
		
	//新增商品
	public int insertOrders(OrdersBean ordersBean) {
		String insertSql="INSERT INTO orders (mem_id,ship_id,ship_fee, total_price, final_price,orders_date) VALUES(?,?,?,?,?,GETDATE())";
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			connection = LAButil.getConnection();
			ps = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
			
			ps.setInt(1, ordersBean.getMemId());
			ps.setInt(2, ordersBean.getShipId());
			ps.setInt(3, ordersBean.getShipFee());			
			ps.setInt(4, ordersBean.getTotalPrice());
			ps.setInt(5, ordersBean.getFinalPrice());
			ps.executeUpdate();
			
			rs = ps.getGeneratedKeys();
			if(rs.next()) {
				return rs.getInt(1);// 回傳自動產生的訂單ID
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			LAButil.closeRs(connection, ps, rs);
		}
		return -1;
	}
	
	// 新增訂單明細（多筆）
	public void insertOrderItems(int ordersId, List<OrdersItemBean> items) {
	    String sql = "INSERT INTO orders_item (orders_id, prod_id, quantity, unit_price, subtotal, prod_name, prod_img) VALUES (?, ?, ?, ?, ?, ?, ?)";
	    Connection conn = null;
	    PreparedStatement ps = null;
	    try{
	    	
	    	conn = LAButil.getConnection();
	    	ps = conn.prepareStatement(sql);

	        for (OrdersItemBean item : items) {
	            ps.setInt(1, ordersId);
	            ps.setInt(2, item.getProdId());
	            ps.setInt(3, item.getQuantity());
	            ps.setInt(4, item.getUnitPrice());
	            ps.setInt(5, item.getSubtotal());
	            ps.setString(6, item.getProdName());
	            ps.setString(7, item.getProdImg());
	            ps.addBatch(); // 批次新增
	        }
	        ps.executeBatch();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }finally {
	    	LAButil.closeRs(conn,ps);
		}
	}
	
	//查詢全部
	public List<OrdersBean> findAll(){
		System.out.println("Start Find All");
		List<OrdersBean> ordersList = new ArrayList<OrdersBean>();
		String allSql="SELECT o.orders_id,o.mem_id,o.orders_date,s.ship_name,s.ship_fee,o.total_price,o.final_price FROM orders o LEFT JOIN ship s ON o.ship_id = s.ship_id ORDER BY orders_date ASC";
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		System.out.println("before Try");
		
		
		
		try {
			
			System.out.println("Before Get Connect");
			connection = LAButil.getConnection();
			System.out.println("after get conn");
			ps = connection.prepareStatement(allSql);
			rs= ps.executeQuery();
			if (rs!=null) {
				System.out.println("rs is not null");
			}else {
				System.out.println("rs is null");
			}
			while(rs.next()) {
				int ordersId = rs.getInt("orders_id");
				int memId = rs.getInt("mem_id");
				Timestamp ordersDate = rs.getTimestamp("orders_date");
				String shipName= rs.getString("ship_name");
				int shipFee = rs.getInt("ship_fee");
				int totalPrice = rs.getInt("total_price");
				int finalPrice = rs.getInt("final_price");
				
				OrdersBean bean = new OrdersBean();
				bean.setOrdersId(ordersId);
				bean.setMemId(memId);
				bean.setOrdersDate(ordersDate);
				bean.setShipName(shipName);
				bean.setShipFee(shipFee);
				bean.setTotalPrice(totalPrice);
				bean.setFinalPrice(finalPrice);
				ordersList.add(bean);
				
//				for (OrdersBean ob : ordersList) {
//				    System.out.println("訂單：" + ob.getOrdersId() + " 總金額：" + ob.getTotalPrice() + " 應付金額：" + ob.getFinalPrice());
//				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
//			connection.close();
			System.out.println("final output");
			LAButil.closeRs(connection, ps, rs);
		}
		
		System.out.println("End Find All");
		
		return ordersList;
	}
	
	//查詢單筆(orders_id)
	public OrdersBean getOneOrders(int ordersId) {
		OrdersBean bean = null;
		String getOneOrders="SELECT o.orders_id, o.mem_id, o.orders_date, o.ship_id, o.total_price, o.final_price, s.ship_name, s.ship_fee FROM orders o LEFT JOIN ship s ON o.ship_id = s.ship_id WHERE o.orders_id=?";
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			connection = LAButil.getConnection();
			ps = connection.prepareStatement(getOneOrders);
			ps.setInt(1,ordersId);
			rs = ps.executeQuery();
			
			if(rs.next()) {
				bean = new OrdersBean();
				bean.setOrdersId(rs.getInt("orders_id"));
				bean.setMemId(rs.getInt("mem_id"));
				bean.setOrdersDate(rs.getTimestamp("orders_date"));
				bean.setShipName(rs.getString("ship_name"));
				bean.setShipFee(rs.getInt("ship_fee"));
				bean.setTotalPrice(rs.getInt("total_price"));
				bean.setFinalPrice(rs.getInt("final_price"));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			LAButil.closeRs(connection, ps, rs);
		}
		return bean;
	}
	
	
	//顯示訂單明細
	public List<OrdersItemBean> findItemsByOrderId(int ordersId) {
	    List<OrdersItemBean> itemList = new ArrayList<>();
	    String sql = "SELECT oi.prod_id, p.prod_name, p.prod_img, oi.quantity, oi.unit_price, " +
	                 "(oi.quantity * oi.unit_price) AS subtotal " +
	                 "FROM orders_item oi " +
	                 "JOIN prod p ON oi.prod_id = p.prod_id " +
	                 "WHERE oi.orders_id = ?";
	    
	    Connection conn = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    
	    
	    try{
	    	conn = LAButil.getConnection();
	        ps = conn.prepareStatement(sql);
	        ps.setInt(1, ordersId);
	        rs = ps.executeQuery();
	        
	        while (rs.next()) {
	            OrdersItemBean bean = new OrdersItemBean();
	            bean.setProdId(rs.getInt("prod_id"));
	            bean.setProdName(rs.getString("prod_name"));
	            bean.setProdImg(rs.getString("prod_img")); // 圖片路徑
	            bean.setQuantity(rs.getInt("quantity"));
	            bean.setUnitPrice(rs.getInt("unit_price"));
	            bean.setSubtotal(rs.getInt("subtotal"));
	            itemList.add(bean);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }finally {
	    	LAButil.closeRs(conn, ps, rs);
		}
	    return itemList;
	}
	
	//修改資料
	public void updateOrders(OrdersBean ordersBean) {
		String updateOrders="UPDATE orders SET ship_id=?, ship_fee=?, final_price=? WHERE orders_id=?";
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = LAButil.getConnection();
			ps = connection.prepareStatement(updateOrders);
			ps.setInt(1, ordersBean.getShipId());
			ps.setInt(2, ordersBean.getShipFee());
			ps.setInt(3, ordersBean.getFinalPrice());
			ps.setInt(4, ordersBean.getOrdersId());
			
			int result = ps.executeUpdate();
//			System.out.println("更新筆數:"+result);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			LAButil.closeRs(connection, ps);
		}
	}
	
	//刪除資料
	public void deleteOrders(int ordersId) {
		String deleteOrders="DELETE FROM orders WHERE orders_id=?";
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = LAButil.getConnection();
			ps = connection.prepareStatement(deleteOrders);
			ps.setInt(1, ordersId);
			ps.executeUpdate();
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			LAButil.closeRs(connection, ps);
		}
		
	}
	
	
}
