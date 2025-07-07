package crowdfund.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import team.common.util.HibernateUtil;

import java.io.IOException;
import java.sql.SQLException;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import crowdfund.DAO.FundraisingDao;
import crowdfund.DAO.FundraisingService;

@WebServlet("/CrowdFundDeleteCam")
public class CrowdFundDeleteCam extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			proccessAction(request, response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			proccessAction(request, response);
		} catch (IOException | ServletException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*String idStr = request.getParameter("campaign_id");
		try {
            int campaignId = Integer.parseInt(idStr);
            FundraisingDao dao = new FundraisingDao();
            dao.deleteCamById(campaignId);
            request.setAttribute("cams", dao.getAllCams());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
		request.getRequestDispatcher("/jsp/crowdfund/GetAllCams.jsp").forward(request, response);
*/
}
	private void proccessAction(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SQLException {
		response.setContentType("text/html;charset=UTF-8");
		SessionFactory factory = HibernateUtil.getSessionFactory();
		Session session = factory.getCurrentSession();
		
		FundraisingService fService = new FundraisingService(session);
		
		String idStr = request.getParameter("campaign_id");
		int campaignId = Integer.parseInt(idStr);
		fService.deleteById(campaignId);
		request.setAttribute("cams", fService.queryAll());
		request.getRequestDispatcher("/jsp/crowdfund/GetAllCams.jsp").forward(request, response);
		
	}
}
