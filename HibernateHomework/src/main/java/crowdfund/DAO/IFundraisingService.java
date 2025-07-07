package crowdfund.DAO;

import java.sql.SQLException;
import java.util.List;

import crowdfund.bean.CampaignBean;

public interface IFundraisingService {
	public CampaignBean add(CampaignBean addBean) throws SQLException;
	public CampaignBean update(CampaignBean updateBean) throws SQLException;
	public boolean deleteById(Integer id) throws SQLException;
	public CampaignBean queryById(Integer id) throws SQLException;
	public List<CampaignBean> queryByCategory(String category) throws SQLException;
	public List<CampaignBean> queryByStatus(String status) throws SQLException;
	public List<CampaignBean> queryByName(String campaignName) throws SQLException;
	public List<CampaignBean> queryAll() throws SQLException;
	

}
