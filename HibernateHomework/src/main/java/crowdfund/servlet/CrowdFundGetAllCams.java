package crowdfund.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import team.common.util.HibernateUtil;
import tw.leonchen.model.HouseService;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import crowdfund.DAO.FundraisingDao;
import crowdfund.DAO.FundraisingService;
import crowdfund.bean.CampaignBean;


@WebServlet("/CrowdFundGetAllCams")
public class CrowdFundGetAllCams extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		processAction(request, response);
		
		/*
		FundraisingDao dao = new FundraisingDao();
		List<CampaignBean> cams = dao.getAllCams();
		request.setAttribute("cams", cams);
		
		request.getRequestDispatcher("/jsp/crowdfund/GetAllCams.jsp").forward(request, response);
			*/
			
		
		
		
		
//		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processAction(request, response);
	}

	private void processAction(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/html;charset=UTF-8");
		
		SessionFactory factory = HibernateUtil.getSessionFactory();
		Session session = factory.getCurrentSession();
		
		FundraisingService fService = new FundraisingService(session);
		try {
			List<CampaignBean> cams = fService.queryAll();
			request.setAttribute("cams", cams);
			request.getRequestDispatcher("/jsp/crowdfund/GetAllCams.jsp").forward(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		
		
		
	}
}
}
