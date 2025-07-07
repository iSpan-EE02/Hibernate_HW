package tw.leonchen.model;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Session;

public class HouseService implements IHouseService {
	
	private HouseDao hDao;
	

	public HouseService(Session session) {
		hDao = new HouseDao(session);
	}

	@Override
	public House insert(House insertBean) throws SQLException {
		
		return hDao.insert(insertBean);
	}

	@Override
	public House update(House updateBean) throws SQLException {
		return hDao.update(updateBean);
	}

	@Override
	public boolean deleteById(House deleteBean) throws SQLException {
		return hDao.deleteById(deleteBean);
	}

	@Override
	public House queryById(Integer id) throws SQLException {
		return hDao.queryById(id);
	}

	@Override
	public List<House> queryAll() throws SQLException {
		return hDao.queryAll();
	}

}
