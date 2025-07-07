package crowdfund.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import team.common.util.HibernateUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import crowdfund.DAO.FundraisingDao;
import crowdfund.DAO.FundraisingService;
import crowdfund.bean.CampaignBean;

@WebServlet("/CrowdFundSearchByCamCategory")
public class CrowdFundSearchByCamCategory extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			processAction(request, response);
		} catch (SQLException | ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		String campaignCategory = request.getParameter("campaign_category");
		FundraisingDao dao = new FundraisingDao();
		List<CampaignBean> cams = dao.SearchByCategory(campaignCategory);
		request.setAttribute("cams",cams);
		request.getRequestDispatcher("/jsp/crowdfund/GetAllCams.jsp").forward(request, response);
		
		
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		*/
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			processAction(request, response);
		} catch (SQLException | ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void processAction(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		
		SessionFactory factory = HibernateUtil.getSessionFactory();
		Session session = factory.getCurrentSession();
		
		FundraisingService fService = new FundraisingService(session);
		String campaignCategory = request.getParameter("campaign_category");
		List<CampaignBean> cams = fService.queryByCategory(campaignCategory);
		request.setAttribute("cams", cams);
		request.getRequestDispatcher("/jsp/crowdfund/GetAllCams.jsp").forward(request, response);
		
	}
}
