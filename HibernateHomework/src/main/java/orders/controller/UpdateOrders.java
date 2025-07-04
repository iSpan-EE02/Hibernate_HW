package orders.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import orders.bean.OrdersBean;
import orders.bean.ShipBean;
import orders.dao.OrdersDao;
import orders.dao.ShipDao;
import orders.utils.LAButil;

import java.awt.Label;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;


@WebServlet("/UpdateOrders")
public class UpdateOrders extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public UpdateOrders() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("DEBUG >>> orders_id=" + request.getParameter("orders_id"));
		OrdersBean orders = new OrdersBean();
		int ordersId =Integer.parseInt(request.getParameter("orders_id"));
		List<OrdersBean> ordersBeans =new ArrayList<>();
		orders.setOrdersId(ordersId);
		ordersBeans.add(orders);
		System.out.println("GET方法");
		
		
		try {
			
//	        Context context = new InitialContext();
//	        DataSource ds = (DataSource) context.lookup("java:/comp/env/jdbc/team06db");
			
//	        Connection conn = LAButil.getConnection();
	        	OrdersDao dao = new OrdersDao();
	        	OrdersBean ordersBean = dao.getOneOrders(ordersId);
	            request.setAttribute("orders", ordersBean);
	            ShipDao shipDao = new ShipDao();
	            List<ShipBean> shipList = shipDao.findAll(); // 或其他取得所有運送方式的方法
	            request.setAttribute("shipList", shipList);
	            request.getRequestDispatcher("/jsp/orders/OrdersUpdate.jsp").forward(request, response);
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		System.out.println("UPDATE DAO成功");
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		doGet(request, response);
		System.out.println("post方法");
		
		int ordersId =Integer.parseInt(request.getParameter("orders_id"));
		int memId = Integer.parseInt(request.getParameter("mem_id"));
//		Date ordersDate =Date.valueOf(request.getParameter("orders_date"));
		int totalPrice = Integer.parseInt(request.getParameter("total_price"));
		int shipId = Integer.parseInt(request.getParameter("ship_id"));
		int shipFee = Integer.parseInt(request.getParameter("ship_fee"));
		int finalPrice = Integer.parseInt(request.getParameter("final_price"));
		
		System.out.println("id" + ordersId);
		System.out.println("mem id"+ memId);
		System.out.println("totol price" + totalPrice);
		System.out.println("ship id" + shipId);
		System.out.println("ship Fee" + shipFee);
		System.out.println("final price" + finalPrice);
		
		
		OrdersBean bean = new OrdersBean();
		bean.setOrdersId(ordersId);
		bean.setMemId(memId);
		bean.setTotalPrice(totalPrice);
		bean.setShipId(shipId);
		bean.setShipFee(shipFee);
		bean.setFinalPrice(finalPrice);
		
		try {

	        	OrdersDao dao = new OrdersDao();
	        	dao.updateOrders(bean);

	}catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
		System.out.println("Error發生");
	}
		response.sendRedirect(request.getContextPath() + "/AllOrders");
		System.out.println(request.getContextPath());
		
	}

}