package fi.softala.ttl.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.softala.ttl.dao.PassiDAO;

@Service("passiService")
@Transactional
public class PassiServiceImpl implements PassiService {

	@Inject
	private PassiDAO dao;

	public PassiDAO getDao() {
		return dao;
	}

	public void setDao(PassiDAO dao) {
		this.dao = dao;
	}

	@Override
	public void saveFeadback(int waypointID, int instructorRating, String instructorComment) {
		dao.saveFeedback(waypointID, instructorRating, instructorComment);	
	}
	
}
