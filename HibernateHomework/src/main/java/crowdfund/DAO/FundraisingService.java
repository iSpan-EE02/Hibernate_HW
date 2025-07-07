package crowdfund.DAO;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Session;

import crowdfund.bean.CampaignBean;

public class FundraisingService implements IFundraisingService{
	
	private FundraisingDao fDao;
	
	public FundraisingService(Session session) {
		fDao = new FundraisingDao(session);
	}

	@Override
	public CampaignBean add(CampaignBean addBean) throws SQLException {
		return fDao.add(addBean);
	}

	@Override
	public CampaignBean update(CampaignBean updateBean) throws SQLException {
		return fDao.update(updateBean);
	}

	@Override
	public boolean deleteById(Integer id) throws SQLException {
		return fDao.deleteById(id);
	}

	@Override
	public CampaignBean queryById(Integer id) throws SQLException {
		return fDao.queryById(id);
	}

	@Override
	public List<CampaignBean> queryByCategory(String category) throws SQLException {
		return fDao.queryByCategory(category);
	}

	@Override
	public List<CampaignBean> queryByStatus(String status) throws SQLException {
		return fDao.queryByStatus(status);
	}

	@Override
	public List<CampaignBean> queryAll() throws SQLException {
		return fDao.queryAll();
	}

	@Override
	public List<CampaignBean> queryByName(String campaignName) throws SQLException {
		return fDao.queryByName(campaignName);
	}
	

}
