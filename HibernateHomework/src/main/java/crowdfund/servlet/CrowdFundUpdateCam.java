package crowdfund.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import team.common.util.HibernateUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import crowdfund.DAO.FundraisingService; 
import crowdfund.bean.CampaignBean; 

@MultipartConfig(
	maxFileSize = 50 * 1024 * 1024, // 最大檔案大小 50MB
	maxRequestSize = 100 * 1024 * 1024, // 最大請求大小 100MB
	fileSizeThreshold = 1024 * 1024 // 1MB 用於存放臨時檔案
)
@WebServlet("/CrowdFundUpdateCam")
public class CrowdFundUpdateCam extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

		SessionFactory factory = HibernateUtil.getSessionFactory();
		Session session = factory.getCurrentSession();
		FundraisingService fService = new FundraisingService(session);

		String campaignIdStr = request.getParameter("campaign_id");
		if (campaignIdStr == null || campaignIdStr.trim().isEmpty()) {
			request.setAttribute("error", "缺少活動編號，請重新操作。");
			request.getRequestDispatcher("/jsp/crowdfund/UpdateCam.jsp").forward(request, response);
			return;
		}

		int campaignId;
		try {
			campaignId = Integer.parseInt(campaignIdStr);
		} catch (NumberFormatException e) {
			request.setAttribute("error", "活動編號格式錯誤。");
			request.getRequestDispatcher("/jsp/crowdfund/UpdateCam.jsp").forward(request, response);
			return;
		}

		CampaignBean cam = null;
		try {
			cam = fService.queryById(campaignId);
		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("error", "查詢資料庫時發生錯誤: " + e.getMessage());
			request.getRequestDispatcher("/jsp/crowdfund/UpdateCam.jsp").forward(request, response);
			return;
		}
		if (cam == null) {
			request.setAttribute("error", "找不到該活動資料。");
			request.getRequestDispatcher("/jsp/crowdfund/UpdateCam.jsp").forward(request, response);
			return;
		}

		request.setAttribute("cam", cam);
		request.getRequestDispatcher("/jsp/crowdfund/UpdateCam.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
//		System.out.println("測試: 開始分析 request.getParts()");
//
//		for (Part part : request.getParts()) {
//		    System.out.println("收到 Part: " + part.getName());
//		    System.out.println("是否有內容: " + (part.getSize() > 0));
//		}
		
		response.setContentType("text/html;charset=UTF-8");

		SessionFactory factory = HibernateUtil.getSessionFactory();
		Session session = factory.getCurrentSession();
		FundraisingService fService = new FundraisingService(session);

		List<String> errors = new ArrayList<>();
		String campaignIdStr = request.getParameter("campaign_id");
		int campaignId = 0;
		try {
		    campaignId = Integer.parseInt(campaignIdStr);
		} catch (NumberFormatException e) {
		    errors.add("活動編號格式錯誤");
		}
		String campaignName = request.getParameter("campaign_name");
		String campaignCategory = request.getParameter("campaign_category");
		String goalAmountStr = request.getParameter("goal_amount");
		String currentAmountStr = request.getParameter("current_amount");
		String startDateStr = request.getParameter("start_date");
		String endDateStr = request.getParameter("end_date");
		String status = request.getParameter("status");
		String creatorIdStr = request.getParameter("creator_id");
		String description = request.getParameter("description");
		double goalAmount = 0;
		double currentAmount = 0;
		int creatorId = 0;
		Date startDate = null;
		Date endDate = null;
		if (campaignName == null || campaignName.trim().isEmpty()) {
		    errors.add("請輸入活動名稱");
		}
		try {
		    goalAmount = Double.parseDouble(goalAmountStr);
		    if (goalAmount <= 0) errors.add("目標金額必須為正數");
		} catch (Exception e) {
		    errors.add("目標金額格式錯誤");
		}
		try {
		    currentAmount = Double.parseDouble(currentAmountStr);
		    if (currentAmount < 0) errors.add("目前金額不能為負");
		} catch (Exception e) {
		    errors.add("目前金額格式錯誤");
		}
		try {
		    startDate = Date.valueOf(startDateStr);
		} catch (Exception e) {
		    errors.add("開始日期格式錯誤");
		}
		try {
		    endDate = Date.valueOf(endDateStr);
		} catch (Exception e) {
		    errors.add("結束日期格式錯誤");
		}
		try {
		    creatorId = Integer.parseInt(creatorIdStr);
		} catch (Exception e) {
		    errors.add("建立者ID格式錯誤");
		}
		
		
		String imagePath = null;
		try {
		    Part imagePart = request.getPart("cover_image");

		    if (imagePart != null && imagePart.getSize() > 0) {
		        String fileName = Paths.get(imagePart.getSubmittedFileName()).getFileName().toString();
		        String uploadPath = getServletContext().getRealPath("/jsp/crowdfund/uploads");
		        File uploadDir = new File(uploadPath);
		        if (!uploadDir.exists()) uploadDir.mkdirs();

		        String filePath = uploadPath + File.separator + fileName;
		        imagePart.write(filePath);
		        imagePath = "jsp/crowdfund/uploads/" + fileName;
		    } else {
		        String oldImagePath = request.getParameter("original_cover_image");
		        imagePath = oldImagePath != null ? oldImagePath : "jsp/crowdfund/uploads/default.jpg";
		    }
		} catch (IllegalStateException e) {
		    errors.add("Tomcat 限制檔案欄位數量，請確認只選擇單一圖片！");
		    e.printStackTrace();
		} catch (IOException | ServletException e) {
		    errors.add("圖片上傳失敗：" + e.getMessage());
		    e.printStackTrace();
		}
		
		if (!errors.isEmpty()) {
		    CampaignBean cam = new CampaignBean();
		    cam.setCampaignID(campaignId);
		    cam.setCampaignName(campaignName);
		    cam.setCampaignCategory(campaignCategory);
		    cam.setGoalAmount(goalAmount);
		    cam.setCurrentAmount(currentAmount);
		    cam.setStartDate(startDate);
		    cam.setEndDate(endDate);
		    cam.setCoverImage(imagePath);
		    cam.setStatus(status);
		    cam.setCreatorID(creatorId);
		    cam.setDescription(description);
		    request.setAttribute("cam", cam);
		    request.setAttribute("errors", errors);
		    request.getRequestDispatcher("/jsp/crowdfund/UpdateCam.jsp").forward(request, response);
		    return;
		}
		
		System.out.println("campaign_name: " + request.getParameter("campaign_name"));
		System.out.println("goal_amount: " + request.getParameter("goal_amount"));
		System.out.println("start_date: " + request.getParameter("start_date"));

		
		
		
		CampaignBean cam = new CampaignBean();
		cam.setCampaignID(campaignId);
		cam.setCampaignName(campaignName);
		cam.setCampaignCategory(campaignCategory);
		cam.setGoalAmount(goalAmount);
		cam.setCurrentAmount(currentAmount);
		cam.setStartDate(startDate);
		cam.setEndDate(endDate);
		cam.setCoverImage(imagePath);
		cam.setStatus(status);
		cam.setCreatorID(creatorId);
		cam.setDescription(description);
		try {
		    fService.update(cam);
		} catch (SQLException e) {
		    e.printStackTrace();
		}
		response.sendRedirect(request.getContextPath() + "/CrowdFundGetAllCams");
}
}