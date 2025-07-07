package tw.leonchen.action;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import team.common.util.HibernateUtil;
import tw.leonchen.model.House;
import tw.leonchen.model.HouseService;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;


@WebServlet("/DemoHouseServletAction.do")
public class DemoHouseServletAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processAction(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processAction(request,response);
	}

	private void processAction(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=UTF8");
		PrintWriter out = response.getWriter();
		
		SessionFactory factory = HibernateUtil.getSessionFactory();
		Session session = factory.getCurrentSession();
		
		HouseService hService = new HouseService(session);
		try {
			House house = new House("Happy House");
			hService.insert(house);
//			House resultBean = hService.queryById(1000);
//			if (resultBean!=null) {
//				out.write(resultBean.getHouseid()+" "+resultBean.getHousename());
//			}else {
//				out.write("no result");
//			}
			out.write("insert success");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		out.close();
	}

}
