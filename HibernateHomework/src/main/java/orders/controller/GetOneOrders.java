package orders.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import orders.bean.*;
import orders.dao.OrdersDao;
import orders.dao.OrdersItemDao;
import orders.utils.LAButil;

@WebServlet("/GetOneOrders")
public class GetOneOrders extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int ordersId =Integer.parseInt(request.getParameter("orders_id")) ;
		
		 OrdersDao ordersDao = new OrdersDao();
	        OrdersItemDao itemDao = new OrdersItemDao(); // 你需要自己建立這個 DAO

	        OrdersBean orders = ordersDao.getOneOrders(ordersId);
	        List<OrdersItemBean> itemList = itemDao.getItemsByOrdersId(ordersId);

	        request.setAttribute("orders", orders);
	        request.setAttribute("itemList", itemList);

	        request.getRequestDispatcher("/jsp/orders/GetOneOrders.jsp").forward(request, response);
	        System.out.println("明細數量：" + itemList.size());
	    }
	
		

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
